package ru.khanin.dmitrii.schedule.compose.entity

data class Cabinet(
    var id: Long?,
    var cabinet: String,
    var building: String?,
    var address: String?
)