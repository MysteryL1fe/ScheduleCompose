/*
package com.example.schedule.compose.views

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.R
import com.example.schedule.compose.SettingsStorage

@Composable
fun TimerView(timerSeconds: Long) {
    var timeRemaining by remember { mutableStateOf(timerSeconds + 1) } // add 1 to initial countdown to start
    var isImageVisible by remember { mutableStateOf(true) }

    // Timer countdown logic
    val timer = remember {
        object : CountDownTimer((timeRemaining * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = (millisUntilFinished / 1000).toInt()
                isImageVisible = !isImageVisible
            }

            override fun onFinish() {
                // Update the parent or perform actions when the timer finishes
                // Could use a callback to notify parent
            }
        }
    }

    // Start the timer when the TimerView is first composed
    LaunchedEffect(Unit) {
        timer.start()
    }

    // Layout
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image to toggle visibility
        if (isImageVisible) {
            Image(
                painter = painterResource(id = R.drawable.red_circle), // Replace with your drawable
                contentDescription = "Timer Image",
                modifier = Modifier.size(32.dp)
            )
        }

        // Timer TextView
        Text(
            text = formatTime(timeRemaining),
            style = TextStyle(
                fontSize = when (SettingsStorage.textSize) {
                    0 -> 8.sp
                    2 -> 24.sp
                    else -> 16.sp
                }
            ),
            modifier = Modifier.padding(start = 16.dp)
        )
    }

    Divider(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        color = Color.Gray,
        thickness = 1.dp
    )
}

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format("%02d:%02d", minutes, remainingSeconds)
    }
}

@Preview
@Composable
fun TimerViewPreview() {
    TimerView(timerSeconds = 60) // Pass a test value for timer seconds
}
*/
