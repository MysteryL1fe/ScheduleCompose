package ru.khanin.dmitrii.schedule.compose.retrofit

import ru.khanin.dmitrii.schedule.compose.entity.Teacher
import retrofit2.Call
import retrofit2.http.GET

interface TeacherController {
    @GET("/teacher/all")
    fun getAll(): Call<List<Teacher>>
}