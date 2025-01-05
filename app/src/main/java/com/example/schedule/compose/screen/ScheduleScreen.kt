package com.example.schedule.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.ScheduleScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        itemsIndexed(viewModel.lessons) { index, lessons ->
            if (viewModel.displayModeFull || (lessons.size > 0 && lessons.stream().anyMatch {it != null} )) {
                LessonsView(
                    lessons = lessons,
                    startDate = viewModel.date.value,
                    daysOffset = index,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun LessonsView(
    lessons: List<Lesson?>,
    startDate: LocalDate,
    daysOffset: Int,
    viewModel: ScheduleScreenViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val formattedDate = startDate.plusDays(daysOffset.toLong()).format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
        Text(
            text = formattedDate,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        lessons.forEachIndexed { index, lesson ->
            if (viewModel.displayModeFull || lesson != null) {
                LessonView(
                    lesson = lesson,
                    lessonNum = index + 1,
                    viewModel = viewModel
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun LessonView(
    lesson: Lesson?,
    lessonNum: Int,
    viewModel: ScheduleScreenViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(0.dp, 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(0.dp, 64.dp, 64.dp, 0.dp)
                    )
            ) {
                Text(
                    text = lessonNum.toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize,
                    modifier = Modifier.padding(25.dp, 5.dp, 10.dp, 5.dp)
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = Utils.lessonsBeginning[lessonNum - 1] + " - " + Utils.lessonsEnding[lessonNum - 1],
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = viewModel.textSize,
                modifier = Modifier.padding(10.dp, 0.dp)
            )
        }
        if (lesson != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = lesson.name,
                        fontSize = viewModel.textSize,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(20.dp, 0.dp)
                    )

                    if (lesson.cabinet?.isNotEmpty() == true || lesson.teacher?.isNotEmpty() == true) {
                        Text(
                            text = buildString {
                                if (lesson.cabinet?.isNotEmpty() == true) {
                                    append(lesson.cabinet)
                                    if (lesson.teacher?.isNotEmpty() == true) append(", ")
                                }
                                if (lesson.teacher?.isNotEmpty() == true) append(lesson.teacher)
                            },
                            fontSize = viewModel.textSize,
                            modifier = Modifier.padding(20.dp, 0.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}
