package com.example.schedule.compose.view.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.Utils
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.ScheduleRepo
import java.time.LocalDate

class ScheduleViewModel(
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

    fun updateData() {
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