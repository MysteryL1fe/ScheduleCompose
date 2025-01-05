package com.example.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.utils.SettingsStorage

class ChangeScheduleScreenViewModel(
    private val lessonRepo: LessonRepo,
    private val scheduleRepo: ScheduleRepo,
    private val flowLvl: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModel() {
    var dayOfWeek by mutableIntStateOf(1)
    val numerator = mutableStateListOf<Lesson?>().apply {
        addAll(arrayOfNulls(8))
    }
    val denominator = mutableStateListOf<Lesson?>().apply {
        addAll(arrayOfNulls(8))
    }
    var textSize by mutableStateOf(SettingsStorage.textSize)

    var showDayPickerDialog by mutableStateOf(false)

    fun update() {
        textSize = SettingsStorage.textSize
        for (isNumerator in 0..1) {
            for (lessonNum in 1..8) {
                if (isNumerator == 0) {
                    denominator[lessonNum - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false
                    )?.lesson?.let {
                        lessonRepo.findById(it)
                    }
                } else {
                    numerator[lessonNum - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                        flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true
                    )?.lesson?.let {
                        lessonRepo.findById(it)
                    }
                }
            }
        }
    }

    fun changeLesson(
        lessonName: String,
        teacher: String,
        cabinet: String,
        lessonNum: Int,
        isNumerator: Boolean
    ) {
        if (isNumerator) {
            scheduleRepo.addOrUpdate(
                flowLvl,
                course,
                group,
                subgroup,
                lessonName,
                teacher,
                cabinet,
                dayOfWeek,
                lessonNum,
                true
            )
            numerator[lessonNum - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                flowLvl, course, group, subgroup, dayOfWeek, lessonNum, true
            )?.lesson?.let {
                lessonRepo.findById(it)
            }
        } else {
            scheduleRepo.addOrUpdate(
                flowLvl,
                course,
                group,
                subgroup,
                lessonName,
                teacher,
                cabinet,
                dayOfWeek,
                lessonNum,
                false
            )
            denominator[lessonNum - 1] =
                scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                    flowLvl, course, group, subgroup, dayOfWeek, lessonNum, false
                )?.lesson?.let {
                    lessonRepo.findById(it)
                }
        }
    }

    fun deleteLesson(
        lessonNum: Int,
        isNumerator: Boolean
    ) {
        scheduleRepo.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
            flowLvl, course, group, subgroup, dayOfWeek, lessonNum, isNumerator
        )

        if (isNumerator) numerator[lessonNum - 1] = null
        else denominator[lessonNum - 1] = null
    }

    fun onDaySelected(selectedDay: Int) {
        dayOfWeek = selectedDay
        showDayPickerDialog = false
        update()
    }
}