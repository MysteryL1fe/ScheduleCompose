package com.example.schedule.compose.repo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Schedule

class ScheduleRepo {
    private val dbHelper: ScheduleDBHelper
    private val flowRepo: FlowRepo
    private val lessonRepo: LessonRepo

    constructor(context: Context) {
        this.dbHelper = ScheduleDBHelper(context)
        this.flowRepo = FlowRepo(context)
        this.lessonRepo = LessonRepo(context)
    }

    constructor(dbHelper: ScheduleDBHelper, flowRepo: FlowRepo, lessonRepo: LessonRepo) {
        this.dbHelper = dbHelper
        this.flowRepo = flowRepo
        this.lessonRepo = lessonRepo
    }

    fun add(
        flow: Long, lesson: Long, dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.put("flow", flow)
        values.put("lesson", lesson)
        values.put("day_of_week", dayOfWeek)
        values.put("lesson_num", lessonNum)
        values.put("numerator", if (numerator) "1" else "0")

        val id: Long = db.insert("schedule", null, values)
        db.close()
        return id
    }

    fun add(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int,
        name: String, teacher: String, cabinet: String,
        dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Long {
        val foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        val flowId = foundFlow?.id ?: flowRepo.add(flowLvl, course, flow, subgroup)

        val foundLesson = lessonRepo.findByNameAndTeacherAndCabinet(
            name, teacher, cabinet
        )
        val lessonId = foundLesson?.id ?: lessonRepo.add(name, teacher, cabinet)

        return add(flowId, lessonId, dayOfWeek, lessonNum, numerator)
    }

    fun update(
        flow: Long, lesson: Long, dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.put("lesson", lesson)

        val whereClause = "flow=? AND day_of_week=? AND lesson_num=? AND numerator=?"
        val whereArgs = arrayOf(
            flow.toString(), dayOfWeek.toString(), lessonNum.toString(),
            if (numerator) "1" else "0"
        )

        val count: Int = db.update("schedule", values, whereClause, whereArgs)
        db.close()
        return count
    }

    fun update(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int,
        name: String, teacher: String, cabinet: String,
        dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Int {
        val foundFlow: Flow? = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        val flowId = foundFlow?.id ?: flowRepo.add(flowLvl, course, flow, subgroup)

        val foundLesson = lessonRepo.findByNameAndTeacherAndCabinet(
            name, teacher, cabinet
        )
        val lessonId = foundLesson?.id ?: lessonRepo.add(name, teacher, cabinet)

        return update(flowId, lessonId, dayOfWeek, lessonNum, numerator)
    }

    fun addOrUpdate(
        flow: Long, lesson: Long, dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ) {
        val foundSchedule = findByFlowAndDayOfWeekAndLessonNumAndNumerator(
            flow, dayOfWeek, lessonNum, numerator
        )
        if (foundSchedule != null) update(flow, lesson, dayOfWeek, lessonNum, numerator)
        else add(flow, lesson, dayOfWeek, lessonNum, numerator)
    }

    fun addOrUpdate(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int,
        name: String, teacher: String, cabinet: String,
        dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ) {
        val foundFlow: Flow? = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        val flowId = foundFlow?.id ?: flowRepo.add(flowLvl, course, flow, subgroup)

        val foundLesson = lessonRepo.findByNameAndTeacherAndCabinet(
            name, teacher, cabinet
        )
        val lessonId = foundLesson?.id ?: lessonRepo.add(name, teacher, cabinet)

        addOrUpdate(flowId, lessonId, dayOfWeek, lessonNum, numerator)
    }

    @SuppressLint("Range")
    fun findByFlowAndDayOfWeekAndLessonNumAndNumerator(
        flow: Long, dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Schedule? {
        var result: Schedule? = null
        val db = dbHelper.readableDatabase

        val sql = "SELECT * FROM schedule " +
                "WHERE flow=? AND day_of_week=? AND lesson_num=? AND numerator=?;"
        val selectionArgs = arrayOf(
            flow.toString(), dayOfWeek.toString(), lessonNum.toString(),
            if (numerator) "1" else "0"
        )
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val lesson = cursor.getLong(cursor.getColumnIndex("lesson"))

            result = Schedule(id, flow, lesson, dayOfWeek, lessonNum, numerator)
        }
        cursor.close()
        db.close()
        return result
    }

    fun findByFlowAndDayOfWeekAndLessonNumAndNumerator(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int,
        dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Schedule? {
        val foundFlow: Flow? = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        if (foundFlow == null) return null

        return findByFlowAndDayOfWeekAndLessonNumAndNumerator(
            foundFlow.id!!, dayOfWeek, lessonNum, numerator
        )
    }

    @SuppressLint("Range")
    fun findAllByFlow(flow: Long): List<Schedule> {
        val result = mutableListOf<Schedule>()
        val db = dbHelper.readableDatabase

        val sql = "SELECT * FROM schedule WHERE flow=?;"
        val selectionArgs = arrayOf(flow.toString())
        val cursor: Cursor = db.rawQuery(sql, selectionArgs)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val lesson = cursor.getLong(cursor.getColumnIndex("lesson"))
            val dayOfWeek = cursor.getInt(cursor.getColumnIndex("day_of_week"))
            val lessonNum = cursor.getInt(cursor.getColumnIndex("lesson_num"))
            val numerator = cursor.getInt(cursor.getColumnIndex("numerator")) == 1

            val schedule = Schedule(id, flow, lesson, dayOfWeek, lessonNum, numerator)

            result.add(schedule)
        }
        cursor.close()
        db.close()
        return result
    }

    fun findAllByFlow(flowLvl: Int, course: Int, flow: Int, subgroup: Int): List<Schedule>? {
        val foundFlow: Flow? = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        if (foundFlow == null) return null

        return findAllByFlow(foundFlow.id!!)
    }

    fun deleteByFlow(flow: Long): Int {
        val db = dbHelper.writableDatabase

        val whereClause = "flow=?"
        val whereArgs = arrayOf(flow.toString())

        val count: Int = db.delete("schedule", whereClause, whereArgs)
        db.close()
        return count
    }

    fun deleteByFlow(flowLvl: Int, course: Int, flow: Int, subgroup: Int): Int {
        val foundFlow: Flow? = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        if (foundFlow == null) return 0

        return deleteByFlow(foundFlow.id!!)
    }

    fun deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
        flow: Long, dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Int {
        val db = dbHelper.writableDatabase

        val whereClause = "flow=? AND day_of_week=? AND lesson_num=? AND numerator=?"
        val whereArgs = arrayOf(
            flow.toString(), dayOfWeek.toString(), lessonNum.toString(),
            if (numerator) "1" else "0"
        )

        val count: Int = db.delete("schedule", whereClause, whereArgs)
        db.close()
        return count
    }

    fun deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
        flowLvl: Int, course: Int, flow: Int, subgroup: Int,
        dayOfWeek: Int, lessonNum: Int, numerator: Boolean
    ): Int {
        val foundFlow: Flow? = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
            flowLvl, course, flow, subgroup
        )
        if (foundFlow == null) return 0

        return deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
            foundFlow.id!!, dayOfWeek, lessonNum, numerator
        )
    }
}
