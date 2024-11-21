/*
package com.example.schedule.compose.views

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.schedule.compose.R
import com.example.schedule.compose.SettingsStorage
import com.example.schedule.compose.Utils
import com.example.schedule.compose.activities.ChangeLessonActivity
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.entity.Lesson
import com.example.schedule.compose.repo.TempScheduleRepo
import java.time.LocalDate

@Composable
fun TempLessonView(
    context: Context,
    flow: Flow,
    date: LocalDate,
    lessonNum: Int,
    lesson: Lesson?,
    tempLesson: Lesson?,
    willLessonBe: Boolean,
    tempScheduleRepo: TempScheduleRepo
) {
    val lessonName = tempLesson?.name ?: lesson?.name ?: ""
    val lessonTeacher = tempLesson?.teacher ?: lesson?.teacher ?: ""
    val lessonCabinet = tempLesson?.cabinet ?: lesson?.cabinet ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        // First Row: Lesson number and time
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lesson Number
            Text(
                text = "$lessonNum",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(16.dp)
            )

            // Temporary Lesson Indicator (if applicable)
            if (!willLessonBe) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cross),
                    contentDescription = "No lesson",
                    modifier = Modifier.padding(end = 16.dp)
                )
            } else if (tempLesson != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_temporary),
                    contentDescription = "Temporary lesson",
                    modifier = Modifier.padding(end = 16.dp)
                )
            }

            // Time
            Text(
                text = Utils.getTimeByLesson(lessonNum),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        // Second Row: Lesson Name and Teacher
        if (lessonName.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = lessonName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = getTextSize()
                )
                if (lessonCabinet.isNotEmpty() || lessonTeacher.isNotEmpty()) {
                    Text(
                        text = if (lessonTeacher.isEmpty()) lessonCabinet
                        else if (lessonCabinet.isEmpty()) lessonTeacher
                        else "$lessonCabinet, $lessonTeacher",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = getTextSize()
                    )
                }
            }
        }

        // Divider
        Divider(modifier = Modifier.padding(vertical = 10.dp))

        // Action Buttons (Cancel, Replace, etc.)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!willLessonBe) {
                ActionButton(
                    text = "Cancel",
                    onClick = {
                        // Delete Temp Lesson
                        tempScheduleRepo.deleteByFlowAndLessonDateAndLessonNum(
                            flow.flowLvl, flow.course, flow.flow, flow.subgroup,
                            date, lessonNum
                        )
                        Toast.makeText(context, "Temporary lesson canceled", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            if (!willLessonBe && tempLesson == null) {
                ActionButton(
                    text = "Lesson Won't Be",
                    onClick = {
                        // Mark lesson as won't be
                        tempScheduleRepo.addOrUpdate(
                            flow.flowLvl, flow.course, flow.flow, flow.subgroup,
                            lesson?.name ?: "", lesson?.teacher ?: "", lesson?.cabinet ?: "",
                            date, lessonNum, false
                        )
                        Toast.makeText(context, "Lesson marked as won't be", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            ActionButton(
                text = "Replace",
                onClick = {
                    val intent = Intent(context, ChangeLessonActivity::class.java).apply {
                        putExtra("flowLvl", flow.flowLvl)
                        putExtra("course", flow.course)
                        putExtra("group", flow.flow)
                        putExtra("subgroup", flow.subgroup)
                        putExtra("year", date.year)
                        putExtra("month", date.monthValue)
                        putExtra("day", date.dayOfMonth)
                        putExtra("lessonNum", lessonNum)
                        putExtra("isTempView", true)
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun getTextSize(): Float {
    return when (SettingsStorage.textSize) {
        0 -> 8f
        2 -> 24f
        else -> 16f
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTempLessonView() {
    TempLessonView(
        context = LocalContext.current,
        flow = Flow(1, 2, 1, 1),
        date = LocalDate.now(),
        lessonNum = 1,
        lesson = Lesson(name = "Math", teacher = "Mr. Smith", cabinet = "101"),
        tempLesson = null,
        willLessonBe = true,
        tempScheduleRepo = TempScheduleRepo(LocalContext.current)
    )
}
*/
