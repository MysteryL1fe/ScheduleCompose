package com.example.schedule.compose.view.model.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.TempSchedule
import com.example.schedule.compose.repo.HomeworkRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.repo.TempScheduleRepo
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScheduleScreenViewModel(
    private val scheduleRepo: ScheduleRepo,
    private val tempScheduleRepo: TempScheduleRepo,
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
    val tempSchedules = mutableStateListOf<MutableList<TempSchedule?>>().apply {
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

    var timeLeft by mutableLongStateOf(0L)
    var timerDayOffset by mutableIntStateOf(0)
    var timerLessonNum by mutableIntStateOf(0)
    var timerBeforeLesson by mutableStateOf(true)
    var timerCoroutineScope: CoroutineScope? = null
    private var timerJob: Job? = null

    fun update() {
        textSize = SettingsStorage.textSize
        displayModeFull = SettingsStorage.displayModeFull
        date = LocalDate.now()
        for (i in 0..13) {
            val isNumerator = Utils.isNumerator(date.plusDays(i.toLong()))
            for (j in 1..8) {
                schedules[i][j - 1] = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(educationLevel, course, group, subgroup, date.plusDays(i.toLong()).dayOfWeek.value, j, isNumerator)
                tempSchedules[i][j - 1] = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date.plusDays(i.toLong()), j)
                homeworks[i][j - 1] = homeworkRepo.findByFlowAndLessonDateAndLessonNum(educationLevel, course, group, subgroup, date.plusDays(i.toLong()), j)
            }
        }
        updateTimer()
    }

    fun updateTimer() {
        stopTimer()
        val now = LocalDateTime.now()
        for (i in 0..13) {
            for (j in 0..7) {
                val beforeLesson = LocalDateTime.of(now.plusDays(i.toLong()).toLocalDate(), LocalTime.parse(Utils.lessonsBeginning[j], DateTimeFormatter.ofPattern("H[H]:mm")))
                val whileLesson = LocalDateTime.of(now.plusDays(i.toLong()).toLocalDate(), LocalTime.parse(Utils.lessonsEnding[j], DateTimeFormatter.ofPattern("H[H]:mm")).withSecond(0))
                if ((schedules[i][j] != null || tempSchedules[i][j] != null) && now.isBefore(whileLesson)) {
                    timerDayOffset = i
                    timerLessonNum = j + 1
                    timerBeforeLesson = now.isBefore(beforeLesson)
                    timeLeft = ChronoUnit.SECONDS.between(LocalDateTime.now(), if (timerBeforeLesson) beforeLesson else whileLesson)
                    startTimer()
                    return
                }
            }
        }
        timerDayOffset = -1
    }

    private fun startTimer() {
        timerJob = timerCoroutineScope?.launch {
            while (timeLeft >= 0) {
                delay(1000)
                timeLeft--
            }
            updateTimer()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }
}