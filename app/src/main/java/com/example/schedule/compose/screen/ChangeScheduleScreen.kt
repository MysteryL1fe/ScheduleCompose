package com.example.schedule.compose.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.ChangeScheduleScreenViewModel

private lateinit var buttonColors: ButtonColors
private lateinit var iconButtonColors: IconButtonColors
private lateinit var textFieldColors: TextFieldColors
private lateinit var checkboxColors: CheckboxColors

@Composable
fun ChangeScheduleScreen(
    viewModel: ChangeScheduleScreenViewModel,
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
    checkboxColors = CheckboxDefaults.colors(
        checkedColor = MaterialTheme.colorScheme.secondary,
        uncheckedColor = MaterialTheme.colorScheme.primary,
        checkmarkColor = MaterialTheme.colorScheme.tertiary
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextButton(
            onClick = { viewModel.showDayPickerDialog = true },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(25.dp),
            colors = buttonColors
        ) {
            Text(
                text = Utils.daysOfWeekNames[viewModel.dayOfWeek - 1],
                fontSize = viewModel.textSize,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChangeSchedulesView(viewModel = viewModel)
        }
    }

    if (viewModel.showDayPickerDialog) {
        DayOfWeekPickerDialog(viewModel)
    }
}

@Composable
private fun DayOfWeekPickerDialog(
    viewModel: ChangeScheduleScreenViewModel
) {
    Dialog(
        onDismissRequest = { viewModel.showDayPickerDialog = false }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.choose_day_of_week),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize
                )
                LazyColumn {
                    itemsIndexed(Utils.daysOfWeekNames) { index, item ->
                        TextButton(
                            onClick = { viewModel.onDaySelected(index + 1) },
                            colors = buttonColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = viewModel.textSize
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChangeSchedulesView(
    viewModel: ChangeScheduleScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        for (i in 1..8) {
            ChangeScheduleView(
                lessonNum = i,
                viewModel = viewModel
            )
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun ChangeScheduleView(
    lessonNum: Int,
    viewModel: ChangeScheduleScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
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
        if (viewModel.numerator[lessonNum - 1]?.subject?.subject?.isNotEmpty() == true
            && viewModel.denominator[lessonNum - 1]?.subject?.subject?.isNotEmpty() == true
            && viewModel.numerator[lessonNum - 1]!!.subject.subject == viewModel.denominator[lessonNum - 1]!!.subject.subject
            || viewModel.numerator[lessonNum - 1]?.subject?.subject.isNullOrEmpty()
            && viewModel.denominator[lessonNum - 1]?.subject?.subject.isNullOrEmpty()) {
            ScheduleCard(
                schedule = viewModel.numerator[lessonNum - 1],
                isNumerator = true,
                isDenominator = true,
                onChangeLesson = { isNumerator, isDenominator, subject, surname, name, patronymic, cabinet, building ->
                    if (isNumerator) {
                        viewModel.changeLesson(lessonNum, true, subject, surname, name, patronymic, cabinet, building)
                    } else {
                        viewModel.deleteLesson(lessonNum, true)
                    }

                    if (isDenominator) {
                        viewModel.changeLesson(lessonNum, false, subject, surname, name, patronymic, cabinet, building)
                    } else {
                        viewModel.deleteLesson(lessonNum, false)
                    }
                },
                onDeleteLesson = {
                    viewModel.deleteLesson(lessonNum, true)
                    viewModel.deleteLesson(lessonNum, false)
                },
                viewModel = viewModel
            )
        } else {
            ScheduleCard(
                schedule = viewModel.numerator[lessonNum - 1],
                isNumerator = true,
                isDenominator = false,
                onChangeLesson = { isNumerator, isDenominator, subject, surname, name, patronymic, cabinet, building ->
                    if (isNumerator) {
                        viewModel.changeLesson(lessonNum, true, subject, surname, name, patronymic, cabinet, building)
                    } else {
                        viewModel.deleteLesson(lessonNum, true)
                    }

                    if (isDenominator) {
                        viewModel.changeLesson(lessonNum, false, subject, surname, name, patronymic, cabinet, building)
                    }
                },
                onDeleteLesson = {
                    viewModel.deleteLesson(lessonNum, true)
                },
                viewModel = viewModel
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            ScheduleCard(
                schedule = viewModel.denominator[lessonNum - 1],
                isNumerator = false,
                isDenominator = true,
                onChangeLesson = { isNumerator, isDenominator, subject, surname, name, patronymic, cabinet, building ->
                    if (isNumerator) {
                        viewModel.changeLesson(lessonNum, true, subject, surname, name, patronymic, cabinet, building)
                    }

                    if (isDenominator) {
                        viewModel.changeLesson(lessonNum, false, subject, surname, name, patronymic, cabinet, building)
                    } else {
                        viewModel.deleteLesson(lessonNum, false)
                    }
                },
                onDeleteLesson = {
                    viewModel.deleteLesson(lessonNum, false)
                },
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun ChangeScheduleDialog(
    schedule: Schedule?,
    isNumerator: Boolean,
    isDenominator: Boolean,
    onDismissRequest: () -> Unit,
    onDone: (isNumerator: Boolean, isDenominator: Boolean, subject: String, surname: String, name: String, patronymic: String, cabinet: String, building: String) -> Unit,
    viewModel: ChangeScheduleScreenViewModel
) {
    val scrollState = rememberScrollState()

    var subject by remember { mutableStateOf(schedule?.subject?.subject ?: "") }
    var surname by remember { mutableStateOf(schedule?.teacher?.surname ?: "") }
    var name by remember { mutableStateOf(schedule?.teacher?.name ?: "") }
    var patronymic by remember { mutableStateOf(schedule?.teacher?.patronymic ?: "") }
    var cabinet by remember { mutableStateOf(schedule?.cabinet?.cabinet ?: "") }
    var building by remember { mutableStateOf(schedule?.cabinet?.cabinet ?: "") }
    var numerator by remember { mutableStateOf(isNumerator) }
    var denominator by remember { mutableStateOf(isDenominator) }

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
                    onValueChange = { subject = it },
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
                    onValueChange = { surname = it },
                    label = stringResource(R.string.teacher_surname),
                    suggestions = viewModel.teachers.map { it.surname }.distinct(),
                    viewModel = viewModel
                )
                DropdownTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(R.string.teacher_name_optional),
                    suggestions = viewModel.teachers.filter { it.name != null }.map { it.name!! }.distinct().sorted(),
                    viewModel = viewModel
                )
                DropdownTextField(
                    value = patronymic,
                    onValueChange = { patronymic = it },
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
                    onValueChange = { cabinet = it },
                    label = stringResource(R.string.cabinet),
                    suggestions = viewModel.cabinets.map { it.cabinet }.distinct(),
                    viewModel = viewModel
                )
                DropdownTextField(
                    value = building,
                    onValueChange = { building = it },
                    label = stringResource(R.string.building_optional),
                    suggestions = viewModel.cabinets.filter { it.building != null }.map { it.building!! }.distinct().sorted(),
                    viewModel = viewModel
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .toggleable(
                            value = numerator,
                            onValueChange = { numerator = it },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(
                        checked = numerator,
                        onCheckedChange = null,
                        colors = checkboxColors
                    )
                    Text(
                        text = stringResource(R.string.numerator),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .toggleable(
                            value = denominator,
                            onValueChange = { denominator = it },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(
                        checked = denominator,
                        onCheckedChange = null,
                        colors = checkboxColors
                    )
                    Text(
                        text = stringResource(R.string.denominator),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = viewModel.textSize,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = viewModel.textSize
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            if (subject.isNotBlank() && (surname.isNotBlank() || name.isBlank() && patronymic.isBlank()) && (cabinet.isNotBlank() || building.isBlank()) && (numerator || denominator)) {
                                onDone(numerator, denominator, subject.trim(), surname.trim(), name.trim(), patronymic.trim(), cabinet.trim(), building.trim())
                            }
                        },
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = viewModel.textSize
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleCard(
    schedule: Schedule?,
    isNumerator: Boolean,
    isDenominator: Boolean,
    onChangeLesson: (isNumerator: Boolean, isDenominator: Boolean, subject: String, surname: String, name: String, patronymic: String, cabinet: String, building: String) -> Unit,
    onDeleteLesson: () -> Unit,
    viewModel: ChangeScheduleScreenViewModel
) {
    var showChangeLessonDialog by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        if (schedule != null) {
            Column(
                modifier = Modifier.weight(0.75f)
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

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.25f)
        ) {
            IconButton (
                onClick = { showChangeLessonDialog = true },
                colors = iconButtonColors,
                modifier = Modifier.padding(0.dp, 10.dp, 10.dp, 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_rename),
                    contentDescription = "Change lesson"
                )
            }

            if (schedule != null) {
                IconButton(
                    onClick = onDeleteLesson,
                    colors = iconButtonColors,
                    modifier = Modifier.padding(0.dp, 10.dp, 10.dp, 10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_thrash),
                        contentDescription = "Delete lesson"
                    )
                }
            }
        }
    }

    if (showChangeLessonDialog) {
        ChangeScheduleDialog(
            schedule = schedule,
            isNumerator = isNumerator,
            isDenominator = isDenominator,
            onDismissRequest = { showChangeLessonDialog = false },
            onDone = { numerator, denominator, subject, surname, name, patronymic, cabinet, building ->
                onChangeLesson(numerator, denominator, subject, surname, name, patronymic, cabinet, building)
                showChangeLessonDialog = false
            },
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    viewModel: ChangeScheduleScreenViewModel
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