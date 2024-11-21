/*
package com.example.schedule.compose.fragments

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.*
import com.example.schedule.compose.entity.*
import com.example.schedule.compose.repo.*
import com.example.schedule.compose.views.HomeworkLessonView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NewHomeworkScreen(
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int
) {
    var date by remember { mutableStateOf(LocalDate.now()) }
    var homeworkLessons by remember { mutableStateOf<List<HomeworkLessonView>>(emptyList()) }
    var textSize by remember { mutableStateOf(SettingsStorage.textSize) }
    var isLoading by remember { mutableStateOf(true) }

    val dateFormatted = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    val chooseDayBtnTextSize = when (textSize) {
        0 -> 10.sp
        2 -> 30.sp
        else -> 20.sp
    }

    val chooseDayTVTextSize = when (textSize) {
        0 -> 12.sp
        2 -> 36.sp
        else -> 24.sp
    }

    val homeworkLessonContainerPadding = 20.dp

    // Load data for the specific date
    LaunchedEffect(date) {
        isLoading = true
        val dbHelper = ScheduleDBHelper()
        val flowRepo = FlowRepo(dbHelper)
        val lessonRepo = LessonRepo(dbHelper)
        val homeworkRepo = HomeworkRepo(dbHelper, flowRepo)
        val scheduleRepo = ScheduleRepo(dbHelper, flowRepo, lessonRepo)
        val tempScheduleRepo = TempScheduleRepo(dbHelper, flowRepo, lessonRepo)

        val tempHomeworkLessons = mutableListOf<HomeworkLessonView>()
        val isNumerator = Utils.isNumerator(date)

        for (i in 1..8) {
            val flow = Flow().apply {
                setFlowLvl(flowLvl)
                setCourse(course)
                setFlow(group)
                setSubgroup(subgroup)
            }

            val schedule = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                flowLvl, course, group, subgroup, date.dayOfWeek.value, i, isNumerator
            )
            val lesson = schedule?.let { lessonRepo.findById(schedule.lesson) }
            val homework = homeworkRepo.findByFlowAndLessonDateAndLessonNum(
                flowLvl, course, group, subgroup, date, i
            )

            val tempSchedule = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(
                flowLvl, course, group, subgroup, date, i
            )
            val tempLesson = tempSchedule?.let { lessonRepo.findById(it.lesson) }
            val willLessonBe = tempSchedule?.willLessonBe ?: true

            val homeworkLessonView = HomeworkLessonView(
                flow, date, i, lesson, homework, tempLesson, willLessonBe
            )
            tempHomeworkLessons.add(homeworkLessonView)
        }

        homeworkLessons = tempHomeworkLessons
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Choose Day Section
        Text(
            text = "Choose Date",
            style = TextStyle(fontSize = chooseDayTVTextSize),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                // Show DatePicker Dialog
                DatePickerDialog(
                    LocalContext.current,
                    { _, year, month, dayOfMonth ->
                        date = LocalDate.of(year, month + 1, dayOfMonth)
                    },
                    date.year,
                    date.monthValue - 1,
                    date.dayOfMonth
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp)
        ) {
            Text(
                text = dateFormatted,
                fontSize = chooseDayBtnTextSize
            )
        }

        // Loading or Homework Lesson Views
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (homeworkLessons.isEmpty()) {
            Text(
                text = "No Homework Found",
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth().padding(homeworkLessonContainerPadding),
                style = TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                color = Color.Gray
            )
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                homeworkLessons.forEach { lessonView ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(homeworkLessonContainerPadding)
                    ) {
                        lessonView // Custom View for Homework Lesson
                    }
                }
            }
        }
    }
}
*/
