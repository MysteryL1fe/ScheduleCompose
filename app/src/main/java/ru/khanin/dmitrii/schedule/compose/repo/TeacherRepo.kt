package ru.khanin.dmitrii.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import ru.khanin.dmitrii.schedule.compose.entity.Teacher

class TeacherRepo(
    private val dbHelper: ScheduleDBHelper
) {
    fun add(
        surname: String,
        name: String?,
        patronymic: String?
    ): Long {
        val values = ContentValues()
        values.put("surname", surname)
        name?.let { values.put("name", name) }
        patronymic?.let { values.put("patronymic", patronymic) }

        val db = dbHelper.writableDatabase
        val id = db.insert("teacher", null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun findBySurnameAndNameAndPatronymic(
        surname: String,
        name: String?,
        patronymic: String?
    ): Teacher? {
        var result: Teacher? = null

        val sql = """
            SELECT * FROM teacher
            WHERE surname=? AND name=? AND patronymic=?
        """.trimIndent()
        val selectionArgs = arrayOf(surname, name, patronymic)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))

            result = Teacher(id, surname, name, patronymic)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAll(): List<Teacher> {
        val result = mutableListOf<Teacher>()

        val sql = """
            SELECT * FROM teacher
            ORDER BY surname, name, patronymic
        """.trimIndent()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, arrayOf())
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val surname = cursor.getString(cursor.getColumnIndex("surname"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val patronymic = cursor.getString(cursor.getColumnIndex("patronymic"))

            result.add(Teacher(id, surname, name, patronymic))
        }
        cursor.close()
        db.close()
        return result
    }
}