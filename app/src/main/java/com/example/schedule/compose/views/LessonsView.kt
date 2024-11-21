/*
package com.example.schedule.compose.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.Utils
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.repo.LessonRepo
import com.example.schedule.compose.repo.HomeworkRepo
import com.example.schedule.compose.repo.ScheduleRepo
import com.example.schedule.compose.repo.TempScheduleRepo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LessonsView(
    context: Context,
    date: LocalDate,
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int,
    lessonRepo: LessonRepo,
    homeworkRepo: HomeworkRepo,
    scheduleRepo: ScheduleRepo,
    tempScheduleRepo: TempScheduleRepo
) {
    Column(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
        val formattedDate = date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp))

        val isNumerator = Utils.isNumerator(date)
        for (i in 1..8) {
            val schedule: Schedule? = scheduleRepo.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
                flowLvl, course, group, subgroup, date.dayOfWeek.value, i, isNumerator
            )
            val lesson: Lesson? = schedule?.let { lessonRepo.findById(it.lesson) }
            val homework: Homework? = homeworkRepo.findByFlowAndLessonDateAndLessonNum(
                flowLvl, course, group, subgroup, date, i
            )
            val tempSchedule = tempScheduleRepo.findByFlowAndLessonDateAndLessonNum(
                flowLvl, course, group, subgroup, date, i
            )
            val tempLesson: Lesson? = tempSchedule?.let { lessonRepo.findById(it.lesson) }
            val willLessonBe = tempSchedule?.willLessonBe ?: true

            if (schedule != null || homework != null || tempSchedule != null) {
                LessonView(
                    lessonNum = i,
                    lesson = lesson,
                    homework = homework,
                    tempLesson = tempLesson,
                    willLessonBe = willLessonBe
                )
                Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp))
            }
        }
    }
}

@Composable
fun LessonView(
    lessonNum: Int,
    lesson: Lesson?,
    homework: Homework?,
    tempLesson: Lesson?,
    willLessonBe: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp).background(MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Text(
                text = "$lessonNum",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
            )

            if (!willLessonBe) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cross),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            } else if (tempLesson != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_temporary),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = Utils.getTimeByLesson(lessonNum),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        if (lesson != null) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = lesson.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${lesson.cabinet}, ${lesson.teacher}", style = MaterialTheme.typography.bodySmall)
            }
        }

        if (homework != null) {
            Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp))
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = "Homework: ${homework.homework}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLessonsView() {
    val context = remember { LocalContext.current }
    LessonsView(
        context = context,
        date = LocalDate.now(),
        flowLvl = 1,
        course = 2,
        group = 3,
        subgroup = 1,
        lessonRepo = LessonRepo(context),
        homeworkRepo = HomeworkRepo(context),
        scheduleRepo = ScheduleRepo(context),
        tempScheduleRepo = TempScheduleRepo(context)
    )
}
*/
