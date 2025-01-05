/*
package com.example.schedule.compose.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.repo.HomeworkRepo
import java.time.LocalDate

class ChangeHomeworkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChangeHomeworkScreen(
                flowLvl = intent.getIntExtra("flowLvl", 0),
                course = intent.getIntExtra("course", 0),
                group = intent.getIntExtra("group", 0),
                subgroup = intent.getIntExtra("subgroup", 0),
                year = intent.getIntExtra("year", 0),
                month = intent.getIntExtra("month", 0),
                day = intent.getIntExtra("day", 0),
                lessonNum = intent.getIntExtra("lessonNum", 0),
                lessonName = intent.getStringExtra("lessonName") ?: "",
                homework = intent.getStringExtra("homework") ?: ""
            )
        }
    }
}

@Composable
fun ChangeHomeworkScreen(
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int,
    year: Int,
    month: Int,
    day: Int,
    lessonNum: Int,
    lessonName: String,
    homework: String
) {
    var lessonNameState by remember { mutableStateOf(lessonName) }
    var homeworkState by remember { mutableStateOf(homework) }

    val buttonTextSize = when (SettingsStorage.textSize) {
        0 -> 10.sp
        1 -> 20.sp
        2 -> 30.sp
        else -> 20.sp
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lesson Name TextField
        OutlinedTextField(
            value = lessonNameState,
            onValueChange = { lessonNameState = it },
            label = { Text("Lesson Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Homework TextField
        OutlinedTextField(
            value = homeworkState,
            onValueChange = { homeworkState = it },
            label = { Text("Homework") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Cancel Button
            Button(
                onClick = { finish() },
                modifier = Modifier.weight(1f),
                content = { Text("Cancel", fontSize = buttonTextSize) }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // OK Button
            Button(
                onClick = {
                    if (homeworkState.trim().isNotEmpty()) {
                        val homeworkRepo = HomeworkRepo(this@ChangeHomeworkActivity)
                        homeworkRepo.addOrUpdate(
                            flowLvl,
                            course,
                            group,
                            subgroup,
                            lessonNameState,
                            homeworkState,
                            LocalDate.of(year, month, day),
                            lessonNum
                        )
                        finish()
                    } else {
                        Toast.makeText(context, "Homework cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f),
                content = { Text("OK", fontSize = buttonTextSize) }
            )
        }
    }
}
*/
