package com.example.schedule.compose.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.TempSchedule
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.HomeworkScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private lateinit var buttonColors: ButtonColors
private lateinit var iconButtonColors: IconButtonColors
private lateinit var textFieldColors: TextFieldColors
@OptIn(ExperimentalMaterial3Api::class)
private lateinit var datePickerColors: DatePickerColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    viewModel: HomeworkScreenViewModel,
    modifier: Modifier = Modifier
) {
    buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
    )
    iconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.tertiary,
    )
    textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.tertiary,
        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
        cursorColor = MaterialTheme.colorScheme.tertiary,
        focusedLabelColor = MaterialTheme.colorScheme.secondary,
        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
        focusedContainerColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
    )
    datePickerColors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.background,
        weekdayContentColor = MaterialTheme.colorScheme.secondary,
        navigationContentColor = MaterialTheme.colorScheme.tertiary,
        yearContentColor = MaterialTheme.colorScheme.tertiary,
        currentYearContentColor = MaterialTheme.colorScheme.tertiary,
        selectedYearContentColor = MaterialTheme.colorScheme.tertiary,
        selectedYearContainerColor = MaterialTheme.colorScheme.secondary,
        dayContentColor = MaterialTheme.colorScheme.tertiary,
        selectedDayContentColor = MaterialTheme.colorScheme.tertiary,
        selectedDayContainerColor = MaterialTheme.colorScheme.secondary,
        todayContentColor = MaterialTheme.colorScheme.tertiary,
        todayDateBorderColor = MaterialTheme.colorScheme.secondary,
        dividerColor = MaterialTheme.colorScheme.secondary
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (viewModel.changeHomeworkScreen) {
            Text(
                text = stringResource(R.string.choose_date),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = viewModel.textSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp)
            )
            TextButton(
                onClick = { viewModel.showDatePickerDialog = true },
                colors = buttonColors,
                contentPadding = PaddingValues(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = viewModel.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    fontSize = viewModel.textSize
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
            ) {
                items(8) { index ->
                    if (index == 0) {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    ScheduleCard(
                        schedule = viewModel.schedules[index],
                        tempSchedule = viewModel.tempSchedules[index],
                        homework = viewModel.homeworksByDate[index],
                        lessonNum = index + 1,
                        viewModel = viewModel
                    )
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            TextButton(
                onClick = { viewModel.changeHomeworkScreen = false },
                colors = buttonColors,
                contentPadding = PaddingValues(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.show_homework),
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    fontSize = viewModel.textSize
                )
            }
        } else {
            TextButton(
                onClick = { viewModel.changeHomeworkScreen = true },
                colors = buttonColors,
                contentPadding = PaddingValues(25.dp),
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.change_homework),
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    fontSize = viewModel.textSize
                )
            }

            if (viewModel.homeworks.isEmpty()) {
                Text(
                    text = stringResource(R.string.homeworks_not_found),
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LazyColumn {
                    itemsIndexed(viewModel.homeworks) { index, homework ->
                        if (index == 0) {
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        HomeworkCard(homework, viewModel)
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }

    if (viewModel.showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = viewModel.date.toEpochDay() * 24 * 60 * 60 * 1000)

        DatePickerDialog(
            onDismissRequest = { viewModel.showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.changeDate(LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / 1000 / 60 / 60 / 24)) },
                    colors = buttonColors,
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.showDatePickerDialog = false },
                    colors = buttonColors,
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = datePickerColors,

        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = stringResource(R.string.choose_date),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 25.dp)
                    )
                },
                headline = {
                    Text(
                        text = LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / 1000 / 60 / 60 / 24).format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )
                },
                showModeToggle = false,
                colors = datePickerColors
            )
        }
    }
}

@Composable
private fun HomeworkCard(
    homework: Homework,
    viewModel: HomeworkScreenViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp, 10.dp)
    ) {
        Text(
            text = "${homework.subject.subject}, ${homework.lessonDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}:",
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize
        )
        Text(
            text = homework.homework,
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = viewModel.textSize
        )
    }
}

@Composable
private fun ScheduleCard(
    schedule: Schedule?,
    tempSchedule: TempSchedule?,
    homework: Homework?,
    lessonNum: Int,
    viewModel: HomeworkScreenViewModel
) {
    var showChangeHomeworkDialog by remember { mutableStateOf(false) }

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            if (tempSchedule?.willLessonBe == true) {
                Column(
                    modifier = Modifier.weight(1f)
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
                    modifier = Modifier.weight(1f)
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
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            if (homework == null) {
                IconButton(
                    onClick = { showChangeHomeworkDialog = true },
                    colors = iconButtonColors,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_homework),
                        contentDescription = "Change lesson"
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
                IconButton(
                    onClick = { showChangeHomeworkDialog = true },
                    colors = iconButtonColors,
                    modifier = Modifier.padding(10.dp, 0.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_homework),
                        contentDescription = "Change lesson"
                    )
                }
                IconButton(
                    onClick = { viewModel.deleteHomework(lessonNum) },
                    colors = iconButtonColors,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_thrash),
                        contentDescription = "Change lesson"
                    )
                }
            }
        }
    }

    if (showChangeHomeworkDialog) {
        ChangeHomeworkDialog(
            homework = homework,
            subject = tempSchedule?.subject?.subject ?: schedule?.subject?.subject,
            onDismissRequest = { showChangeHomeworkDialog = false },
            onDone = { homeworkText, subjectText ->
                viewModel.changeHomework(lessonNum, homeworkText, subjectText)
                showChangeHomeworkDialog = false
            },
            viewModel = viewModel
        )
    }
}

@Composable
private fun ChangeHomeworkDialog(
    homework: Homework?,
    subject: String?,
    onDismissRequest: () -> Unit,
    onDone: (homework: String, subject: String) -> Unit,
    viewModel: HomeworkScreenViewModel
) {
    val scrollState = rememberScrollState()

    var homeworkText by remember { mutableStateOf(homework?.homework ?: "") }
    var subjectText by remember { mutableStateOf(homework?.subject?.subject ?: subject ?: "") }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                TextField(
                    value = homeworkText,
                    onValueChange = { homeworkText = it.trimStart() },
                    label = {
                        Text(
                            text = stringResource(R.string.homework),
                            fontSize = viewModel.textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = viewModel.textSize),
                    modifier = Modifier.fillMaxWidth(),
                )
                DropdownTextField(
                    value = subjectText,
                    onValueChange = { subjectText = it.trimStart() },
                    label = stringResource(R.string.subject),
                    suggestions = viewModel.subjects.map { it.subject },
                    viewModel = viewModel
                )
                Row {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = viewModel.textSize,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            if (homeworkText.isNotBlank() && subjectText.isNotBlank()) {
                                onDone(homeworkText.trim(), subjectText.trim())
                            }
                        },
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = viewModel.textSize,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    viewModel: HomeworkScreenViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    fontSize = viewModel.textSize
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
            colors = textFieldColors,
            textStyle = TextStyle(fontSize = viewModel.textSize),
            modifier = Modifier.menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            suggestions.forEach { suggestion ->
                if (suggestion.startsWith(value, ignoreCase = true)) {
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            onValueChange(suggestion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
