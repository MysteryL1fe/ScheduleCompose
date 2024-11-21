/*
package com.example.schedule.compose.repo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.schedule.ScheduleDBHelper;
import com.example.schedule.entity.Flow;
import com.example.schedule.entity.Homework;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeworkRepo {
    private final ScheduleDBHelper dbHelper;
    private final FlowRepo flowRepo;

    public HomeworkRepo(Context context) {
        this.dbHelper = new ScheduleDBHelper(context);
        this.flowRepo = new FlowRepo(context);
    }

    public HomeworkRepo(ScheduleDBHelper dbHelper, FlowRepo flowRepo) {
        this.dbHelper = dbHelper;
        this.flowRepo = flowRepo;
    }

    public long add(
            long flow, String lessonName, String homework, LocalDate lessonDate, int lessonNum
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flow", flow);
        values.put("lesson_name", lessonName);
        values.put("homework", homework);
        values.put(
                "lesson_date",
                lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        values.put("lesson_num", lessonNum);

        long id = db.insert("homework", null, values);
        db.close();
        return id;
    }

    public long add(
            int flowLvl, int course, int flow, int subgroup,
            String lessonName, String homework, LocalDate lessonDate, int lessonNum
    ) {
        long flowId = -1;
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow != null) flowId = foundFlow.getId();
        else flowId = flowRepo.add(flowLvl, course, flow, subgroup);

        return add(flowId, lessonName, homework, lessonDate, lessonNum);
    }

    public int update(
            long flow, String lessonName, String homework, LocalDate lessonDate, int lessonNum
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("lesson_name", lessonName);
        values.put("homework", homework);
        
        String whereClause = "flow=? AND lesson_date=? AND lesson_num=?";
        String[] whereArgs = new String[] {
                String.valueOf(flow), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(lessonNum)
        };

        int count = db.update("homework", values, whereClause, whereArgs);
        db.close();
        return count;
    }
    
    public int update(
            int flowLvl, int course, int flow, int subgroup,
            String lessonName, String homework, LocalDate lessonDate, int lessonNum
    ) {
        long flowId = -1;
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow != null) flowId = foundFlow.getId();
        else flowId = flowRepo.add(flowLvl, course, flow, subgroup);

        return update(flowId, lessonName, homework, lessonDate, lessonNum);
    }
    
    public void addOrUpdate(
            long flow, String lessonName, String homework, LocalDate lessonDate, int lessonNum
    ) {
        Homework foundHomework = findByFlowAndLessonDateAndLessonNum(
                flow, lessonDate, lessonNum
        );
        if (foundHomework != null) update(flow, lessonName, homework, lessonDate, lessonNum);
        else add(flow, lessonName, homework, lessonDate, lessonNum);
    }
    
    public void addOrUpdate(
            int flowLvl, int course, int flow, int subgroup,
            String lessonName, String homework, LocalDate lessonDate, int lessonNum
    ) {
        long flowId = -1;
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow != null) flowId = foundFlow.getId();
        else flowId = flowRepo.add(flowLvl, course, flow, subgroup);

        addOrUpdate(flowId, lessonName, homework, lessonDate, lessonNum);
    }

    @SuppressLint("Range")
    public Homework findByFlowAndLessonDateAndLessonNum(
            long flow, LocalDate lessonDate, int lessonNum
    ) {
        Homework result = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM homework " +
                "WHERE flow=? AND lesson_date=? AND lesson_num=?;";
        String[] selectionArgs = new String[] {
                String.valueOf(flow), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(lessonNum)
        };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String lessonName = cursor.getString(cursor.getColumnIndex("lesson_name"));
            String homework = cursor.getString(cursor.getColumnIndex("homework"));

            result = new Homework();
            result.setId(id);
            result.setFlow(flow);
            result.setLessonName(lessonName);
            result.setHomework(homework);
            result.setLessonDate(lessonDate);
            result.setLessonNum(lessonNum);
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public Homework findByFlowAndLessonDateAndLessonNum(
            int flowLvl, int course, int flow, int subgroup, LocalDate lessonDate, int lessonNum
    ) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return null;

        long flowId = foundFlow.getId();

        return findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum);
    }

    @SuppressLint("Range")
    public List<Homework> findAllByFlow(long flow) {
        List<Homework> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM homework WHERE flow=?;";
        String[] selectionArgs = new String[] {String.valueOf(flow)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String lessonName = cursor.getString(cursor.getColumnIndex("lesson_name"));
            String homework = cursor.getString(cursor.getColumnIndex("homework"));
            LocalDate lessonDate = LocalDate.parse(
                    cursor.getString(cursor.getColumnIndex("lesson_date")),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
            int lessonNum = cursor.getInt(cursor.getColumnIndex("lesson_num"));

            Homework foundHomework = new Homework();
            foundHomework.setId(id);
            foundHomework.setFlow(flow);
            foundHomework.setLessonName(lessonName);
            foundHomework.setHomework(homework);
            foundHomework.setLessonDate(lessonDate);
            foundHomework.setLessonNum(lessonNum);

            result.add(foundHomework);
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Homework> findAllByFlow(int flowLvl, int course, int flow, int subgroup) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return null;

        long flowId = foundFlow.getId();

        return findAllByFlow(flowId);
    }

    public int deleteByFlowAndLessonDateAndLessonNum(
            long flow, LocalDate lessonDate, int lessonNum
    ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "flow=? AND lesson_date=? AND lesson_num=?";
        String[] whereArgs = new String[] {
                String.valueOf(flow), lessonDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(lessonNum)
        };

        int count = db.delete("homework", whereClause, whereArgs);
        db.close();
        return count;
    }

    public int deleteByFlowAndLessonDateAndLessonNum(
            int flowLvl, int course, int flow, int subgroup, LocalDate lessonDate, int lessonNum
    ) {
        Flow foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(
                flowLvl, course, flow, subgroup
        );
        if (foundFlow == null) return 0;

        long flowId = foundFlow.getId();

        return deleteByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum);
    }

    public int deleteAllBeforeDate(LocalDate date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "lesson_date<?";
        String[] whereArgs = new String[] {
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        };

        int count = db.delete("homework", whereClause, whereArgs);
        db.close();
        return count;
    }
}
*/
