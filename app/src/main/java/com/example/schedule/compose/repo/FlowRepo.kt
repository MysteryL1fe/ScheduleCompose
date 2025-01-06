package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.schedule.compose.entity.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FlowRepo(
    private val dbHelper: ScheduleDBHelper
) {
    fun add(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        lastEdit: LocalDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0),
        lessonsStartDate: LocalDate? = null,
        sessionStartDate: LocalDate? = null,
        sessionEndDate: LocalDate? = null,
        active: Boolean = true
    ): Long {
        val values = ContentValues()
        values.put("education_level", educationLevel)
        values.put("course", course)
        values.put("_group", group)
        values.put("subgroup", subgroup)
        values.put(
            "last_edit",
            lastEdit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        lessonsStartDate?.let {
            values.put(
                "lessons_start_date",
                it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }
        sessionStartDate?.let {
            values.put(
                "session_start_date",
                it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }
        sessionEndDate?.let {
            values.put(
                "session_end_date",
                it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }
        values.put("active", if (active) "1" else "0")

        val db: SQLiteDatabase = dbHelper.writableDatabase
        val id: Long = db.insert("flow", null, values)
        db.close()
        return id
    }

    fun update(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        active: Boolean
    ): Int {
        val values = ContentValues()
        values.put("active", if (active) "1" else "0")

        val whereClause = "education_level=? AND course=? AND _group=? AND subgroup=?"
        val whereArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString())

        val db: SQLiteDatabase = dbHelper.writableDatabase
        val count: Int = db.update("flow", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun update(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int,
        lastEdit: LocalDateTime,
        lessonsStartDate: LocalDate?,
        sessionStartDate: LocalDate?,
        sessionEndDate: LocalDate?,
        active: Boolean
    ): Int {
        val values = ContentValues()
        values.put(
            "last_edit",
            lastEdit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        values.put(
            "lessons_start_date",
            lessonsStartDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put(
            "session_start_date",
            sessionStartDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put(
            "session_end_date",
            sessionEndDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put("active", if (active) "1" else "0")

        val whereClause = "education_level=? AND course=? AND _group=? AND subgroup=?"
        val whereArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString())

        val db: SQLiteDatabase = dbHelper.writableDatabase
        val count: Int = db.update("flow", values, whereClause, whereArgs)
        db.close()
        return count
    }

    @SuppressLint("Range")
    fun findByEducationLevelAndCourseAndGroupAndSubgroup(
        educationLevel: Int,
        course: Int,
        group: Int,
        subgroup: Int
    ): Flow? {
        var result: Flow? = null

        val sql = """
            SELECT * FROM flow
            WHERE education_level=? AND course=? AND _group=? AND subgroup=?
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString(), subgroup.toString())
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = cursor.getString(cursor.getColumnIndex("lessons_start_date"))?.let {
                LocalDate.parse(
                    it,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            }
            val sessionStartDate = cursor.getString(cursor.getColumnIndex("session_start_date"))?.let {
                LocalDate.parse(
                    it,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            }
            val sessionEndDate = cursor.getString(cursor.getColumnIndex("session_end_date"))?.let {
                LocalDate.parse(
                    it,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            }
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            result = Flow(id, educationLevel, course, group, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findDistinctActiveCourseByEducationLevel(
        educationLevel: Int
    ): List<Int> {
        val result: MutableList<Int> = ArrayList()

        val sql = """
            SELECT DISTINCT course FROM flow
            WHERE education_level=? AND active=1
            ORDER BY course
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString())
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val course = cursor.getInt(cursor.getColumnIndex("course"))
            result.add(course)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findDistinctActiveGroupByEducationLevelAndCourse(
        educationLevel: Int,
        course: Int
    ): List<Int> {
        val result: MutableList<Int> = ArrayList()

        val sql = """
            SELECT DISTINCT _group FROM flow
            WHERE education_level=? AND course=? AND active=1
            ORDER BY _group
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString())
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val group = cursor.getInt(cursor.getColumnIndex("_group"))
            result.add(group)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findDistinctActiveSubgroupByEducationLevelAndCourseAndGroup(
        educationLevel: Int,
        course: Int,
        group: Int
    ): List<Int> {
        val result: MutableList<Int> = ArrayList()

        val sql = """
            SELECT DISTINCT subgroup FROM flow
            WHERE education_level=? AND course=? AND _group=? AND active=1
            ORDER BY subgroup
        """.trimIndent()
        val selectionArgs = arrayOf(educationLevel.toString(), course.toString(), group.toString())
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"))
            result.add(subgroup)
        }
        cursor.close()
        db.close()
        return result
    }
}
