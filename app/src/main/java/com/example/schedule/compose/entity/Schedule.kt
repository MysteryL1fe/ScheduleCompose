package com.example.schedule.compose.entity

data class Schedule(
    var id: Long,
    var flow: Long,
    var lesson: Long,
    var dayOfWeek: Int,
    var lessonNum: Int,
    var numerator: Boolean
)
