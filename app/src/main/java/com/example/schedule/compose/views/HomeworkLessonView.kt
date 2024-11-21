/*
package com.example.schedule.compose.views

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.R
import com.example.schedule.compose.activities.ChangeHomeworkActivity
import com.example.schedule.compose.entity.Homework
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.repo.HomeworkRepo
import com.example.schedule.compose.Utils
import java.time.LocalDate

@Composable
fun HomeworkLessonView(
    flowLvl: Int,
    course: Int,
    group: Int,
    subgroup: Int,
    date: LocalDate,
    lessonNum: Int,
    lesson: Lesson?,
    tempLesson: Lesson?,
    homework: Homework?,
    willLessonBe: Boolean,
    homeworkRepo: HomeworkRepo
) {
    val lessonName = tempLesson?.name ?: lesson?.name ?: ""
    val lessonTeacher = tempLesson?.teacher ?: lesson?.teacher ?: ""
    val lessonCabinet = tempLesson?.cabinet ?: lesson?.cabinet ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Gray)
            .padding(8.dp)
    ) {
        // Lesson Number and Time Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$lessonNum",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = Utils.getTimeByLesson(lessonNum),
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }

        // Lesson Name and Teacher/Room Info
        Column(modifier = Modifier.padding(16.dp)) {
            if (lessonName.isNotEmpty()) {
                Text(
                    text = lessonName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (lessonTeacher.isNotEmpty() || lessonCabinet.isNotEmpty()) {
                    Text(
                        text = if (lessonTeacher.isEmpty()) lessonCabinet else if (lessonCabinet.isEmpty()) lessonTeacher else "$lessonTeacher, $lessonCabinet",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Divider between lesson info and homework
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

        // Homework Section
        if (homework == null) {
            ChangeHomeworkButton {
                // Open ChangeHomeworkActivity
                val intent = Intent(context, ChangeHomeworkActivity::class.java).apply {
                    putExtra("flowLvl", flowLvl)
                    putExtra("course", course)
                    putExtra("group", group)
                    putExtra("subgroup", subgroup)
                    putExtra("year", date.year)
                    putExtra("month", date.monthValue)
                    putExtra("day", date.dayOfMonth)
                    putExtra("lessonNum", lessonNum)
                    putExtra("lessonName", lessonName)
                    putExtra("homework", "")
                }
                context.startActivity(intent)
            }
        } else {
            HomeworkDisplay(homework = homework, onDeleteHomework = {
                homeworkRepo.deleteByFlowAndLessonDateAndLessonNum(
                    flowLvl, course, group, subgroup, date, lessonNum
                )
                // Update UI to reflect homework deletion
                Toast.makeText(context, "Homework deleted", Toast.LENGTH_SHORT).show()
            })
        }
    }
}

@Composable
fun ChangeHomeworkButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_homework),
            contentDescription = "Change Homework"
        )
    }
}

@Composable
fun HomeworkDisplay(homework: Homework, onDeleteHomework: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Homework for ${homework.lessonName}:")
        Text(text = homework.homework, fontSize = 16.sp)

        // Buttons to change or delete homework
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onDeleteHomework) {
                Text(text = "Delete")
            }
            Button(onClick = {
                // Navigate to ChangeHomeworkActivity to modify homework
            }) {
                Text(text = "Change")
            }
        }
    }
}

@Preview
@Composable
fun HomeworkLessonViewPreview() {
    HomeworkLessonView(
        flowLvl = 1,
        course = 1,
        group = 1,
        subgroup = 1,
        date = LocalDate.now(),
        lessonNum = 1,
        lesson = Lesson("Math", "Mr. Smith", "101"),
        tempLesson = null,
        homework = Homework("Math", "Solve exercise 5", LocalDate.now(), 1),
        willLessonBe = true,
        homeworkRepo = HomeworkRepo(context)
    )
}
*/
