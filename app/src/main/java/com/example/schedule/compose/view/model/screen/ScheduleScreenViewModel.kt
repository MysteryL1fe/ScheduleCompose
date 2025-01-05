package com.example.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.utils.Utils
import java.time.LocalDate

class ScheduleScreenViewModel(
    private val lessonRepo: LessonRepo,
    private val scheduleRepo: ScheduleRepo,
    private val flowLvl: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModel() {
    var date = mutableStateOf(LocalDate.now())
    val lessons = mutableStateListOf<MutableList<Lesson?>>().apply {
        for (i in 0..13) {
            add(arrayOfNulls<Lesson?>(8).toMutableList())
        }
    }
    var textSize by mutableStateOf(SettingsStorage.textSize)
    var displayModeFull by mutableStateOf(SettingsStorage.displayModeFull)

    fun update() {
        textSize = SettingsStorage.textSize
        displayModeFull = SettingsStorage.displayModeFull
        date.value = LocalDate.now()
        for (i in 0..13) {
            val isNumerator = Utils.isNumerator(date.value.plusDays(i.toLong()))
            for (j in 1..8) {
                lessons[i][j - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                    flowLvl, course, group, subgroup, date.value.plusDays(i.toLong()).dayOfWeek.value, j, isNumerator
                )?.lesson?.let {
                    lessonRepo.findById(it)
                }
            }
        }
    }
}