package com.example.schedule.compose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.Utils
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.Dialog
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.view.model.ChangeScheduleViewModel

@Composable
fun ChangeScheduleScreen(
    viewModel: ChangeScheduleViewModel,
    textSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = colorResource(R.color.primary),
        contentColor = colorResource(R.color.secondary),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextButton(
            onClick = { viewModel.showDayPickerDialog = true },
            modifier = Modifier.padding(20.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(25.dp),
            colors = buttonColors
        ) {
            Text(
                text = Utils.daysOfWeekNames[viewModel.dayOfWeek - 1],
                fontSize = textSize,
                color = colorResource(R.color.text)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChangeLessonsView(
                viewModel = viewModel,
                textSize = textSize,
                buttonColors = buttonColors
            )
        }
    }

    if (viewModel.showDayPickerDialog) {
        DayOfWeekPickerDialog(
            textSize = textSize,
            buttonColors = buttonColors,
            onDismissRequest = { viewModel.showDayPickerDialog = false }
        ) { selectedDay ->
            viewModel.dayOfWeek = selectedDay
            viewModel.showDayPickerDialog = false
            viewModel.updateData()
        }
    }
}

@Composable
private fun DayOfWeekPickerDialog(
    textSize: TextUnit,
    buttonColors: ButtonColors,
    onDismissRequest: () -> Unit,
    onDaySelected: (Int) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colorResource(R.color.background),
            border = BorderStroke(1.dp, colorResource(R.color.secondary))
        ) {
            Column {
                Text(
                    text = stringResource(R.string.choose_day_of_week),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = colorResource(R.color.text),
                    fontSize = textSize
                )
                LazyColumn {
                    itemsIndexed(Utils.daysOfWeekNames) { index, item ->
                        TextButton(
                            onClick = { onDaySelected(index + 1) },
                            colors = buttonColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item,
                                color = colorResource(R.color.text),
                                fontSize = textSize
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChangeLessonsView(
    viewModel: ChangeScheduleViewModel,
    textSize: TextUnit,
    buttonColors: ButtonColors
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.secondary)
        )
        for (i in 1..8) {
            ChangeLessonView(
                lessonNum = i,
                viewModel = viewModel,
                textSize = textSize,
                buttonColors = buttonColors
            )
            HorizontalDivider(
                thickness = 2.dp,
                color = colorResource(R.color.secondary)
            )
        }
    }
}

@Composable
private fun ChangeLessonView(
    lessonNum: Int,
    viewModel: ChangeScheduleViewModel,
    textSize: TextUnit,
    buttonColors: ButtonColors
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
        if (viewModel.numerator[lessonNum - 1]?.name?.isNotEmpty() == true
            && viewModel.denominator[lessonNum - 1]?.name?.isNotEmpty() == true
            && viewModel.numerator[lessonNum - 1]!!.name == viewModel.denominator[lessonNum - 1]!!.name
            || viewModel.numerator[lessonNum - 1]?.name.isNullOrEmpty()
            && viewModel.denominator[lessonNum - 1]?.name.isNullOrEmpty()) {
            LessonCard(
                lesson = viewModel.numerator[lessonNum - 1],
                isNumerator = true,
                isDenominator = true,
                textSize = textSize,
                buttonColors = buttonColors,
                onChangeLesson = { lessonName, teacher, cabinet, isNumerator, isDenominator ->
                    if (isNumerator) {
                        viewModel.changeLesson(
                            lessonName, teacher, cabinet, lessonNum, true
                        )
                    } else {
                        viewModel.deleteLesson(
                            lessonNum, true
                        )
                    }

                    if (isDenominator) {
                        viewModel.changeLesson(
                            lessonName, teacher, cabinet, lessonNum, false
                        )
                    } else {
                        viewModel.deleteLesson(lessonNum, false)
                    }
                },
                onDeleteLesson = {
                    viewModel.deleteLesson(lessonNum, true)
                    viewModel.deleteLesson(lessonNum, false)
                }
            )
        } else {
            LessonCard(
                lesson = viewModel.numerator[lessonNum - 1],
                isNumerator = true,
                isDenominator = false,
                textSize = textSize,
                buttonColors = buttonColors,
                onChangeLesson = { lessonName, teacher, cabinet, isNumerator, isDenominator ->
                    if (isNumerator) {
                        viewModel.changeLesson(
                            lessonName, teacher, cabinet, lessonNum, true
                        )
                    } else {
                        viewModel.deleteLesson(lessonNum, true)
                    }

                    if (isDenominator) {
                        viewModel.changeLesson(
                            lessonName, teacher, cabinet, lessonNum, false
                        )
                    }
                },
                onDeleteLesson = {
                    viewModel.deleteLesson(lessonNum, true)
                }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = colorResource(R.color.text)
            )
            LessonCard(
                lesson = viewModel.denominator[lessonNum - 1],
                isNumerator = false,
                isDenominator = true,
                textSize = textSize,
                buttonColors = buttonColors,
                onChangeLesson = { lessonName, teacher, cabinet, isNumerator, isDenominator ->
                    if (isNumerator) {
                        viewModel.changeLesson(
                            lessonName, teacher, cabinet, lessonNum, true
                        )
                    }

                    if (isDenominator) {
                        viewModel.changeLesson(
                            lessonName, teacher, cabinet, lessonNum, false
                        )
                    } else {
                        viewModel.deleteLesson(lessonNum, false)
                    }
                },
                onDeleteLesson = {
                    viewModel.deleteLesson(lessonNum, false)
                }
            )
        }
    }
}

@Composable
private fun LessonCard(
    lesson: Lesson?,
    isNumerator: Boolean,
    isDenominator: Boolean,
    textSize: TextUnit,
    buttonColors: ButtonColors,
    onChangeLesson: (lessonName: String, teacher: String, cabinet: String, isNumerator: Boolean, isDenominator: Boolean) -> Unit,
    onDeleteLesson: () -> Unit
) {
    val iconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = colorResource(R.color.secondary),
        contentColor = colorResource(R.color.text),
    )
    var showChangeLessonDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (lesson != null) {
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

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
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

            if (lesson != null) {
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
        ChangeLessonDialog(
            lesson = lesson,
            isNumerator = isNumerator,
            isDenominator = isDenominator,
            textSize = textSize,
            buttonColors = buttonColors,
            onDismissRequest = { showChangeLessonDialog = false }
        ) { lessonName, teacher, cabinet, numerator, denominator ->
            onChangeLesson(lessonName, teacher, cabinet, numerator, denominator)
            showChangeLessonDialog = false
        }
    }
}

@Composable
private fun ChangeLessonDialog(
    lesson: Lesson?,
    isNumerator: Boolean,
    isDenominator: Boolean,
    textSize: TextUnit,
    buttonColors: ButtonColors,
    onDismissRequest: () -> Unit,
    onDone: (lessonName: String, teacher: String, cabinet: String, numerator: Boolean, denominator: Boolean) -> Unit
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = colorResource(R.color.text),
        unfocusedTextColor = colorResource(R.color.text),
        cursorColor = colorResource(R.color.text),
        focusedLabelColor = colorResource(R.color.secondary),
        unfocusedLabelColor = colorResource(R.color.secondary),
        focusedContainerColor = colorResource(R.color.primary),
        unfocusedContainerColor = colorResource(R.color.primary),
        focusedIndicatorColor = colorResource(R.color.secondary),
        unfocusedIndicatorColor = colorResource(R.color.secondary)
    )
    var lessonName by remember { mutableStateOf(lesson?.name ?: "") }
    var teacher by remember { mutableStateOf(lesson?.teacher ?: "") }
    var cabinet by remember { mutableStateOf(lesson?.cabinet ?: "") }
    var numerator by remember { mutableStateOf(isNumerator) }
    var denominator by remember { mutableStateOf(isDenominator) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colorResource(R.color.background),
            border = BorderStroke(1.dp, colorResource(R.color.secondary)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TextField(
                    value = lessonName,
                    onValueChange = { lessonName = it },
                    label = {
                        Text(
                            text = stringResource(R.string.lesson_name),
                            fontSize = textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = textSize),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = {
                        Text(
                            text = stringResource(R.string.teacher),
                            fontSize = textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = textSize),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = cabinet,
                    onValueChange = { cabinet = it },
                    label = {
                        Text(
                            text = stringResource(R.string.cabinet),
                            fontSize = textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = textSize),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                        .fillMaxWidth()
                        .toggleable(
                            value = numerator,
                            onValueChange = { numerator = it },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(
                        checked = numerator,
                        onCheckedChange = null
                    )
                    Text(
                        text = stringResource(R.string.numerator),
                        color = colorResource(R.color.text),
                        fontSize = textSize,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                        .fillMaxWidth()
                        .toggleable(
                            value = denominator,
                            onValueChange = { denominator = it },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(
                        checked = denominator,
                        onCheckedChange = null
                    )
                    Text(
                        text = stringResource(R.string.denominator),
                        color = colorResource(R.color.text),
                        fontSize = textSize,
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
                            color = colorResource(R.color.text),
                            fontSize = textSize
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            if (lessonName.isNotBlank() && (numerator || denominator)) {
                                onDone(lessonName.trim(), teacher.trim(), cabinet.trim(), numerator, denominator)
                            }
                        },
                        colors = buttonColors,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            color = colorResource(R.color.text),
                            fontSize = textSize
                        )
                    }
                }
            }
        }
    }
}
