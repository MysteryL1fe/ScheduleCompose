/*
package com.example.schedule.compose

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _flowLvl = MutableStateFlow(1)
    val flowLvl: StateFlow<Int> = _flowLvl

    private val _course = MutableStateFlow(0)
    val course: StateFlow<Int> = _course

    private val _group = MutableStateFlow(0)
    val group: StateFlow<Int> = _group

    private val _subgroup = MutableStateFlow(0)
    val subgroup: StateFlow<Int> = _subgroup

    fun setFlowLvl(level: Int) {
        _flowLvl.value = level
        resetCourseGroupSubgroup()
    }

    fun setCourse(course: Int) {
        _course.value = course
        _group.value = 0
        _subgroup.value = 0
    }

    fun setGroup(group: Int) {
        _group.value = group
        _subgroup.value = 0
    }

    fun setSubgroup(subgroup: Int) {
        _subgroup.value = subgroup
    }

    private fun resetCourseGroupSubgroup() {
        _course.value = 0
        _group.value = 0
        _subgroup.value = 0
    }
}
*/
