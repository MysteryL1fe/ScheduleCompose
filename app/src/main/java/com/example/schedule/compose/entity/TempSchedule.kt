package com.example.schedule.compose.entity

import java.time.LocalDate

data class TempSchedule(
    var id: Long,
    var flow: Long,
    var lesson: Long,
    var lessonDate: LocalDate,
    var lessonNum: Int,
    var willLessonBe: Boolean
)
