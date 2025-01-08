package ru.khanin.dmitrii.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.khanin.dmitrii.schedule.compose.entity.Schedule
import ru.khanin.dmitrii.schedule.compose.entity.TempSchedule
import ru.khanin.dmitrii.schedule.compose.repo.CabinetRepo
import ru.khanin.dmitrii.schedule.compose.repo.ScheduleRepo
import ru.khanin.dmitrii.schedule.compose.repo.SubjectRepo
import ru.khanin.dmitrii.schedule.compose.repo.TeacherRepo
import ru.khanin.dmitrii.schedule.compose.repo.TempScheduleRepo
import ru.khanin.dmitrii.schedule.compose.utils.SettingsStorage
import ru.khanin.dmitrii.schedule.compose.utils.Utils
import java.time.LocalDate

class TempScheduleScreenViewModel(
    private val subjectRepo: SubjectRepo,
    private val teacherRepo: TeacherRepo,
    private val cabinetRepo: CabinetRepo,
    private val scheduleRepo: ScheduleRepo,
    private val tempScheduleRepo: TempScheduleRepo,
    private val educationLevel: Int,
    private val course: Int,
    private val group: Int,
    private val subgroup: Int
) : ViewModel() {
    var date: LocalDate by mutableStateOf(LocalDate.now())
    val schedules = mutableStateListOf<Schedule?>().apply { addAll(arrayOfNulls(8)) }
    val tempSchedules = mutableStateListOf<TempSchedule?>().apply { addAll(arrayOfNulls(8)) }
    var subjects by mutableStateOf(subjectRepo.findAll())
    var teachers by mutableStateOf(teacherRepo.findAll())
    var cabinets by mutableStateOf(cabinetRepo.findAll())
    var textSize by mutableStateOf(SettingsStorage.textSize)

    var showDatePickerDialog by mutableStateOf(false)

    fun update() {
        textSize = SettingsStorage.textSize
        subjects = subjectRepo.findAll()
        teachers = teacherRepo.findAll()
        cabinets = cabinetRepo.findAll()
        val dayOfWeek = date.dayOfWeek.value
        val numerator = Utils.isNumerator(date)
        for (i in 1..8) {
            schedules[i - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, dayOfWeek, i, numerator)
            tempSchedules[i - 1] = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, i)
        }
    }

    fun changeDate(date: LocalDate) {
        this.date = date
        update()
        showDatePickerDialog = false
    }

    fun changeTempSchedule(
        lessonNum: Int,
        willLessonBe: Boolean,
        subject: String? = null,
        surname: String? = null,
        name: String? = null,
        patronymic: String? = null,
        cabinet: String? = null,
        building: String? = null
    ) {
        tempScheduleRepo.addOrUpdate(educationLevel, course, group, subgroup, date, lessonNum, willLessonBe, subject, surname, name, patronymic, cabinet, building)
        update()
    }

    fun deleteTempSchedule(lessonNum: Int) {
        tempScheduleRepo.deleteByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date, lessonNum)
        update()
    }
}