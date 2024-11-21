package com.example.schedule.compose.entity

import java.time.LocalDate

data class Homework(
    var id: Long,
    var homework: String,
    var lessonDate: LocalDate,
    var lessonNum: Int,
    var flow: Long,
    var lessonName: String
)
