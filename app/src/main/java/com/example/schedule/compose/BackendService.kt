/*
package com.example.schedule.compose

import com.example.schedule.compose.dto.FlowResponse
import com.example.schedule.compose.dto.HomeworkResponse
import com.example.schedule.compose.dto.ScheduleResponse
import com.example.schedule.compose.dto.TempScheduleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BackendService {

    @GET("flow/all")
    suspend fun getAllFlows(): Call<List<FlowResponse>>

    @GET("flow/flow")
    suspend fun getFlow(
        @Query("flow_lvl") flowLvl: Int,
        @Query("course") course: Int,
        @Query("flow") flow: Int,
        @Query("subgroup") subgroup: Int
    ): Call<FlowResponse>

    @GET("schedule/flow")
    suspend fun getAllSchedulesByFlow(
        @Query("flow_lvl") flowLvl: Int,
        @Query("course") course: Int,
        @Query("flow") flow: Int,
        @Query("subgroup") subgroup: Int
    ): Call<List<ScheduleResponse>>

    @GET("schedule/teacher")
    suspend fun getAllSchedulesByTeacher(@Query("teacher") teacher: String): Call<List<ScheduleResponse>>

    @GET("homework/flow")
    suspend fun getAllHomeworksByFlow(
        @Query("flow_lvl") flowLvl: Int,
        @Query("course") course: Int,
        @Query("flow") flow: Int,
        @Query("subgroup") subgroup: Int
    ): Call<List<HomeworkResponse>>

    @GET("temp/flow")
    suspend fun getAllTempSchedulesByFlow(
        @Query("flow_lvl") flowLvl: Int,
        @Query("course") course: Int,
        @Query("flow") flow: Int,
        @Query("subgroup") subgroup: Int
    ): Call<List<TempScheduleResponse>>
}
*/
