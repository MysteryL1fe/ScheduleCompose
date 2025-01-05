package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.schedule.compose.entity.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FlowRepo {
    private val dbHelper: ScheduleDBHelper

    constructor(context: Context?) {
        this.dbHelper = context?.let { ScheduleDBHelper(it) }!!
    }

    constructor(dbHelper: ScheduleDBHelper) {
        this.dbHelper = dbHelper
    }

    fun add(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int, lessonsStartDate: LocalDate,
        sessionStartDate: LocalDate, sessionEndDate: LocalDate, active: Boolean
    ): Long {
        return add(
            flowLvl, course, flow, subgroup,
            LocalDateTime.of(1970, 1, 1, 0, 0, 0),
            lessonsStartDate, sessionStartDate, sessionEndDate, active
        )
    }

    fun add(
        flowLvl: Int,
        course: Int,
        flow: Int,
        subgroup: Int,
        lastEdit: LocalDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0),
        lessonsStartDate: LocalDate = LocalDate.of(1970, 1, 1),
        sessionStartDate: LocalDate = LocalDate.of(2970, 1, 1),
        sessionEndDate: LocalDate = LocalDate.of(2970, 1, 1),
        active: Boolean = true
    ): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val values = ContentValues()
        values.put("flow_lvl", flowLvl)
        values.put("course", course)
        values.put("flow", flow)
        values.put("subgroup", subgroup)
        values.put(
            "last_edit",
            lastEdit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        values.put(
            "lessons_start_date",
            lessonsStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put(
            "session_start_date",
            sessionStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put(
            "session_end_date",
            sessionEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put("active", if (active) "1" else "0")

        val id: Long = db.insert("flow", null, values)
        db.close()
        return id
    }

    fun update(flowLvl: Int, course: Int, flow: Int, subgroup: Int, active: Boolean): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val values = ContentValues()
        values.put("active", if (active) "1" else "0")

        val whereClause = "flow_lvl=? AND course=? AND flow=? AND subgroup=?"
        val whereArgs = arrayOf(
            flowLvl.toString(), course.toString(), flow.toString(),
            subgroup.toString()
        )

        val count: Int = db.update("flow", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun update(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int, lessonsStartDate: LocalDate,
        sessionStartDate: LocalDate, sessionEndDate: LocalDate, active: Boolean
    ): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val values = ContentValues()
        values.put(
            "lessons_start_date",
            lessonsStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put(
            "session_start_date",
            sessionStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put(
            "session_end_date",
            sessionEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
        values.put("active", if (active) "1" else "0")

        val whereClause = "flow_lvl=? AND course=? AND flow=? AND subgroup=?"
        val whereArgs = arrayOf(
            flowLvl.toString(), course.toString(), flow.toString(),
            subgroup.toString()
        )

        val count: Int = db.update("flow", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun update(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int, lastEdit: LocalDateTime
    ): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val values = ContentValues()
        values.put(
            "last_edit",
            lastEdit.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )

        val whereClause = "flow_lvl=? AND course=? AND flow=? AND subgroup=?"
        val whereArgs = arrayOf(
            flowLvl.toString(), course.toString(), flow.toString(),
            subgroup.toString()
        )

        val count: Int = db.update("flow", values, whereClause, whereArgs)
        db.close()
        return count
    }

    @SuppressLint("Range")
    fun findById(id: Long): Flow? {
        var result: Flow? = null
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT * FROM flow WHERE id=?;"
        val selectionArgs = arrayOf(id.toString())
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val flowLvl = cursor.getInt(cursor.getColumnIndex("flow_lvl"))
            val course = cursor.getInt(cursor.getColumnIndex("course"))
            val flow = cursor.getInt(cursor.getColumnIndex("flow"))
            val subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionEndDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_end_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            result = Flow(id, flowLvl, course, flow, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findByFlowLvlAndCourseAndFlowAndSubgroup(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int
    ): Flow? {
        var result: Flow? = null
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT * FROM flow " +
                "WHERE flow_lvl=? AND course=? AND flow=? AND subgroup=?;"
        val selectionArgs = arrayOf(
            flowLvl.toString(), course.toString(), flow.toString(),
            subgroup.toString()
        )
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionEndDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_end_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            result = Flow(id, flowLvl, course, flow, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAllActive(): List<Flow> {
        val result: MutableList<Flow> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT * FROM flow where active=1;"
        val selectionArgs = arrayOf<String>()
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val flowLvl = cursor.getInt(cursor.getColumnIndex("flow_lvl"))
            val course = cursor.getInt(cursor.getColumnIndex("course"))
            val flow = cursor.getInt(cursor.getColumnIndex("flow"))
            val subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionEndDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_end_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            val foundFlow = Flow(id, flowLvl, course, flow, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)

            result.add(foundFlow)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAllByFlowLvl(flowLvl: Int): List<Flow> {
        val result: MutableList<Flow> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT * FROM flow WHERE flow_lvl=?;"
        val selectionArgs = arrayOf(flowLvl.toString())
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val course = cursor.getInt(cursor.getColumnIndex("course"))
            val flow = cursor.getInt(cursor.getColumnIndex("flow"))
            val subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionEndDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_end_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            val foundFlow = Flow(id, flowLvl, course, flow, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)

            result.add(foundFlow)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAllByFlowLvlAndCourse(flowLvl: Int, course: Int): List<Flow> {
        val result: MutableList<Flow> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT * FROM flow WHERE flow_lvl=? AND course=?;"
        val selectionArgs = arrayOf(flowLvl.toString(), course.toString())
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val flow = cursor.getInt(cursor.getColumnIndex("flow"))
            val subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionEndDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_end_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            val foundFlow = Flow(id, flowLvl, course, flow, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)

            result.add(foundFlow)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findAllByFlowLvlAndCourseAndFlow(flowLvl: Int, course: Int, flow: Int): List<Flow> {
        val result: MutableList<Flow> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT * FROM flow WHERE flow_lvl=? AND course=? AND flow=?;"
        val selectionArgs = arrayOf(
            flowLvl.toString(), course.toString(), flow.toString()
        )
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val subgroup = cursor.getInt(cursor.getColumnIndex("subgroup"))
            val lastEdit = LocalDateTime.parse(
                cursor.getString(cursor.getColumnIndex("last_edit")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            val lessonsStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("lessons_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionStartDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_start_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val sessionEndDate = LocalDate.parse(
                cursor.getString(cursor.getColumnIndex("session_end_date")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            )
            val active = cursor.getInt(cursor.getColumnIndex("active")) == 1

            val foundFlow = Flow(id, flowLvl, course, flow, subgroup, lastEdit, lessonsStartDate, sessionStartDate, sessionEndDate, active)

            result.add(foundFlow)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findDistinctActiveCourseByFlowLvl(flowLvl: Int): List<Int> {
        val result: MutableList<Int> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT DISTINCT course FROM flow WHERE flow_lvl=? AND active=1;"
        val selectionArgs = arrayOf(flowLvl.toString())
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
    fun findDistinctActiveFlowByFlowLvlAndCourse(flowLvl: Int, course: Int): List<Int> {
        val result: MutableList<Int> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT DISTINCT flow FROM flow WHERE flow_lvl=? AND course=? AND active=1;"
        val selectionArgs = arrayOf(flowLvl.toString(), course.toString())
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val flow = cursor.getInt(cursor.getColumnIndex("flow"))
            result.add(flow)
        }
        cursor.close()
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun findDistinctActiveSubgroupByFlowLvlAndCourseAndFlow(
        flowLvl: Int, course: Int, flow: Int
    ): List<Int> {
        val result: MutableList<Int> = ArrayList()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val sql = "SELECT DISTINCT subgroup " +
                "FROM flow WHERE flow_lvl=? AND course=? AND flow=? AND active=1;"
        val selectionArgs = arrayOf(
            flowLvl.toString(), course.toString(), flow.toString()
        )
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
