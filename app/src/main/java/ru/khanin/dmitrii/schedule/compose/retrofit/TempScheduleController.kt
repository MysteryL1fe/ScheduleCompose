package ru.khanin.dmitrii.schedule.compose.retrofit

import ru.khanin.dmitrii.schedule.compose.entity.TempSchedule
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TempScheduleController {
    @GET("/temp/flow")
    fun getAllByFlow(@Query("education_level") educationLevel: Int, @Query("course") course: Int, @Query("group") group: Int, @Query("subgroup") subgroup: Int): Call<List<TempSchedule>>
}