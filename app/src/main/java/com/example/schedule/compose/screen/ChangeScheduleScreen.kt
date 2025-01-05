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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.ChangeScheduleScreenViewModel

private lateinit var buttonColors: ButtonColors
private lateinit var iconButtonColors: IconButtonColors
private lateinit var textFieldColors: TextFieldColors

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
            ChangeLessonsView(viewModel = viewModel)
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
private fun ChangeLessonsView(
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
            ChangeLessonView(
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
private fun ChangeLessonView(
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
        if (viewModel.numerator[lessonNum - 1]?.name?.isNotEmpty() == true
            && viewModel.denominator[lessonNum - 1]?.name?.isNotEmpty() == true
            && viewModel.numerator[lessonNum - 1]!!.name == viewModel.denominator[lessonNum - 1]!!.name
            || viewModel.numerator[lessonNum - 1]?.name.isNullOrEmpty()
            && viewModel.denominator[lessonNum - 1]?.name.isNullOrEmpty()) {
            LessonCard(
                lesson = viewModel.numerator[lessonNum - 1],
                isNumerator = true,
                isDenominator = true,
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
                },
                viewModel = viewModel
            )
        } else {
            LessonCard(
                lesson = viewModel.numerator[lessonNum - 1],
                isNumerator = true,
                isDenominator = false,
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
                },
                viewModel = viewModel
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            LessonCard(
                lesson = viewModel.denominator[lessonNum - 1],
                isNumerator = false,
                isDenominator = true,
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
                },
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun LessonCard(
    lesson: Lesson?,
    isNumerator: Boolean,
    isDenominator: Boolean,
    onChangeLesson: (lessonName: String, teacher: String, cabinet: String, isNumerator: Boolean, isDenominator: Boolean) -> Unit,
    onDeleteLesson: () -> Unit,
    viewModel: ChangeScheduleScreenViewModel
) {
    var showChangeLessonDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (lesson != null) {
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
            onDismissRequest = { showChangeLessonDialog = false },
            onDone = { lessonName, teacher, cabinet, numerator, denominator ->
                onChangeLesson(lessonName, teacher, cabinet, numerator, denominator)
                showChangeLessonDialog = false
                     },
            viewModel = viewModel
        )
    }
}

@Composable
private fun ChangeLessonDialog(
    lesson: Lesson?,
    isNumerator: Boolean,
    isDenominator: Boolean,
    onDismissRequest: () -> Unit,
    onDone: (lessonName: String, teacher: String, cabinet: String, numerator: Boolean, denominator: Boolean) -> Unit,
    viewModel: ChangeScheduleScreenViewModel
) {
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
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TextField(
                    value = lessonName,
                    onValueChange = { lessonName = it },
                    label = {
                        Text(
                            text = stringResource(R.string.lesson_name),
                            fontSize = viewModel.textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = viewModel.textSize),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = {
                        Text(
                            text = stringResource(R.string.teacher),
                            fontSize = viewModel.textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = viewModel.textSize),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = cabinet,
                    onValueChange = { cabinet = it },
                    label = {
                        Text(
                            text = stringResource(R.string.cabinet),
                            fontSize = viewModel.textSize
                        )
                    },
                    singleLine = true,
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = viewModel.textSize),
                    modifier = Modifier.fillMaxWidth()
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
                        onCheckedChange = null
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
                        onCheckedChange = null
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
                            if (lessonName.isNotBlank() && (numerator || denominator)) {
                                onDone(lessonName.trim(), teacher.trim(), cabinet.trim(), numerator, denominator)
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
