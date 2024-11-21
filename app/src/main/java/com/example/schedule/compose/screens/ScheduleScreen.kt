package com.example.schedule.compose.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.Utils
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.view.model.ScheduleViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    textSize: TextUnit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        itemsIndexed(viewModel.lessons) { index, lessons ->
            LessonsView(
                lessons = lessons,
                startDate = viewModel.date.value,
                daysOffset = index,
                textSize = textSize
            )
        }
    }
}

@Composable
private fun LessonsView(
    lessons: List<Lesson?>,
    startDate: LocalDate,
    daysOffset: Int,
    textSize: TextUnit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val formattedDate = startDate.plusDays(daysOffset.toLong()).format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
        Text(
            text = formattedDate,
            color = colorResource(R.color.text),
            fontSize = textSize,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.secondary)
        )
        lessons.forEachIndexed { index, lesson ->
            LessonView(
                lesson = lesson,
                lessonNum = index + 1,
                textSize = textSize
            )
            HorizontalDivider(
                thickness = 2.dp,
                color = colorResource(R.color.secondary)
            )
        }
    }
}

@Composable
private fun LessonView(
    lesson: Lesson?,
    lessonNum: Int,
    textSize: TextUnit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(colorResource(R.color.primary))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(0.dp, 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = colorResource(R.color.secondary),
                        shape = RoundedCornerShape(0.dp, 64.dp, 64.dp, 0.dp)
                    )
            ) {
                Text(
                    text = lessonNum.toString(),
                    color = colorResource(R.color.text),
                    fontSize = textSize,
                    modifier = Modifier.padding(25.dp, 5.dp, 10.dp, 5.dp)
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = Utils.lessonsBeginning[lessonNum - 1] + " - " + Utils.lessonsEnding[lessonNum - 1],
                color = colorResource(R.color.text),
                fontSize = textSize,
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
                        fontSize = textSize,
                        color = colorResource(R.color.text),
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
                            fontSize = textSize,
                            modifier = Modifier.padding(20.dp, 0.dp),
                            color = colorResource(R.color.text)
                        )
                    }
                }
            }
        }
    }
}
