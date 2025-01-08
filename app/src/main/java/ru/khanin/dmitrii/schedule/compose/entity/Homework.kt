package ru.khanin.dmitrii.schedule.compose.entity

import java.time.LocalDate

data class Homework(
    var id: Long,
    var flow: Flow,
    var homework: String,
    var lessonDate: LocalDate,
    var lessonNum: Int,
    var subject: Subject
)
