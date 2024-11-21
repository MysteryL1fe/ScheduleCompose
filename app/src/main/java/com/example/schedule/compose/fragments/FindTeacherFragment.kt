/*
package com.example.schedule.compose.fragments

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.RetrofitHelper
import com.example.schedule.compose.SettingsStorage
import com.example.schedule.compose.dto.ScheduleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun FindTeacherScreen() {
    var teacherName by remember { mutableStateOf("") }
    var lessons by remember { mutableStateOf<List<LessonsView>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val textSize = SettingsStorage.textSize
    val teacherEditTextFontSize = when (textSize) {
        0 -> 12.sp
        2 -> 36.sp
        else -> 24.sp
    }
    val buttonFontSize = when (textSize) {
        0 -> 10.sp
        2 -> 30.sp
        else -> 20.sp
    }
    val notFoundTextFontSize = when (textSize) {
        0 -> 12.sp
        2 -> 36.sp
        else -> 24.sp
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Teacher input field
        OutlinedTextField(
            value = teacherName,
            onValueChange = { teacherName = it },
            label = { Text(text = "Teacher") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.body1.copy(fontSize = teacherEditTextFontSize),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Find button
        Button(
            onClick = { findTeacher(teacherName) { result, error ->
                isLoading = false
                if (result != null) {
                    lessons = result
                    errorMessage = ""
                } else {
                    errorMessage = error ?: "Unknown error"
                }
            } },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(25.dp)
        ) {
            Text(
                text = "Find",
                fontSize = buttonFontSize
            )
        }

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Error message if no results are found
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = notFoundTextFontSize,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // Display the lessons if available
        if (lessons.isNotEmpty()) {
            lessons.forEach { lesson ->
                LessonItem(lesson)
            }
        }
    }
}

@Composable
fun LessonItem(lesson: LessonsView) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Lesson: ${lesson.lessonName}",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Teacher: ${lesson.teacher}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Room: ${lesson.cabinet}",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(8.dp)
        )
        Divider()
    }
}

@Composable
fun findTeacher(teacherName: String, callback: (List<LessonsView>?, String?) -> Unit) {
    if (teacherName.isEmpty()) {
        callback(null, "Please enter a teacher's name.")
        return
    }

    // Show loading state
    val scope = rememberCoroutineScope()
    scope.launch {
        try {
            val service = RetrofitHelper.getBackendService()
            val call = service.getAllSchedulesByTeacher(teacherName)
            val response = withContext(Dispatchers.IO) { call.execute() }

            if (response.isSuccessful && response.body() != null) {
                val schedules = response.body()!!
                val lessonsViews = transformSchedulesToLessonsView(schedules)
                callback(lessonsViews, null)
            } else {
                callback(null, "No lessons found.")
            }
        } catch (e: Exception) {
            Log.e("Backend", e.toString())
            callback(null, "Error fetching data.")
        }
    }
}

fun transformSchedulesToLessonsView(schedules: List<ScheduleResponse>): List<LessonsView> {
    val lessonsViews = mutableListOf<LessonsView>()
    // Assuming that you want to sort and group the lessons as per your original code logic
    for (schedule in schedules) {
        val lesson = schedule.lesson
        val lessonsView = LessonsView(
            lessonName = lesson.name,
            teacher = lesson.teacher,
            cabinet = lesson.cabinet
        )
        lessonsViews.add(lessonsView)
    }
    return lessonsViews
}

data class LessonsView(
    val lessonName: String,
    val teacher: String,
    val cabinet: String
)
*/
