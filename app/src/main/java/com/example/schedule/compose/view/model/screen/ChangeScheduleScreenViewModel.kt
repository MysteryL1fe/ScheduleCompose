package com.example.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.repo.CabinetRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.repo.SubjectRepo
import com.example.schedule.compose.repo.TeacherRepo
import com.example.schedule.compose.utils.SettingsStorage

class ChangeScheduleScreenViewModel(
    private val subjectRepo: SubjectRepo,
    private val teacherRepo: TeacherRepo,
    private val cabinetRepo: CabinetRepo,
    private val scheduleRepo: ScheduleRepo,
    private val educationLevel: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModel() {
    var dayOfWeek by mutableIntStateOf(1)
    val numerator = mutableStateListOf<Schedule?>().apply {
        addAll(arrayOfNulls(8))
    }
    val denominator = mutableStateListOf<Schedule?>().apply {
        addAll(arrayOfNulls(8))
    }
    var textSize by mutableStateOf(SettingsStorage.textSize)

    var subjects by mutableStateOf(subjectRepo.findAll())
    var teachers by mutableStateOf(teacherRepo.findAll())
    var cabinets by mutableStateOf(cabinetRepo.findAll())

    var showDayPickerDialog by mutableStateOf(false)

    fun update() {
        textSize = SettingsStorage.textSize
        subjects = subjectRepo.findAll()
        teachers = teacherRepo.findAll()
        cabinets = cabinetRepo.findAll()
        for (isNumerator in 0..1) {
            for (lessonNum in 1..8) {
                if (isNumerator == 0) {
                    denominator[lessonNum - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                        educationLevel, course, group, subgroup, dayOfWeek, lessonNum, false
                    )
                } else {
                    numerator[lessonNum - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                        educationLevel, course, group, subgroup, dayOfWeek, lessonNum, true
                    )
                }
            }
        }
    }

    fun changeLesson(
        lessonNum: Int,
        isNumerator: Boolean,
        subject: String,
        surname: String,
        name: String,
        patronymic: String,
        cabinet: String,
        building: String,
    ) {
        if (isNumerator) {
            scheduleRepo.addOrUpdate(educationLevel, course, group, subgroup, dayOfWeek, lessonNum, true, subject, surname, name, patronymic, cabinet, building)
        } else {
            scheduleRepo.addOrUpdate(educationLevel, course, group, subgroup, dayOfWeek, lessonNum, false, subject, surname, name, patronymic, cabinet, building)
        }
        update()
    }

    fun deleteLesson(
        lessonNum: Int,
        isNumerator: Boolean
    ) {
        scheduleRepo.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
            educationLevel, course, group, subgroup, dayOfWeek, lessonNum, isNumerator
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