package com.example.schedule.compose

import androidx.annotation.IntRange
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

object Utils {
    val daysOfWeekNames = arrayOf(
        "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"
    )
    val lessonsBeginning = listOf("8:00", "9:45", "11:30", "13:25", "15:10", "16:55", "18:40", "20:10")
    val lessonsEnding = listOf("9:35", "11:20", "13:05", "15:00", "16:45", "18:30", "20:00", "21:30")

    // Determine if a given date (year, month, day) is a numerator day
    fun isNumerator(year: Int, month: Int, day: Int): Boolean {
        val date = LocalDate.of(year, month, day)
        return isNumerator(date)
    }

    // Check if the given date is a numerator day
    fun isNumerator(date: LocalDate): Boolean {
        var prevYear = LocalDate.of(date.year - 1, 9, 1)
        prevYear = prevYear.minusDays(prevYear.dayOfWeek.value.toLong() - 1)

        var curYear = LocalDate.of(date.year, 9, 1)
        curYear = curYear.minusDays(curYear.dayOfWeek.value.toLong() - 1)

        return if (date.isAfter(curYear)) {
            ((date.toEpochDay() - curYear.toEpochDay()) / 7 % 2).toInt() == 0
        } else {
            ((date.toEpochDay() - prevYear.toEpochDay()) / 7 % 2).toInt() == 0
        }
    }
}
