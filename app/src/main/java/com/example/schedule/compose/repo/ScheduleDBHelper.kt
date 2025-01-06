package com.example.schedule.compose.repo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScheduleDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "schedule"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val flowTableSql = """
            CREATE TABLE IF NOT EXISTS flow (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                education_level INTEGER NOT NULL CHECK (education_level >= 1 AND education_level <= 3),
                course INTEGER NOT NULL CHECK (course > 0 AND course <= 5),
                _group INTEGER NOT NULL CHECK (_group > 0),
                subgroup INTEGER NOT NULL CHECK (subgroup > 0),
                last_edit TEXT NOT NULL,
                lessons_start_date TEXT,
                session_start_date TEXT,
                session_end_date TEXT,
                active INTEGER NOT NULL,
                UNIQUE (education_level, course, _group, subgroup)
            )
        """.trimIndent()
        db.execSQL(flowTableSql)

        val subjectTableSql = """
            CREATE TABLE IF NOT EXISTS subject (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                subject TEXT NOT NULL,
                UNIQUE (subject)
            )
        """.trimIndent()
        db.execSQL(subjectTableSql)

        val teacherTableSql = """
            CREATE TABLE IF NOT EXISTS teacher (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                surname TEXT NOT NULL,
                name TEXT,
                patronymic TEXT,
                UNIQUE (surname, name, patronymic)
            )
        """.trimIndent()
        db.execSQL(teacherTableSql)

        val cabinetTableSql = """
            CREATE TABLE IF NOT EXISTS cabinet (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cabinet TEXT NOT NULL,
                building TEXT,
                address TEXT,
                UNIQUE (cabinet, building)
            )
        """.trimIndent()
        db.execSQL(cabinetTableSql)

        val scheduleTableSql = """
            CREATE TABLE IF NOT EXISTS schedule (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                flow INTEGER NOT NULL,
                day_of_week INTEGER NOT NULL CHECK (day_of_week >= 1 AND day_of_week <= 7),
                lesson_num INTEGER NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
                numerator INTEGER NOT NULL,
                subject INTEGER NOT NULL,
                teacher INTEGER,
                cabinet INTEGER,
                FOREIGN KEY (flow) REFERENCES flow(id),
                FOREIGN KEY (subject) REFERENCES subject(id),
                FOREIGN KEY (teacher) REFERENCES teacher(id),
                FOREIGN KEY (cabinet) REFERENCES cabinet(id),
                UNIQUE (flow, day_of_week, lesson_num, numerator)
            )
        """.trimIndent()
        db.execSQL(scheduleTableSql)

        val homeworkTableSql = """
            CREATE TABLE IF NOT EXISTS homework (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                homework TEXT NOT NULL,
                lesson_date TEXT NOT NULL,
                lesson_num INTEGER NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
                flow INTEGER NOT NULL,
                subject INTEGER NOT NULL,
                FOREIGN KEY (flow) REFERENCES flow(id),
                FOREIGN KEY (subject) REFERENCES subject(id),
                UNIQUE (lesson_date, lesson_num, flow)
            )
        """.trimIndent()
        db.execSQL(homeworkTableSql)

        val tempScheduleTableSql = """
            CREATE TABLE IF NOT EXISTS temp_schedule (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                flow INTEGER NOT NULL,
                lesson_date TEXT NOT NULL,
                lesson_num INTEGER NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
                will_lesson_be INTEGER NOT NULL,
                subject INTEGER,
                teacher INTEGER,
                cabinet INTEGER NOT NULL,
                FOREIGN KEY (flow) REFERENCES flow(id),
                FOREIGN KEY (subject) REFERENCES subject(id),
                FOREIGN KEY (teacher) REFERENCES teacher(id),
                FOREIGN KEY (cabinet) REFERENCES cabinet(id),
                CHECK (will_lesson_be = 0 OR subject IS NOT NULL),
                UNIQUE (flow, lesson_date, lesson_num)
            )
        """.trimIndent()
        db.execSQL(tempScheduleTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS temp_schedule")
        db.execSQL("DROP TABLE IF EXISTS homework")
        db.execSQL("DROP TABLE IF EXISTS schedule")
        db.execSQL("DROP TABLE IF EXISTS lesson")
        db.execSQL("DROP TABLE IF EXISTS subject")
        db.execSQL("DROP TABLE IF EXISTS teacher")
        db.execSQL("DROP TABLE IF EXISTS cabinet")
        db.execSQL("DROP TABLE IF EXISTS flow")

        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS temp_schedule")
        db.execSQL("DROP TABLE IF EXISTS homework")
        db.execSQL("DROP TABLE IF EXISTS schedule")
        db.execSQL("DROP TABLE IF EXISTS lesson")
        db.execSQL("DROP TABLE IF EXISTS subject")
        db.execSQL("DROP TABLE IF EXISTS teacher")
        db.execSQL("DROP TABLE IF EXISTS cabinet")
        db.execSQL("DROP TABLE IF EXISTS flow")

        onCreate(db)
    }

    fun clearData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM temp_schedule")
        db.execSQL("DELETE FROM homework")
        db.execSQL("DELETE FROM schedule")
        db.execSQL("DELETE FROM subject")
        db.execSQL("DELETE FROM teacher")
        db.execSQL("DELETE FROM cabinet")
        db.execSQL("DELETE FROM flow")
    }
}
