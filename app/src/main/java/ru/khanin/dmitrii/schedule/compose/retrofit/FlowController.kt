package ru.khanin.dmitrii.schedule.compose.retrofit

import ru.khanin.dmitrii.schedule.compose.entity.Flow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlowController {
    @GET("/flow/flow")
    fun get(@Query("education_level") educationLevel: Int, @Query("course") course: Int, @Query("group") group: Int, @Query("subgroup") subgroup: Int): Call<Flow>

    @GET("/flow/active")
    fun getAllActive(): Call<List<Flow>>
}