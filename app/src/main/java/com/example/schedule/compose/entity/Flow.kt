package com.example.schedule.compose.entity

import java.time.LocalDate
import java.time.LocalDateTime

data class Flow(
    var id: Long?,
    var flowLvl: Int,
    var course: Int,
    var group: Int,
    var subgroup: Int,
    var lastEdit: LocalDateTime?,
    var lessonsStartDate: LocalDate?,
    var sessionStartDate: LocalDate?,
    var sessionEndDate: LocalDate?,
    var active: Boolean?
) {
    constructor(flowLvl: Int, course: Int, flow: Int, subgroup: Int) : this(null, flowLvl, course, flow, subgroup, null, null, null, null, null)
}
