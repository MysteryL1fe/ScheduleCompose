/*
package com.example.schedule.compose.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.entity.Homework
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeworkView(homework: Homework, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Gray)
            .padding(8.dp)
    ) {
        // Divider at the start
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        // Homework details
        Text(
            text = "${homework.lessonName}, ${homework.lessonDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = homework.homework,
            fontSize = 14.sp
        )

        // Divider between sections
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        // Buttons for delete and change homework
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                // Handle delete homework logic
                Toast.makeText(context, "Homework deleted", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Delete")
            }
            Button(onClick = {
                // Navigate to ChangeHomeworkActivity to modify homework
            }) {
                Text(text = "Change")
            }
        }

        // Divider at the end
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
    }
}

@Preview
@Composable
fun HomeworkViewPreview() {
    HomeworkView(
        homework = Homework("Math", "Solve exercise 5", LocalDate.now(), 1),
        context = LocalContext.current
    )
}
*/
