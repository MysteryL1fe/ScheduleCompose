package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Subject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeworkRepo(
    private val dbHelper: ScheduleDBHelper,
    private val flowRepo: FlowRepo,
    private val subjectRepo: SubjectRepo
) {
    fun add(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        homework: String,
        lessonDate: LocalDate,
        lessonNum: Int,
        subject: String
    ): Long {
        val foundFlow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
        val flowId = foundFlow?.id ?: flowRepo.add(educationLevel, course, group, subgroup)

        val foundSubject = subjectRepo.findBySubject(subject)
        val subjectId = foundSubject?.id ?: subjectRepo.add(subject)

        val values = ContentValues()
        values.put("flow", flowId)
        values.put("homework", homework)
        values.put("lesson_date", lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        values.put("lesson_num", lessonNum)
        values.put("subject", subjectId)

        val db = dbHelper.writableDatabase
        val id = db.insert("homework", null, values)
        db.close()
        return id
    }

    fun update(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        homework: String,
        lessonDate: LocalDate,
        lessonNum: Int,
        subject: String
    ): Int {
        val foundFlow = flowRepo.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
        val flowId = foundFlow?.id ?: flowRepo.add(educationLevel, course, group, subgroup)

        val foundSubject = subjectRepo.findBySubject(subject)
        val subjectId = foundSubject?.id ?: subjectRepo.add(subject)

        val values = ContentValues()
        values.put("homework", homework)
        values.put("subject", subjectId)

        val whereClause = "flow=? AND lesson_date=? AND lesson_num=?"
        val whereArgs = arrayOf(flowId.toString(), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), lessonNum.toString())

        val db = dbHelper.writableDatabase
        val count = db.update("homework", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun addOrUpdate(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        homework: String,
        lessonDate: LocalDate,
        lessonNum: Int,
        subject: String
    ) {
        val foundHomework = findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, lessonDate, lessonNum)
        foundHomework?.let {
            update(educationLevel, course, group, subgroup, homework, lessonDate, lessonNum, subject)
        } ?: run {
            add(educationLevel, course, group, subgroup, homework, lessonDate, lessonNum, subject)
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
    ): Homework? {
        var result: Homework? = null

        val sql = """
            SELECT h.id AS homework_id, h.flow AS flow_id, h.subject AS subject_id, h.homework, s.subject
            FROM homework h
            JOIN flow f ON h.flow = f.id
            JOIN subject s ON h.subject = s.id
            WHERE education_level=? AND course=? AND _group=? AND subgroup=? AND lesson_date=? AND lesson_num=?
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString(), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), lessonNum.toString())
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val flowId = cursor.getLong(cursor.getColumnIndex("flow_id"))

            val subjectId = cursor.getLong(cursor.getColumnIndex("subject_id"))
            val subject = cursor.getString(cursor.getColumnIndex("subject"))

            val id = cursor.getLong(cursor.getColumnIndex("homework_id"))
            val homework = cursor.getString(cursor.getColumnIndex("homework"))

            result = Homework(
                id,
                Flow(flowId, educationLevel, course, group, subgroup),
                homework,
                lessonDate,
                lessonNum,
                Subject(subjectId, subject)
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
    ): List<Homework> {
        val result = mutableListOf<Homework>()

        val sql = """
            SELECT h.id AS homework_id, h.flow AS flow_id, h.subject AS subject_id,
                h.homework, h.lesson_date, h.lesson_num, s.subject
            FROM homework h
            JOIN flow f ON h.flow = f.id
            JOIN subject s ON h.subject = s.id
            WHERE education_level=? AND course=? AND _group=? AND subgroup=?
            ORDER BY lesson_date, lesson_num
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString())
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val flowId = cursor.getLong(cursor.getColumnIndex("flow_id"))

            val subjectId = cursor.getLong(cursor.getColumnIndex("subject_id"))
            val subject = cursor.getString(cursor.getColumnIndex("subject"))

            val id = cursor.getLong(cursor.getColumnIndex("homework_id"))
            val homework = cursor.getString(cursor.getColumnIndex("homework"))
            val lessonDate = LocalDate.parse(cursor.getString(cursor.getColumnIndex("lesson_date")), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val lessonNum = cursor.getInt(cursor.getColumnIndex("lesson_num"))

            result.add(Homework(
                id,
                Flow(flowId, educationLevel, course, group, subgroup),
                homework,
                lessonDate,
                lessonNum,
                Subject(subjectId, subject)
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
        val count = db.delete("homework", whereClause, whereArgs)
        db.close()
        return count
    }

    fun deleteAllBeforeDate(
        date: LocalDate
    ): Int {
        val whereClause = "lesson_date<?"
        val whereArgs = arrayOf(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

        val db = dbHelper.writableDatabase
        val count = db.delete("homework", whereClause, whereArgs)
        db.close()
        return count
    }
}
