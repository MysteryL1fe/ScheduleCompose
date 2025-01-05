package com.example.schedule.compose.retrofit/*
package com.example.schedule.compose

import com.example.schedule.compose.dto.FlowResponse
import com.example.schedule.compose.dto.HomeworkResponse
import com.example.schedule.compose.dto.LessonResponse
import com.example.schedule.compose.dto.ScheduleResponse
import com.example.schedule.compose.dto.TempScheduleResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

object RetrofitHelper {
    val backendService: BackendService

    init {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(FlowResponse::class.java, FlowResponseJsonDeserializer())
            .registerTypeAdapter(LessonResponse::class.java, LessonResponseJsonDeserializer())
            .registerTypeAdapter(HomeworkResponse::class.java, HomeworkResponseJsonDeserializer())
            .registerTypeAdapter(ScheduleResponse::class.java, ScheduleResponseJsonDeserializer())
            .registerTypeAdapter(TempScheduleResponse::class.java, TempScheduleResponseJsonDeserializer())
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(SettingsStorage.backendBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        backendService = retrofit.create(BackendService::class.java)
    }
}

class FlowResponseJsonDeserializer : JsonDeserializer<FlowResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): FlowResponse {
        val jsonObject = json!!.asJsonObject
        val flowLvl = jsonObject.getAsJsonPrimitive("flow_lvl")
        val course = jsonObject.getAsJsonPrimitive("course")
        val flow = jsonObject.getAsJsonPrimitive("flow")
        val subgroup = jsonObject.getAsJsonPrimitive("subgroup")
        val lastEdit = jsonObject.getAsJsonPrimitive("last_edit")
        val lessonsStartDate = jsonObject.getAsJsonPrimitive("lessons_start_date")
        val sessionStartDate = jsonObject.getAsJsonPrimitive("session_start_date")
        val sessionEndDate = jsonObject.getAsJsonPrimitive("session_end_date")
        val active = jsonObject.getAsJsonPrimitive("active")

        return FlowResponse(
            flowLvl = flowLvl.asInt,
            course = course.asInt,
            flow = flow.asInt,
            subgroup = subgroup.asInt,
            lastEdit = LocalDateTime.parse(lastEdit.asString),
            lessonsStartDate = LocalDate.parse(lessonsStartDate.asString),
            sessionStartDate = LocalDate.parse(sessionStartDate.asString),
            sessionEndDate = LocalDate.parse(sessionEndDate.asString),
            active = active.asBoolean
        )
    }
}

class LessonResponseJsonDeserializer : JsonDeserializer<LessonResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): LessonResponse {
        val jsonObject = json!!.asJsonObject
        val name = jsonObject.getAsJsonPrimitive("name")
        val teacher = jsonObject.getAsJsonPrimitive("teacher")
        val cabinet = jsonObject.getAsJsonPrimitive("cabinet")

        return LessonResponse(
            name = name.asString,
            teacher = teacher.asString,
            cabinet = cabinet.asString
        )
    }
}

class HomeworkResponseJsonDeserializer : JsonDeserializer<HomeworkResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): HomeworkResponse {
        val jsonObject = json!!.asJsonObject
        val flowResponse = jsonObject.getAsJsonObject("flow")
        val lessonName = jsonObject.getAsJsonPrimitive("lesson_name")
        val homework = jsonObject.getAsJsonPrimitive("homework")
        val lessonDate = jsonObject.getAsJsonPrimitive("lesson_date")
        val lessonNum = jsonObject.getAsJsonPrimitive("lesson_num")

        return HomeworkResponse(
            flow = FlowResponseJsonDeserializer().deserialize(flowResponse, null, context),
            lessonName = lessonName.asString,
            homework = homework.asString,
            lessonDate = LocalDate.parse(lessonDate.asString),
            lessonNum = lessonNum.asInt
        )
    }
}

class ScheduleResponseJsonDeserializer : JsonDeserializer<ScheduleResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): ScheduleResponse {
        val jsonObject = json!!.asJsonObject
        val flowResponse = jsonObject.getAsJsonObject("flow")
        val lessonResponse = jsonObject.getAsJsonObject("lesson")
        val dayOfWeek = jsonObject.getAsJsonPrimitive("day_of_week")
        val lessonNum = jsonObject.getAsJsonPrimitive("lesson_num")
        val numerator = jsonObject.getAsJsonPrimitive("numerator")

        return ScheduleResponse(
            flow = FlowResponseJsonDeserializer().deserialize(flowResponse, null, context),
            lesson = LessonResponseJsonDeserializer().deserialize(lessonResponse, null, context),
            dayOfWeek = dayOfWeek.asInt,
            lessonNum = lessonNum.asInt,
            numerator = numerator.asBoolean
        )
    }
}

class TempScheduleResponseJsonDeserializer : JsonDeserializer<TempScheduleResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): TempScheduleResponse {
        val jsonObject = json!!.asJsonObject
        val flowResponse = jsonObject.getAsJsonObject("flow")
        val lessonResponse = jsonObject.getAsJsonObject("lesson")
        val lessonDate = jsonObject.getAsJsonPrimitive("lesson_date")
        val lessonNum = jsonObject.getAsJsonPrimitive("lesson_num")
        val willLessonBe = jsonObject.getAsJsonPrimitive("will_lesson_be")

        return TempScheduleResponse(
            flow = FlowResponseJsonDeserializer().deserialize(flowResponse, null, context),
            lesson = LessonResponseJsonDeserializer().deserialize(lessonResponse, null, context),
            lessonDate = LocalDate.parse(lessonDate.asString),
            lessonNum = lessonNum.asInt,
            willLessonBe = willLessonBe.asBoolean
        )
    }
}
*/
