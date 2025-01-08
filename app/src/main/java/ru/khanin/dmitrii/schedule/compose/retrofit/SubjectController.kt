package ru.khanin.dmitrii.schedule.compose.retrofit

import ru.khanin.dmitrii.schedule.compose.entity.Subject
import retrofit2.Call
import retrofit2.http.GET

interface SubjectController {
    @GET("/subject/all")
    fun getAll(): Call<List<Subject>>
}