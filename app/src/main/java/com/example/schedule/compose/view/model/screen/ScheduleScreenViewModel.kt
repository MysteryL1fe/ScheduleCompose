package com.example.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.repo.HomeworkRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.utils.Utils
import java.time.LocalDate

class ScheduleScreenViewModel(
    private val scheduleRepo: ScheduleRepo,
    private val homeworkRepo: HomeworkRepo,
    private val educationLevel: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModel() {
    var date by mutableStateOf(LocalDate.now())
    val schedules = mutableStateListOf<MutableList<Schedule?>>().apply {
        for (i in 0..13) {
            add(mutableStateListOf(null, null, null, null, null, null, null, null))
        }
    }
    val homeworks = mutableStateListOf<MutableList<Homework?>>().apply {
        for (i in 0..13) {
            add(mutableStateListOf(null, null, null, null, null, null, null, null))
        }
    }
    var textSize by mutableStateOf(SettingsStorage.textSize)
    var displayModeFull by mutableStateOf(SettingsStorage.displayModeFull)

    fun update() {
        textSize = SettingsStorage.textSize
        displayModeFull = SettingsStorage.displayModeFull
        date = LocalDate.now()
        for (i in 0..13) {
            val isNumerator = Utils.isNumerator(date.plusDays(i.toLong()))
            for (j in 1..8) {
                schedules[i][j - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, date.plusDays(i.toLong()).dayOfWeek.value, j, isNumerator)
                homeworks[i][j - 1] = homeworkRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date.plusDays(i.toLong()), j)
            }
        }
    }
}