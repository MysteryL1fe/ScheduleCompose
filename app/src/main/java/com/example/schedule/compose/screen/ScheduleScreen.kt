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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.ScheduleScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel,
    modifier: Modifier = Modifier
) {
    val displayedSchedules = viewModel.schedules.filter { viewModel.displayModeFull || (it.size > 0 && it.stream().anyMatch { schedule -> schedule != null } ) }
    if (displayedSchedules.isEmpty()) {
        Text(
            text = stringResource(R.string.lessons_not_founded),
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize,
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            itemsIndexed(displayedSchedules) { index, schedules ->
                SchedulesView(
                    schedules = schedules,
                    startDate = viewModel.date.value,
                    daysOffset = index,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun SchedulesView(
    schedules: List<Schedule?>,
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
        schedules.forEachIndexed { index, schedule ->
            if (viewModel.displayModeFull || schedule != null) {
                ScheduleView(
                    schedule = schedule,
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
        if (schedule != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Column {
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
        }
    }
}
