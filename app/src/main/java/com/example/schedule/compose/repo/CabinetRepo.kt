package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.schedule.compose.entity.Cabinet

class CabinetRepo(
    private val dbHelper: ScheduleDBHelper
) {
    fun add(
        cabinet: String,
        building: String?,
        address: String? = null
    ): Long {
        val values = ContentValues()
        values.put("cabinet", cabinet)
        building?.let { values.put("building", building) }
        address?.let { values.put("address", address) }

        val db = dbHelper.writableDatabase
        val id = db.insert("cabinet", null, values)
        db.close()
        return id
    }

    fun update(
        cabinet: String,
        building: String,
        address: String
    ): Int {
        val values = ContentValues()
        values.put("address", address)

        val whereClause = "cabinet=? AND building=?"
        val whereArgs = arrayOf(cabinet, building)

        val db = dbHelper.writableDatabase
        val count = db.update("cabinet", values, whereClause, whereArgs)
        db.close()
        return count
    }

    @SuppressLint("Range")
    fun findByCabinetAndBuilding(
        cabinet: String,
        building: String?,
    ): Cabinet? {
        var result: Cabinet? = null

        val sql = """
            SELECT * FROM cabinet
            WHERE cabinet=? AND building=?
        """.trimIndent()
        val selectionArgs = arrayOf(cabinet, building)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val address = cursor.getString(cursor.getColumnIndex("address"))

            result = Cabinet(id, cabinet, building, address)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAll(): List<Cabinet> {
        val result = mutableListOf<Cabinet>()

        val sql = """
            SELECT * FROM cabinet
            ORDER BY cabinet, building
        """.trimIndent()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(sql, arrayOf())
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val cabinet = cursor.getString(cursor.getColumnIndex("cabinet"))
            val building = cursor.getString(cursor.getColumnIndex("building"))
            val address = cursor.getString(cursor.getColumnIndex("address"))

            result.add(Cabinet(id, cabinet, building, address))
        }
        cursor.close()
        db.close()
        return result
    }
}