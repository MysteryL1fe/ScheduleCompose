package ru.khanin.dmitrii.schedule.compose.retrofit

import ru.khanin.dmitrii.schedule.compose.entity.Cabinet
import retrofit2.Call
import retrofit2.http.GET

interface CabinetController {
    @GET("/cabinet/all")
    fun getAll(): Call<List<Cabinet>>
}