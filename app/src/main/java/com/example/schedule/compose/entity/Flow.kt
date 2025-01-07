package com.example.schedule.compose.entity

import java.time.LocalDate
import java.time.LocalDateTime

data class Flow(
    var id: Long?,
    var educationLevel: Int,
    var course: Int,
    var group: Int,
    var subgroup: Int,
    var lastEdit: LocalDateTime?,
    var lessonsStartDate: LocalDate?,
    var sessionStartDate: LocalDate?,
    var sessionEndDate: LocalDate?,
    var active: Boolean?
) {
    constructor(educationLevel: Int, course: Int, group: Int, subgroup: Int) : this(null, educationLevel, course, group, subgroup, null, null, null, null, null)
    constructor(id: Long?, educationLevel: Int, course: Int, group: Int, subgroup: Int) : this(id, educationLevel, course, group, subgroup, null, null, null, null, null)
}
