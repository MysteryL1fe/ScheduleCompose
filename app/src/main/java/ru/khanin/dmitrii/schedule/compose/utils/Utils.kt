package ru.khanin.dmitrii.schedule.compose.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Utils {
    val daysOfWeekNames = arrayOf(
        "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"
    )
    val lessonsBeginning = listOf("8:00", "9:45", "11:30", "13:25", "15:10", "16:55", "18:40", "20:10")
    val lessonsEnding = listOf("9:35", "11:20", "13:05", "15:00", "16:45", "18:30", "20:00", "21:30")

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

    fun getNearestLesson(dayOfWeek: Int, lessonNum: Int, numerator: Boolean, from: LocalDateTime): LocalDateTime {
        val fromDate = from.toLocalDate()
        val thisWeek = (fromDate.dayOfWeek.value < dayOfWeek || fromDate.dayOfWeek.value == dayOfWeek && from.toLocalTime().isBefore(
            LocalTime.parse(lessonsBeginning[lessonNum - 1], DateTimeFormatter.ofPattern("H[H]:mm"))
        ))
        var result = LocalDateTime.of(
            LocalDate.ofEpochDay(fromDate.toEpochDay() + dayOfWeek - from.dayOfWeek.value + if (thisWeek) 0 else 7),
            LocalTime.parse(lessonsBeginning[lessonNum - 1], DateTimeFormatter.ofPattern("H[H]:mm"))
        )
        if (isNumerator(result.toLocalDate()) != numerator) result = result.plusDays(7)
        return result
    }
}
