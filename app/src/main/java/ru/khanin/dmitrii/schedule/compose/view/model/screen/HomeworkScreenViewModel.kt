package ru.khanin.dmitrii.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.khanin.dmitrii.schedule.compose.entity.Homework
import ru.khanin.dmitrii.schedule.compose.entity.Schedule
import ru.khanin.dmitrii.schedule.compose.entity.TempSchedule
import ru.khanin.dmitrii.schedule.compose.repo.HomeworkRepo
import ru.khanin.dmitrii.schedule.compose.repo.ScheduleRepo
import ru.khanin.dmitrii.schedule.compose.repo.SubjectRepo
import ru.khanin.dmitrii.schedule.compose.repo.TempScheduleRepo
import ru.khanin.dmitrii.schedule.compose.utils.SettingsStorage
import ru.khanin.dmitrii.schedule.compose.utils.Utils
import java.time.LocalDate

class HomeworkScreenViewModel(
    private val subjectRepo: SubjectRepo,
    private val scheduleRepo: ScheduleRepo,
    private val tempScheduleRepo: TempScheduleRepo,
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
    val tempSchedules = mutableStateListOf<TempSchedule?>().apply { addAll(arrayOfNulls(8)) }
    val homeworksByDate = mutableStateListOf<Homework?>().apply { addAll(arrayOfNulls(8)) }
    var subjects by mutableStateOf(subjectRepo.findAll())
    var textSize by mutableStateOf(SettingsStorage.textSize)

    var showDatePickerDialog by mutableStateOf(false)

    fun update() {
        textSize = SettingsStorage.textSize
        homeworks.clear()
        homeworks.addAll(homeworkRepo.findAllByFlow(educationLevel, course, group, subgroup))
        subjects = subjectRepo.findAll()
        val dayOfWeek = date.dayOfWeek.value
        val numerator = Utils.isNumerator(date)
        for (i in 1..8) {
            schedules[i - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, dayOfWeek, i, numerator)
            tempSchedules[i - 1] = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, i)
            homeworksByDate[i - 1] = homeworkRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, i)
        }
    }

    fun changeDate(date: LocalDate) {
        this.date = date
        update()
        showDatePickerDialog = false
    }

    fun changeHomework(lessonNum: Int, homework: String, subject: String) {
        homeworkRepo.addOrUpdate(educationLevel, course, group, subgroup, homework, date, lessonNum, subject)
        update()
    }

    fun deleteHomework(lessonNum: Int) {
        homeworkRepo.deleteByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, lessonNum)
        update()
    }
}