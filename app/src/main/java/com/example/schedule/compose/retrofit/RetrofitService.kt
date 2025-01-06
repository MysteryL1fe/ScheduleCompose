package com.example.schedule.compose.retrofit

import android.util.Log
import com.example.schedule.compose.entity.Cabinet
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.Subject
import com.example.schedule.compose.entity.Teacher
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RetrofitService private constructor() {
    companion object {
        private const val CONNECTION_URL = "http://192.168.1.139:8080"
        private const val TAG = "RetrofitClient"

        @Volatile
        private var instance: RetrofitService? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RetrofitService().also { instance = it }
        }
    }

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(CONNECTION_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    private val flowService = retrofit.create(FlowController::class.java)
    private val subjectService = retrofit.create(SubjectController::class.java)
    private val teacherService = retrofit.create(TeacherController::class.java)
    private val cabinetService = retrofit.create(CabinetController::class.java)
    private val scheduleService = retrofit.create(ScheduleController::class.java)

    fun getFlow(educationLevel: Int, course: Int, group: Int, subgroup: Int, onSuccess: (Flow) -> Unit) {
        val call = flowService.get(educationLevel, course, group, subgroup)
        call.enqueue(object : Callback<Flow> {
            override fun onResponse(call: Call<Flow>, response: Response<Flow>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                }
            }

            override fun onFailure(call: Call<Flow>, e: Throwable) {
                Log.e(TAG, "Receive active flows failure")
                Log.e(TAG, e.message.toString())
            }
        })
    }

    fun getAllActiveFlows(onSuccess: (List<Flow>) -> Unit) {
        val call = flowService.getAllActive()
        call.enqueue(object : Callback<List<Flow>> {
            override fun onResponse(call: Call<List<Flow>>, response: Response<List<Flow>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                }
            }

            override fun onFailure(call: Call<List<Flow>>, e: Throwable) {
                Log.e(TAG, "Receive active flows failure")
                Log.e(TAG, e.message.toString())
            }
        })
    }

    fun getAllSubjects(onSuccess: (List<Subject>) -> Unit) {
        val call = subjectService.getAll()
        call.enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                }
            }

            override fun onFailure(p0: Call<List<Subject>>, p1: Throwable) {
                Log.d(TAG, "Receive active flows failure")
            }
        })
    }

    fun getAllTeachers(onSuccess: (List<Teacher>) -> Unit) {
        val call = teacherService.getAll()
        call.enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                }
            }

            override fun onFailure(p0: Call<List<Teacher>>, p1: Throwable) {
                Log.d(TAG, "Receive active flows failure")
            }
        })
    }

    fun getAllCabinets(onSuccess: (List<Cabinet>) -> Unit) {
        val call = cabinetService.getAll()
        call.enqueue(object : Callback<List<Cabinet>> {
            override fun onResponse(call: Call<List<Cabinet>>, response: Response<List<Cabinet>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                }
            }

            override fun onFailure(p0: Call<List<Cabinet>>, p1: Throwable) {
                Log.d(TAG, "Receive active flows failure")
            }
        })
    }

    fun getAllSchedulesByFlow(educationLevel: Int, course: Int, group: Int, subgroup: Int, onSuccess: (List<Schedule>) -> Unit) {
        val call = scheduleService.getAllByFlow(educationLevel, course, group, subgroup)
        call.enqueue(object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                }
            }

            override fun onFailure(p0: Call<List<Schedule>>, p1: Throwable) {
                Log.d(TAG, "Receive active flows failure")
            }
        })
    }
}

class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.asString, formatter)
    }
}

class LocalDateAdapter : JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, formatter)
    }
}

/*class FlowResponseJsonDeserializer : JsonDeserializer<FlowResponse> {
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
}*/
