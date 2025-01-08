package ru.khanin.dmitrii.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import ru.khanin.dmitrii.schedule.compose.entity.Subject

class SubjectRepo(
    private val dbHelper: ScheduleDBHelper
) {
    fun add(
        subject: String
    ): Long {
        val values = ContentValues()
        values.put("subject", subject)

        val db = dbHelper.writableDatabase
        val id = db.insert("subject", null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun findBySubject(
        subject: String
    ): Subject? {
        var result: Subject? = null

        val sql = """
            SELECT * FROM subject
            WHERE subject=?
        """.trimIndent()
        val selectionArgs = arrayOf(subject)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))

            result = Subject(id, subject)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAll(): List<Subject> {
        val result = mutableListOf<Subject>()

        val sql = """
            SELECT * FROM subject
            ORDER BY subject
        """.trimIndent()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, arrayOf())
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val subject = cursor.getString(cursor.getColumnIndex("subject"))

            result.add(Subject(id, subject))
        }
        cursor.close()
        db.close()
        return result
    }
}