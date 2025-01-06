package com.example.schedule.compose.retrofit

import com.example.schedule.compose.entity.Cabinet
import retrofit2.Call
import retrofit2.http.GET

interface CabinetController {
    @GET("/cabinet/all")
    fun getAll(): Call<List<Cabinet>>
}