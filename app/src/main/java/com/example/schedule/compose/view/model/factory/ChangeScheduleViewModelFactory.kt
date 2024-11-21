package com.example.schedule.compose.view.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.view.model.ChangeScheduleViewModel

class ChangeScheduleViewModelFactory(
    private val lessonRepo: LessonRepo,
    private val scheduleRepo: ScheduleRepo,
    private val flowLvl: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChangeScheduleViewModel(
            lessonRepo = lessonRepo,
            scheduleRepo = scheduleRepo,
            flowLvl = flowLvl,
            course = course,
            group = group,
            subgroup = subgroup
        ) as T
    }
}
