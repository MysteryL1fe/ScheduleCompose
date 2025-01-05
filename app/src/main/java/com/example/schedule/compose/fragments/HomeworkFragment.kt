/*
package com.example.schedule.compose.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.HomeworkRepo
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.views.HomeworkView

@Composable
fun HomeworkScreen(
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int
) {
    var homeworks by remember { mutableStateOf<List<Homework>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var textSize by remember { mutableStateOf(SettingsStorage.textSize) }

    val buttonTextSize = when (textSize) {
        0 -> 10.sp
        2 -> 30.sp
        else -> 20.sp
    }

    val homeworkTextSize = when (textSize) {
        0 -> 12.sp
        2 -> 36.sp
        else -> 24.sp
    }

    // Load homework list
    LaunchedEffect(flowLvl, course, group, subgroup) {
        isLoading = true
        homeworks = HomeworkRepo().findAllByFlow(flowLvl, course, group, subgroup)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // New Homework Button
        Button(
            onClick = { */
/* Implement the action for adding new homework *//*
 },
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
        ) {
            Text(text = "Change Homework", fontSize = buttonTextSize)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading or Error Message
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (homeworks.isEmpty()) {
            Text(
                text = "No Homeworks Found",
                fontSize = homeworkTextSize,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = Color.Gray,
                style = TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            )
        } else {
            // List of Homework Views
            homeworks.forEach { homework ->
                HomeworkItem(homework)
            }
        }
    }
}

@Composable
fun HomeworkItem(homework: Homework) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Subject: ${homework.subject}",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Description: ${homework.description}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Due Date: ${homework.dueDate}",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(8.dp)
        )
        Divider()
    }
}
*/
