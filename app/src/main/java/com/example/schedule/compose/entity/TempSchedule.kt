package com.example.schedule.compose.entity

import java.time.LocalDate

data class TempSchedule(
    var id: Long,
    var flow: Flow,
    var lessonDate: LocalDate,
    var lessonNum: Int,
    var willLessonBe: Boolean,
    var subject: Subject?,
    var teacher: Teacher?,
    var cabinet: Cabinet?
)
