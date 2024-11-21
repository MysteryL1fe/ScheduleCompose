/*
package com.example.schedule.compose.fragments

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.Utils
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.repo.TempScheduleRepo
import com.example.schedule.compose.views.TempLessonView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TempScheduleFragment(
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int,
    lessonRepo: LessonRepo,
    scheduleRepo: ScheduleRepo,
    tempScheduleRepo: TempScheduleRepo
) {
    var date by remember { mutableStateOf(LocalDate.now()) }
    var tempLessons by remember { mutableStateOf<List<TempLessonView>>(emptyList()) }

    val chooseDayText = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    // Call to update fragment
    LaunchedEffect(date) {
        updateFragment(
            date,
            flowLvl,
            course,
            group,
            subgroup,
            lessonRepo,
            scheduleRepo,
            tempScheduleRepo
        )?.let { tempLessons = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose Date",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 24.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Open DatePickerDialog when the button is clicked
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = chooseDayText, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            tempLessons.forEach { lessonView ->
                lessonView()
            }
        }
    }
}

private fun updateFragment(
    date: LocalDate,
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int,
    lessonRepo: LessonRepo,
    scheduleRepo: ScheduleRepo,
    tempScheduleRepo: TempScheduleRepo
): List<TempLessonView>? {
    val lessons = mutableListOf<TempLessonView>()

    val isNumerator = Utils.isNumerator(date)

    for (i in 1..8) {
        val flow = Flow(flowLvl, course, group, subgroup)
        val schedule = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
            flowLvl, course, group, subgroup, date.dayOfWeek.value, i, isNumerator
        )

        val lesson = schedule?.let { lessonRepo.findById(it.lesson) }

        val tempSchedule = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(
            flowLvl, course, group, subgroup, date, i
        )

        val tempLesson = tempSchedule?.let { lessonRepo.findById(it.lesson) }
        val willLessonBe = tempSchedule == null || tempSchedule.willLessonBe

        lessons.add(TempLessonView(flow, date, i, lesson, tempLesson, willLessonBe))
    }

    return lessons
}

@Composable
fun TempLessonView(
    flow: Flow,
    date: LocalDate,
    lessonNum: Int,
    lesson: Lesson?,
    tempLesson: Lesson?,
    willLessonBe: Boolean
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Lesson $lessonNum", fontSize = 18.sp)
        // Show lesson details
        lesson?.let { Text(text = "Subject: ${it.subject}", fontSize = 16.sp) }
        tempLesson?.let { Text(text = "Temp Lesson: ${it.subject}", fontSize = 16.sp) }
        if (!willLessonBe) {
            Text(text = "This lesson is canceled", fontSize = 16.sp, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TempScheduleFragmentPreview() {
    TempScheduleFragment(
        flowLvl = 1,
        course = 1,
        group = 1,
        subgroup = 1,
        lessonRepo = LessonRepo(), // Provide a mock or real repository
        scheduleRepo = ScheduleRepo(), // Provide a mock or real repository
        tempScheduleRepo = TempScheduleRepo() // Provide a mock or real repository
    )
}
*/
