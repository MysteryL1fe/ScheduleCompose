package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.schedule.compose.entity.Lesson

class LessonRepo {
    private val dbHelper: ScheduleDBHelper

    constructor(context: Context) {
        this.dbHelper = ScheduleDBHelper(context)
    }

    constructor(dbHelper: ScheduleDBHelper) {
        this.dbHelper = dbHelper
    }

    fun add(name: String?, teacher: String?, cabinet: String?): Long {
        if (name == null) return -1

        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.put("name", name)
        values.put("teacher", teacher ?: "")
        values.put("cabinet", cabinet ?: "")

        val id: Long = db.insert("lesson", null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun findById(id: Long): Lesson? {
        var result: Lesson? = null
        val db = dbHelper.readableDatabase

        val sql = "SELECT * FROM lesson WHERE id=?;"
        val selectionArgs = arrayOf(id.toString())
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val teacher = cursor.getString(cursor.getColumnIndex("teacher"))
            val cabinet = cursor.getString(cursor.getColumnIndex("cabinet"))

            result = Lesson(id, name, teacher, cabinet)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findByNameAndTeacherAndCabinet(name: String, teacher: String, cabinet: String): Lesson? {
        var result: Lesson? = null
        val db = dbHelper.readableDatabase

        val sql = "SELECT * FROM lesson" +
                " WHERE name=? AND teacher=? AND cabinet=?;"
        val selectionArgs = arrayOf(name, teacher, cabinet)
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))

            result = Lesson(id, name, teacher, cabinet)
        }
        cursor.close()
        db.close()
        return result
    }
}
