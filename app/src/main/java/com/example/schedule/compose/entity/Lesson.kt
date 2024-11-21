package com.example.schedule.compose.entity

import java.util.Objects

data class Lesson(
    var id: Long,
    var name: String,
    var teacher: String?,
    var cabinet: String?
)
