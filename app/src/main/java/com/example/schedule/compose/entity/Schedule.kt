package com.example.schedule.compose.entity

data class Schedule(
    var id: Long?,
    var flow: Flow,
    var dayOfWeek: Int,
    var lessonNum: Int,
    var numerator: Boolean,
    var subject: Subject,
    var teacher: Teacher?,
    var cabinet: Cabinet?
)
