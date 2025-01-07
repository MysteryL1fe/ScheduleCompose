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
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.entity.TempSchedule
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.TempScheduleScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private lateinit var buttonColors: ButtonColors
private lateinit var editButtonColors: ButtonColors
private lateinit var textFieldColors: TextFieldColors
@OptIn(ExperimentalMaterial3Api::class)
private lateinit var datePickerColors: DatePickerColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempScheduleScreen(
    viewModel: TempScheduleScreenViewModel,
    modifier: Modifier = Modifier
) {
    buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
    )
    editButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.tertiary
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

    if (viewModel.showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = viewModel.date.toEpochDay() * 24 * 60 * 60 * 1000)

        DatePickerDialog(
            onDismissRequest = { viewModel.showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.changeDate(LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / 1000 / 60 / 60 / 24)) },
                    colors = buttonColors
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        textAlign = TextAlign.Center
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.showDatePickerDialog = false },
                    colors = buttonColors
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        textAlign = TextAlign.Center
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = datePickerColors
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
                        text = LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / 1000 / 60 / 60 / 24).format(
                            DateTimeFormatter.ofPattern("dd MMM yyyy")),
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
private fun ScheduleCard(
    schedule: Schedule?,
    tempSchedule: TempSchedule?,
    lessonNum: Int,
    viewModel: TempScheduleScreenViewModel
) {
    var showChangeTempScheduleDialog by remember { mutableStateOf(false) }

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
                    modifier = Modifier.fillMaxWidth()
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
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (tempSchedule != null) {
                TextButton(
                    onClick = { viewModel.deleteTempSchedule(lessonNum) },
                    colors = editButtonColors,
                    modifier = Modifier.weight(1f)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (schedule != null) {
                TextButton(
                    onClick = { viewModel.changeTempSchedule(lessonNum, false) },
                    colors = editButtonColors,
                    modifier = Modifier.weight(1f)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.lesson_wont_be),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        textAlign = TextAlign.Center
                    )
                }
            }
            TextButton(
                onClick = { showChangeTempScheduleDialog = true },
                colors = editButtonColors,
                modifier = Modifier.weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.replace),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showChangeTempScheduleDialog) {
        ChangeTempScheduleDialog(
            tempSchedule = tempSchedule,
            onDismissRequest = { showChangeTempScheduleDialog = false },
            onDone = { subject, surname, name, patronymic, cabinet, building ->
                viewModel.changeTempSchedule(
                    lessonNum = lessonNum,
                    willLessonBe = true,
                    subject = subject,
                    surname = surname,
                    name = name,
                    patronymic = patronymic,
                    cabinet = cabinet,
                    building = building
                )
                showChangeTempScheduleDialog = false
            },
            viewModel = viewModel
        )
    }
}

@Composable
private fun ChangeTempScheduleDialog(
    tempSchedule: TempSchedule?,
    onDismissRequest: () -> Unit,
    onDone: (subject: String, surname: String?, name: String?, patronymic: String?, cabinet: String?, building: String?) -> Unit,
    viewModel: TempScheduleScreenViewModel
) {
    val scrollState = rememberScrollState()

    var subject by remember { mutableStateOf(tempSchedule?.subject?.subject ?: "") }
    var surname by remember { mutableStateOf(tempSchedule?.teacher?.surname ?: "") }
    var name by remember { mutableStateOf(tempSchedule?.teacher?.name ?: "") }
    var patronymic by remember { mutableStateOf(tempSchedule?.teacher?.patronymic ?: "") }
    var cabinet by remember { mutableStateOf(tempSchedule?.cabinet?.cabinet ?: "") }
    var building by remember { mutableStateOf(tempSchedule?.cabinet?.cabinet ?: "") }

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
                Text(
                    text = stringResource(R.string.subject),
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownTextField(
                    value = subject,
                    onValueChange = { subject = it.trimStart() },
                    label = stringResource(R.string.subject),
                    suggestions = viewModel.subjects.map { it.subject },
                    viewModel = viewModel
                )
                Text(
                    text = stringResource(R.string.teacher_optional),
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 20.dp)
                        .fillMaxWidth()
                )
                DropdownTextField(
                    value = surname,
                    onValueChange = { surname = it.trimStart() },
                    label = stringResource(R.string.teacher_surname),
                    suggestions = viewModel.teachers.map { it.surname }.distinct(),
                    viewModel = viewModel
                )
                DropdownTextField(
                    value = name,
                    onValueChange = { name = it.trimStart() },
                    label = stringResource(R.string.teacher_name_optional),
                    suggestions = viewModel.teachers.filter { it.name != null }.map { it.name!! }.distinct().sorted(),
                    viewModel = viewModel
                )
                DropdownTextField(
                    value = patronymic,
                    onValueChange = { patronymic = it.trimStart() },
                    label = stringResource(R.string.teacher_patronymic_optional),
                    suggestions = viewModel.teachers.filter { it.patronymic != null }.map { it.patronymic!! }.distinct().sorted(),
                    viewModel = viewModel
                )
                Text(
                    text = stringResource(R.string.cabinet_optional),
                    fontSize = viewModel.textSize,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 20.dp)
                        .fillMaxWidth()
                )
                DropdownTextField(
                    value = cabinet,
                    onValueChange = { cabinet = it.trimStart() },
                    label = stringResource(R.string.cabinet),
                    suggestions = viewModel.cabinets.map { it.cabinet }.distinct(),
                    viewModel = viewModel
                )
                DropdownTextField(
                    value = building,
                    onValueChange = { building = it.trimStart() },
                    label = stringResource(R.string.building_optional),
                    suggestions = viewModel.cabinets.filter { it.building != null }.map { it.building!! }.distinct().sorted(),
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
                            if (subject.isNotBlank() && (surname.isNotBlank() || name.isBlank() && patronymic.isBlank()) && (cabinet.isNotBlank() || building.isBlank())) {
                                onDone(subject.trim(), surname.trim(), name.trim(), patronymic.trim(), cabinet.trim(), building.trim())
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
    viewModel: TempScheduleScreenViewModel
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
