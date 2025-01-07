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
import com.example.schedule.compose.repo.SubjectRepo
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.utils.Utils
import java.time.LocalDate

class HomeworkScreenViewModel(
    private val subjectRepo: SubjectRepo,
    private val scheduleRepo: ScheduleRepo,
    private val homeworkRepo: HomeworkRepo,
    private val educationLevel: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModel() {
    val homeworks = mutableStateListOf<Homework>()
    var changeHomeworkScreen by mutableStateOf(false)
    var date: LocalDate by mutableStateOf(LocalDate.now())
    val schedules = mutableStateListOf<Schedule?>().apply { addAll(arrayOfNulls(8)) }
    val homeworksByDate = mutableStateListOf<Homework?>().apply { addAll(arrayOfNulls(8)) }
    var subjects by mutableStateOf(subjectRepo.findAll())
    var textSize by mutableStateOf(SettingsStorage.textSize)

    var showDatePickerDialog by mutableStateOf(false)

    fun update() {
        textSize = SettingsStorage.textSize
        homeworks.clear()
        homeworks.addAll(homeworkRepo.findAllByFlow(educationLevel, course, group, subgroup))
        subjects = subjectRepo.findAll()
        changeHomeworkScreen = false
    }

    fun openChangeHomework() {
        changeHomeworkScreen = true
        val dayOfWeek = date.dayOfWeek.value
        val numerator = Utils.isNumerator(date)
        for (i in 1..8) {
            schedules[i - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, dayOfWeek, i, numerator)
            homeworksByDate[i - 1] = homeworkRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, i)
        }
    }

    fun closeChangeHomework() {
        changeHomeworkScreen = false
        homeworks.clear()
        homeworks.addAll(homeworkRepo.findAllByFlow(educationLevel, course, group, subgroup))
    }

    fun changeDate(date: LocalDate) {
        this.date = date
        val dayOfWeek = date.dayOfWeek.value
        val numerator = Utils.isNumerator(date)
        for (i in 1..8) {
            schedules[i - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, dayOfWeek, i, numerator)
            homeworksByDate[i - 1] = homeworkRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, i)
        }
        showDatePickerDialog = false
    }

    fun changeHomework(lessonNum: Int, homework: String, subject: String) {
        homeworkRepo.addOrUpdate(educationLevel, course, group, subgroup, homework, date, lessonNum, subject)
        homeworksByDate[lessonNum - 1] = homeworkRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, lessonNum)
    }

    fun deleteHomework(lessonNum: Int) {
        homeworkRepo.deleteByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, lessonNum)
        homeworksByDate[lessonNum - 1] = null
    }
}