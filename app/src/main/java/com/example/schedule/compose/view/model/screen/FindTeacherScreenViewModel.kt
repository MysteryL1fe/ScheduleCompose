package com.example.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.repo.TeacherRepo
import com.example.schedule.compose.retrofit.RetrofitService
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.utils.Utils
import java.time.LocalDate
import java.time.LocalDateTime

class FindTeacherScreenViewModel(
    private val teacherRepo: TeacherRepo
) : ViewModel() {
    var schedules by mutableStateOf(mapOf<LocalDate, List<Schedule>>())
    var teachers by mutableStateOf(teacherRepo.findAll())
    var loading by mutableStateOf(false)
    var textSize by mutableStateOf(SettingsStorage.textSize)
    private val retrofitService = RetrofitService.getInstance()

    fun update() {
        textSize = SettingsStorage.textSize
        teachers = teacherRepo.findAll()
    }

    fun findSchedules(surname: String, name: String, patronymic: String) {
        loading = true
        schedules = mapOf()
        retrofitService.getAllSchedulesByTeacher(surname, name, patronymic, ::onSchedulesFound, ::onSchedulesNotFound)
    }

    private fun onSchedulesFound(schedules: List<Schedule>) {
        loading = false
        val map = mutableMapOf<LocalDate, MutableList<Schedule>>()
        schedules.forEach {
            val date = Utils.getNearestLesson(it.dayOfWeek, it.lessonNum, it.numerator, LocalDateTime.now()).toLocalDate()
            if (map.containsKey(date)) {
                if (map[date]!!.none { schedule -> it.lessonNum == schedule.lessonNum && it.teacher?.surname == schedule.teacher?.surname && it.teacher?.name == schedule.teacher?.name && it.teacher?.patronymic == schedule.teacher?.patronymic }) {
                    map[date]!!.add(it)
                }
            } else {
                map[date] = mutableListOf(it)
            }
        }
        map.keys.forEach { date ->
            map[date]!!.sortBy {
                it.lessonNum
            }
        }
        this.schedules = map
    }

    private fun onSchedulesNotFound() {
        loading = false
    }
}