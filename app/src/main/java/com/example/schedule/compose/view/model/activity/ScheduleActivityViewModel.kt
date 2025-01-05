package com.example.schedule.compose.view.model.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.activity.MenuItem
import com.example.schedule.compose.utils.SettingsStorage

class ScheduleActivityViewModel(
    val flowLvl: Int,
    val course: Int,
    val group: Int,
    val subgroup: Int
) : ViewModel() {
    var activeMenuItem by mutableStateOf(MenuItem.SCHEDULE)
    var textSize by mutableStateOf(SettingsStorage.textSize)

    fun update() {
        textSize = SettingsStorage.textSize
    }
}