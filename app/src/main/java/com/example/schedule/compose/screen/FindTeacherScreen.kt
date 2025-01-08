package com.example.schedule.compose.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.entity.Schedule
import com.example.schedule.compose.utils.Utils
import com.example.schedule.compose.view.model.screen.FindTeacherScreenViewModel
import java.time.format.DateTimeFormatter

private lateinit var buttonColors: ButtonColors
private lateinit var textFieldColors: TextFieldColors

@Composable
fun FindTeacherScreen(
    viewModel: FindTeacherScreenViewModel,
    modifier: Modifier = Modifier
) {
    buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary
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

    var surname by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
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

        TextButton(
            onClick = {
                if (surname.isNotBlank()) {
                    viewModel.findSchedules(surname.trim(), name.trim(), patronymic.trim())
                }
            },
            colors = buttonColors,
            contentPadding = PaddingValues(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.find_teacher),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = viewModel.textSize,
                textAlign = TextAlign.Center
            )
        }

        if (viewModel.loading) {
            val infiniteTransition = rememberInfiniteTransition()
            val progress by infiniteTransition.animateFloat(
                initialValue = 0.0f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.schedules.keys.toList()) { date ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    val formattedDate = date.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
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
                    viewModel.schedules[date]!!.forEach { schedule ->
                        ScheduleView(
                            schedule = schedule,
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
    }
}

@Composable
private fun ScheduleView(
    schedule: Schedule,
    viewModel: FindTeacherScreenViewModel
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
                    text = schedule.lessonNum.toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = viewModel.textSize,
                    modifier = Modifier.padding(25.dp, 5.dp, 10.dp, 5.dp)
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Text(
                text = Utils.lessonsBeginning[schedule.lessonNum - 1] + " - " + Utils.lessonsEnding[schedule.lessonNum - 1],
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = viewModel.textSize,
                modifier = Modifier.padding(10.dp, 0.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    viewModel: FindTeacherScreenViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp, end = 16.dp)
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
            modifier = Modifier
                .menuAnchor()
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
