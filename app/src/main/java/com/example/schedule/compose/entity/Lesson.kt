package com.example.schedule.compose.entity

data class Lesson(
    var id: Long,
    var name: String,
    var teacher: String?,
    var cabinet: String?
)
