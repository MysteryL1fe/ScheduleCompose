package com.example.schedule.compose.retrofit

import com.example.schedule.compose.entity.Teacher
import retrofit2.Call
import retrofit2.http.GET

interface TeacherController {
    @GET("/teacher/all")
    fun getAll(): Call<List<Teacher>>
}