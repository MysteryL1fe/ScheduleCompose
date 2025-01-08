package ru.khanin.dmitrii.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import ru.khanin.dmitrii.schedule.compose.entity.Cabinet
import ru.khanin.dmitrii.schedule.compose.entity.Flow
import ru.khanin.dmitrii.schedule.compose.entity.Subject
import ru.khanin.dmitrii.schedule.compose.entity.Teacher
import ru.khanin.dmitrii.schedule.compose.entity.TempSchedule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TempScheduleRepo(
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
        lessonDate: LocalDate,
        lessonNum: Int,
        willLessonBe: Boolean,
        subject: String?,
        surname: String?,
        name: String?,
        patronymic: String?,
        cabinet: String?,
        building: String?
    ): Long {
        val foundFlow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
        val flowId = foundFlow?.id ?: flowRepo.add(educationLevel, course, group, subgroup)

        var subjectId: Long? = null
        if (subject?.isNotBlank() == true) {
            val foundSubject = subjectRepo.findBySubject(subject)
            subjectId = foundSubject?.id ?: subjectRepo.add(subject)
        }

        var teacherId: Long? = null
        if (surname?.isNotBlank() == true) {
            val foundTeacher = teacherRepo.findBySurnameAndNameAndPatronymic(surname, name, patronymic)
            teacherId = foundTeacher?.id ?: teacherRepo.add(surname, name, patronymic)
        }

        var cabinetId: Long? = null
        if (cabinet?.isNotBlank() == true) {
            val foundCabinet = cabinetRepo.findByCabinetAndBuilding(cabinet, building)
            cabinetId = foundCabinet?.id ?: cabinetRepo.add(cabinet, building)
        }

        val values = ContentValues()
        values.put("flow", flowId)
        values.put("lesson_date", lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        values.put("lesson_num", lessonNum)
        values.put("will_lesson_be", if (willLessonBe) "1" else "0")
        values.put("subject", subjectId)
        values.put("teacher", teacherId)
        values.put("cabinet", cabinetId)

        val db = dbHelper.writableDatabase
        val id = db.insert("temp_schedule", null, values)
        db.close()
        return id
    }

    fun update(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        lessonDate: LocalDate,
        lessonNum: Int,
        willLessonBe: Boolean,
        subject: String?,
        surname: String?,
        name: String?,
        patronymic: String?,
        cabinet: String?,
        building: String?
    ): Int {
        val foundFlow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
        val flowId = foundFlow?.id ?: flowRepo.add(educationLevel, course, group, subgroup)

        var subjectId: Long? = null
        if (subject?.isNotBlank() == true) {
            val foundSubject = subjectRepo.findBySubject(subject)
            subjectId = foundSubject?.id ?: subjectRepo.add(subject)
        }

        var teacherId: Long? = null
        if (surname?.isNotBlank() == true) {
            val foundTeacher = teacherRepo.findBySurnameAndNameAndPatronymic(surname, name, patronymic)
            teacherId = foundTeacher?.id ?: teacherRepo.add(surname, name, patronymic)
        }

        var cabinetId: Long? = null
        if (cabinet?.isNotBlank() == true) {
            val foundCabinet = cabinetRepo.findByCabinetAndBuilding(cabinet, building)
            cabinetId = foundCabinet?.id ?: cabinetRepo.add(cabinet, building)
        }

        val values = ContentValues()
        values.put("will_lesson_be", if (willLessonBe) "1" else "0")
        values.put("subject", subjectId)
        values.put("teacher", teacherId)
        values.put("cabinet", cabinetId)

        val whereClause = "flow=? AND lesson_date=? AND lesson_num=?"
        val whereArgs = arrayOf(flowId.toString(), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), lessonNum.toString())

        val db = dbHelper.writableDatabase
        val count = db.update("temp_schedule", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun addOrUpdate(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        lessonDate: LocalDate,
        lessonNum: Int,
        willLessonBe: Boolean,
        subject: String?,
        surname: String?,
        name: String?,
        patronymic: String?,
        cabinet: String?,
        building: String?
    ) {
        val foundTempSchedule = findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, lessonDate, lessonNum)
        foundTempSchedule?.let {
            update(educationLevel, course, group, subgroup, lessonDate, lessonNum, willLessonBe, subject, surname, name, patronymic, cabinet, building)
        } ?: run {
            add(educationLevel, course, group, subgroup, lessonDate, lessonNum, willLessonBe, subject, surname, name, patronymic, cabinet, building)
        }
    }

    @SuppressLint("Range")
    fun findByFlowAndLessonDateAndLessonNum(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        lessonDate: LocalDate,
        lessonNum: Int
    ): TempSchedule? {
        var result: TempSchedule? = null

        val sql = """
            SELECT ts.id AS temp_id, ts.flow AS flow_id, ts.subject AS subject_id, ts.teacher AS teacher_id, ts.cabinet AS cabinet_id,
                ts.will_lesson_be, s.subject, t.surname, t.name, t.patronymic, c.cabinet, c.building, c.address
            FROM temp_schedule ts
            JOIN flow f ON ts.flow = f.id
            LEFT JOIN subject s ON ts.subject = s.id
            LEFT JOIN teacher t ON ts.teacher = t.id
            LEFT JOIN cabinet c ON ts.cabinet = c.id
            WHERE education_level=? AND course=? AND _group=? AND subgroup=? AND lesson_date=? AND lesson_num=?
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString(), lessonDate.toString(), lessonNum.toString())
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

            val id = cursor.getLong(cursor.getColumnIndex("temp_id"))
            val willLessonBe = cursor.getInt(cursor.getColumnIndex("will_lesson_be")) == 1

            result = TempSchedule(
                id,
                Flow(flowId, educationLevel, course, group, subgroup),
                lessonDate,
                lessonNum,
                willLessonBe,
                if (subjectId == 0L) null else Subject(subjectId, subject),
                if (teacherId == 0L) null else Teacher(teacherId, surname, name, patronymic),
                if (cabinetId == 0L) null else Cabinet(cabinetId, cabinet, building, address)
            )
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAllByFlow(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int
    ): List<TempSchedule> {
        var result = mutableListOf<TempSchedule>()

        val sql = """
            SELECT ts.id AS temp_id, ts.flow AS flow_id, ts.subject AS subject_id, ts.teacher AS teacher_id, ts.cabinet AS cabinet_id,
                ts.lesson_date, ts.lesson_num, ts.will_lesson_be,
                s.subject, t.surname, t.name, t.patronymic, c.cabinet, c.building, c.address
            FROM temp_schedule ts
            JOIN flow f ON ts.flow = f.id
            LEFT JOIN subject s ON ts.subject = s.id
            LEFT JOIN teacher t ON ts.teacher = t.id
            LEFT JOIN cabinet c ON ts.cabinet = c.id
            WHERE education_level=? AND course=? AND _group=? AND subgroup=? AND lesson_date=? AND lesson_num=?
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString())
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

            val id = cursor.getLong(cursor.getColumnIndex("temp_id"))
            val lessonDate = LocalDate.parse(cursor.getString(cursor.getColumnIndex("lesson_date")), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val lessonNum = cursor.getInt(cursor.getColumnIndex("lesson_num"))
            val willLessonBe = cursor.getInt(cursor.getColumnIndex("will_lesson_be")) == 1

            result.add(TempSchedule(
                id,
                Flow(flowId, educationLevel, course, group, subgroup),
                lessonDate,
                lessonNum,
                willLessonBe,
                if (subjectId == 0L) null else Subject(subjectId, subject),
                if (teacherId == 0L) null else Teacher(teacherId, surname, name, patronymic),
                if (cabinetId == 0L) null else Cabinet(cabinetId, cabinet, building, address)
            ))
        }
        cursor.close()
        db.close()
        return result
    }

    fun deleteByFlowAndLessonDateAndLessonNum(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        lessonDate: LocalDate,
        lessonNum: Int
    ): Int {
        val foundFlow: Flow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup) ?: return 0

        val whereClause = "flow=? AND lesson_date=? AND lesson_num=?"
        val whereArgs = arrayOf(foundFlow.id.toString(), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), lessonNum.toString())

        val db = dbHelper.writableDatabase
        val count = db.delete("temp_schedule", whereClause, whereArgs)
        db.close()
        return count
    }

    fun deleteAllBeforeDate(
        date: LocalDate
    ): Int {
        val whereClause = "lesson_date<?"
        val whereArgs = arrayOf(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

        val db = dbHelper.writableDatabase
        val count = db.delete("temp_schedule", whereClause, whereArgs)
        db.close()
        return count
    }
}
