package com.example.schedule.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.TempSchedule
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.ScheduleScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel,
    modifier: Modifier = Modifier
) {
    var isEmpty by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(13) { index ->
            val schedules = viewModel.schedules[index]
            val homeworks = viewModel.homeworks[index]
            val tempSchedules = viewModel.tempSchedules[index]
            if (viewModel.displayModeFull || schedules.any { it != null } || homeworks.any { it != null } || tempSchedules.any { it != null }) {
                isEmpty = false
                SchedulesView(
                    schedules = schedules,
                    tempSchedules = tempSchedules,
                    homeworks = homeworks,
                    startDate = viewModel.date,
                    daysOffset = index,
                    viewModel = viewModel
                )
            }
        }
    }

    if (isEmpty) {
        Text(
            text = stringResource(R.string.lessons_not_founded),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            modifier = modifier
        )
    }
}

@Composable
private fun SchedulesView(
    schedules: List<Schedule?>,
    tempSchedules: List<TempSchedule?>,
    homeworks: List<Homework?>,
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
            modifier = Modifier.padding(10.dp)
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        (0..7).forEach { index ->
            val schedule = schedules[index]
            val homework = homeworks[index]
            val tempSchedule = tempSchedules[index]
            if (viewModel.displayModeFull || schedule != null || homework != null || tempSchedule != null) {
                ScheduleView(
                    schedule = schedule,
                    homework = homework,
                    tempSchedule = tempSchedule,
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
private fun ScheduleView(
    schedule: Schedule?,
    tempSchedule: TempSchedule?,
    homework: Homework?,
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

            if (tempSchedule?.willLessonBe == true) {
                Icon(
                    painter = painterResource(R.drawable.ic_temporary),
                    contentDescription = stringResource(R.string.lesson_wont_be),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                        .padding(5.dp)
                )
            } else if (tempSchedule?.willLessonBe == false) {
                Icon(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = stringResource(R.string.lesson_wont_be),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                        .padding(5.dp)
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

        if (tempSchedule?.willLessonBe == true) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = tempSchedule.subject!!.subject,
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(20.dp, 0.dp)
                )

                if (tempSchedule.teacher?.surname?.isNotEmpty() == true) {
                    Text(
                        text = buildString {
                            append(tempSchedule.teacher!!.surname)
                            if (tempSchedule.teacher!!.name?.isNotEmpty() == true) {
                                append(" ${tempSchedule.teacher!!.name}")
                            }
                            if (tempSchedule.teacher!!.patronymic?.isNotEmpty() == true) {
                                append(" ${tempSchedule.teacher!!.patronymic}")
                            }
                        },
                        fontSize = viewModel.textSize,
                        modifier = Modifier.padding(20.dp, 0.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                if (tempSchedule.cabinet?.cabinet?.isNotEmpty() == true) {
                    Text(
                        text = buildString {
                            append(tempSchedule.cabinet!!.cabinet)
                            if (tempSchedule.cabinet?.building?.isNotEmpty() == true) {
                                append(", корпус ${tempSchedule.cabinet!!.building}")
                            }
                            if (tempSchedule.cabinet!!.address?.isNotEmpty() == true) {
                                append(", ${tempSchedule.cabinet!!.address}")
                            }
                        },
                        fontSize = viewModel.textSize,
                        modifier = Modifier.padding(20.dp, 0.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        } else if (schedule != null) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = schedule.subject.subject,
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(20.dp, 0.dp)
                )

                if (schedule.teacher?.surname?.isNotEmpty() == true) {
                    Text(
                        text = buildString {
                            append(schedule.teacher!!.surname)
                            if (schedule.teacher!!.name?.isNotEmpty() == true) {
                                append(" ${schedule.teacher!!.name}")
                            }
                            if (schedule.teacher!!.patronymic?.isNotEmpty() == true) {
                                append(" ${schedule.teacher!!.patronymic}")
                            }
                        },
                        fontSize = viewModel.textSize,
                        modifier = Modifier.padding(20.dp, 0.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                if (schedule.cabinet?.cabinet?.isNotEmpty() == true) {
                    Text(
                        text = buildString {
                            append(schedule.cabinet!!.cabinet)
                            if (schedule.cabinet?.building?.isNotEmpty() == true) {
                                append(", корпус ${schedule.cabinet!!.building}")
                            }
                            if (schedule.cabinet!!.address?.isNotEmpty() == true) {
                                append(", ${schedule.cabinet!!.address}")
                            }
                        },
                        fontSize = viewModel.textSize,
                        modifier = Modifier.padding(20.dp, 0.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

        if (homework != null) {
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "ДЗ, ${homework.subject.subject}:\n${homework.homework}",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
