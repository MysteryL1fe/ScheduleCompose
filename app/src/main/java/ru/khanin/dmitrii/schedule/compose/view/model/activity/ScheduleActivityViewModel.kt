package ru.khanin.dmitrii.schedule.compose.view.model.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.khanin.dmitrii.schedule.compose.activity.MenuItem
import ru.khanin.dmitrii.schedule.compose.utils.SettingsStorage

class ScheduleActivityViewModel(
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