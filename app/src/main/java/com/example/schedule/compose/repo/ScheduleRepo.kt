package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.schedule.compose.entity.Cabinet
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.Subject
import com.example.schedule.compose.entity.Teacher

class ScheduleRepo(
    private val dbHelper: ScheduleDBHelper,
    private val flowRepo: FlowRepo,
    private val subjectRepo: SubjectRepo,
    private val teacherRepo: TeacherRepo,
    private val cabinetRepo: CabinetRepo
) {
    fun add(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        dayOfWeek: Int,
        lessonNum: Int,
        numerator: Boolean,
        subject: String,
        surname: String?,
        name: String?,
        patronymic: String?,
        cabinet: String?,
        building: String?
    ): Long {
        val foundFlow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(
            educationLevel, course, group, subgroup
        )
        val flowId = foundFlow?.id ?: flowRepo.add(educationLevel, course, group, subgroup)

        val foundSubject = subjectRepo.findBySubject(
            subject
        )
        val subjectId = foundSubject?.id ?: subjectRepo.add(subject)

        var teacherId: Long? = null
        surname?.let {
            val foundTeacher = teacherRepo.findBySurnameAndNameAndPatronymic(
                surname, name, patronymic
            )
            teacherId = foundTeacher?.id ?: teacherRepo.add(surname, name, patronymic)
        }

        var cabinetId: Long? = null
        cabinet?.let {
            val foundCabinet = cabinetRepo.findByCabinetAndBuilding(
                cabinet, building
            )
            cabinetId = foundCabinet?.id ?: cabinetRepo.add(cabinet, building)
        }

        val values = ContentValues()
        values.put("flow", flowId)
        values.put("day_of_week", dayOfWeek)
        values.put("lesson_num", lessonNum)
        values.put("numerator", if (numerator) "1" else "0")
        values.put("subject", subjectId)
        teacherId?.let { values.put("teacher", teacherId) }
        cabinetId?.let { values.put("cabinet", cabinetId) }

        val db = dbHelper.writableDatabase
        val id: Long = db.insert("schedule", null, values)
        db.close()
        return id
    }

    fun update(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        dayOfWeek: Int,
        lessonNum: Int,
        numerator: Boolean,
        subject: String,
        surname: String?,
        name: String?,
        patronymic: String?,
        cabinet: String?,
        building: String?
    ): Int {
        val foundFlow: Flow? = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(
            educationLevel, course, group, subgroup
        )
        val flowId = foundFlow?.id ?: flowRepo.add(educationLevel, course, group, subgroup)

        val foundSubject = subjectRepo.findBySubject(
            subject
        )
        val subjectId = foundSubject?.id ?: subjectRepo.add(subject)

        var teacherId: Long? = null
        surname?.let {
            val foundTeacher = teacherRepo.findBySurnameAndNameAndPatronymic(
                surname, name, patronymic
            )
            teacherId = foundTeacher?.id ?: teacherRepo.add(surname, name, patronymic)
        }

        var cabinetId: Long? = null
        cabinet?.let {
            val foundCabinet = cabinetRepo.findByCabinetAndBuilding(
                cabinet, building
            )
            cabinetId = foundCabinet?.id ?: cabinetRepo.add(cabinet, building)
        }

        val values = ContentValues()
        values.put("subject", subjectId)
        values.put("teacher", teacherId)
        values.put("cabinet", cabinetId)

        val whereClause = "flow=? AND day_of_week=? AND lesson_num=? AND numerator=?"
        val whereArgs = arrayOf(
            flowId.toString(), dayOfWeek.toString(), lessonNum.toString(),
            if (numerator) "1" else "0"
        )

        val db = dbHelper.writableDatabase
        val count: Int = db.update("schedule", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun addOrUpdate(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        dayOfWeek: Int,
        lessonNum: Int,
        numerator: Boolean,
        subject: String,
        surname: String?,
        name: String?,
        patronymic: String?,
        cabinet: String?,
        building: String?
    ) {
        val foundSchedule = findByFlowAndDayOfWeekAndLessonNumAndNumerator(
            educationLevel, course, group, subgroup, dayOfWeek, lessonNum, numerator
        )
        foundSchedule?.let {
            update(educationLevel, course, group, subgroup, dayOfWeek, lessonNum, numerator, subject, surname, name, patronymic, cabinet, building)
        } ?: run {
            add(educationLevel, course, group, subgroup, dayOfWeek, lessonNum, numerator, subject, surname, name, patronymic, cabinet, building)
        }
    }

    @SuppressLint("Range")
    fun findByFlowAndDayOfWeekAndLessonNumAndNumerator(
        educationLevel: Int, course: Int, group: Int, subgroup: Int,
        dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Schedule? {
        var result: Schedule? = null

        val sql = """
            SELECT sch.id AS schedule_id, sch.flow AS flow_id, sch.subject AS subject_id, sch.teacher AS teacher_id, sch.cabinet AS cabinet_id,
                sub.subject, t.surname, t.name, t.patronymic, c.cabinet, c.building, c.address
            FROM schedule sch
            JOIN flow f ON sch.flow = f.id
            JOIN subject sub ON sch.subject = sub.id
            LEFT JOIN teacher t ON sch.teacher = t.id
            LEFT JOIN cabinet c ON sch.cabinet = c.id
            WHERE education_level=? AND course=? AND _group=? AND subgroup=? AND day_of_week=? AND lesson_num=? AND numerator=?
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString(), dayOfWeek.toString(), lessonNum.toString(), if (numerator) "1" else "0")
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val flowId = cursor.getLong(cursor.getColumnIndex("flow_id"))

            val subjectId = cursor.getLong(cursor.getColumnIndex("subject_id"))
            val subject = cursor.getString(cursor.getColumnIndex("subject"))

            val teacherId = cursor.getLong(cursor.getColumnIndex("teacher_id"))
            val surname = cursor.getString(cursor.getColumnIndex("surname"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val patronymic = cursor.getString(cursor.getColumnIndex("patronymic"))

            val cabinetId = cursor.getLong(cursor.getColumnIndex("cabinet_id"))
            val cabinet = cursor.getString(cursor.getColumnIndex("cabinet"))
            val building = cursor.getString(cursor.getColumnIndex("building"))
            val address = cursor.getString(cursor.getColumnIndex("address"))

            val id = cursor.getLong(cursor.getColumnIndex("schedule_id"))

            result = Schedule(
                id,
                Flow(flowId, educationLevel, course, group, subgroup),
                dayOfWeek,
                lessonNum,
                numerator,
                Subject(subjectId, subject),
                if (teacherId == 0L) null else Teacher(teacherId, surname, name, patronymic),
                if (cabinetId == 0L) null else Cabinet(cabinetId, cabinet, building, address)
            )
        }
        cursor.close()
        db.close()
        return result
    }

    fun deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        dayOfWeek: Int,
        lessonNum: Int,
        numerator: Boolean
    ): Int {
        val foundFlow: Flow? = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(
            educationLevel, course, group, subgroup
        )
        if (foundFlow == null) return 0

        val whereClause = "flow=? AND day_of_week=? AND lesson_num=? AND numerator=?"
        val whereArgs = arrayOf(foundFlow.id.toString(), dayOfWeek.toString(), lessonNum.toString(), if (numerator) "1" else "0")

        val db = dbHelper.writableDatabase
        val count: Int = db.delete("schedule", whereClause, whereArgs)
        db.close()
        return count
    }
}
