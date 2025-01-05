package com.example.schedule.compose.repo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScheduleDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "schedule"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val flowTableSql = """
            CREATE TABLE IF NOT EXISTS flow (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                flow_lvl INTEGER NOT NULL CHECK (flow_lvl >= 1 AND flow_lvl <= 3),
                course INTEGER NOT NULL CHECK (course > 0 AND course <= 5),
                flow INTEGER NOT NULL CHECK (flow > 0),
                subgroup INTEGER NOT NULL CHECK (subgroup > 0),
                last_edit TEXT NOT NULL,
                lessons_start_date TEXT NOT NULL,
                session_start_date TEXT NOT NULL,
                session_end_date TEXT NOT NULL,
                active INTEGER NOT NULL,
                UNIQUE (flow_lvl, course, flow, subgroup)
            );
        """
        db.execSQL(flowTableSql)

        val lessonTableSql = """
            CREATE TABLE IF NOT EXISTS lesson (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                teacher TEXT,
                cabinet TEXT,
                UNIQUE (name, teacher, cabinet)
            );
        """
        db.execSQL(lessonTableSql)

        val scheduleTableSql = """
            CREATE TABLE IF NOT EXISTS schedule (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                flow INTEGER NOT NULL,
                lesson INTEGER NOT NULL,
                day_of_week INTEGER NOT NULL CHECK (day_of_week >= 1 AND day_of_week <= 7),
                lesson_num INTEGER NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
                numerator INTEGER NOT NULL,
                FOREIGN KEY (flow) REFERENCES flow(id),
                FOREIGN KEY (lesson) REFERENCES lesson(id),
                UNIQUE (flow, day_of_week, lesson_num, numerator)
            );
        """
        db.execSQL(scheduleTableSql)

        val homeworkTableSql = """
            CREATE TABLE IF NOT EXISTS homework (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                homework TEXT NOT NULL,
                lesson_date TEXT NOT NULL,
                lesson_num INTEGER NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
                flow INTEGER NOT NULL,
                lesson_name TEXT NOT NULL,
                FOREIGN KEY (flow) REFERENCES flow(id),
                UNIQUE (lesson_date, lesson_num, flow)
            );
        """
        db.execSQL(homeworkTableSql)

        val tempScheduleTableSql = """
            CREATE TABLE IF NOT EXISTS temp_schedule (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                flow INTEGER NOT NULL,
                lesson INTEGER NOT NULL,
                lesson_date TEXT NOT NULL,
                lesson_num INTEGER NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
                will_lesson_be INTEGER NOT NULL,
                FOREIGN KEY (flow) REFERENCES flow(id),
                FOREIGN KEY (lesson) REFERENCES lesson(id),
                UNIQUE (flow, lesson_date, lesson_num)
            );
        """
        db.execSQL(tempScheduleTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop all existing tables and recreate them (simple approach)
        db.execSQL("DROP TABLE IF EXISTS temp_schedule")
        db.execSQL("DROP TABLE IF EXISTS homework")
        db.execSQL("DROP TABLE IF EXISTS schedule")
        db.execSQL("DROP TABLE IF EXISTS lesson")
        db.execSQL("DROP TABLE IF EXISTS flow")

        // Recreate tables
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop all existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS temp_schedule")
        db.execSQL("DROP TABLE IF EXISTS homework")
        db.execSQL("DROP TABLE IF EXISTS schedule")
        db.execSQL("DROP TABLE IF EXISTS lesson")
        db.execSQL("DROP TABLE IF EXISTS flow")

        // Recreate tables
        onCreate(db)
    }

    fun clearData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM temp_schedule")
        db.execSQL("DELETE FROM homework")
        db.execSQL("DELETE FROM schedule")
        db.execSQL("DELETE FROM lesson")
        db.execSQL("DELETE FROM flow")
    }
}
