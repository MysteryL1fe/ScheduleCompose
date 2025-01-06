package com.example.schedule.compose.retrofit

import com.example.schedule.compose.entity.Schedule
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleController {
    @GET("/schedule/flow")
    fun getAllByFlow(@Query("education_level") educationLevel: Int, @Query("course") course: Int, @Query("group") group: Int, @Query("subgroup") subgroup: Int): Call<List<Schedule>>

    @GET("/schedule/teacher")
    fun getAllByTeacher(@Query("surname") surname: String, @Query("name") name: String, @Query("patronymic") patronymic: String): Call<List<Schedule>>
}