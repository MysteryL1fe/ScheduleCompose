package com.example.schedule.compose.view.model.screen

import android.app.Activity
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModel
import com.example.schedule.compose.repo.ScheduleDBHelper
import com.example.schedule.compose.ui.theme.Theme
import com.example.schedule.compose.utils.SettingsStorage
import com.example.schedule.compose.view.model.ThemeManager
import com.example.schedule.compose.view.model.activity.ScheduleActivityViewModel

class SettingsScreenViewModel (
    private val scheduleDBHelper: ScheduleDBHelper,
    private val scheduleActivityViewModel: ScheduleActivityViewModel,
    private val saves: SharedPreferences,
    private val activity: Activity
) : ViewModel() {
    var textSize by mutableStateOf(SettingsStorage.textSize)
        private set
    var displayModeFull by mutableStateOf(SettingsStorage.displayModeFull)
        private set
    var useServer by mutableStateOf(SettingsStorage.useServer)
        private set
    var showChooseThemeDialog by mutableStateOf(false)

    fun updateTheme(theme: Theme) {
        SettingsStorage.setTheme(theme, saves)
        ThemeManager.setTheme(theme)
        showChooseThemeDialog = false
    }

    fun updateTextSize(textSize: TextUnit) {
        SettingsStorage.saveTextSize(textSize, saves)
        this.textSize = textSize
        scheduleActivityViewModel.update()
    }

    fun updateDisplayModeFull(displayModeFull: Boolean) {
        SettingsStorage.saveDisplayMode(displayModeFull, saves)
        this.displayModeFull = displayModeFull
    }

    fun updateUseServer(useServer: Boolean) {
        SettingsStorage.saveUseServer(useServer, saves)
        this.useServer = useServer
    }

    fun finishActivity() {
        activity.finish()
    }

    fun clearDB() {
        scheduleDBHelper.clearData()
        activity.finish()
    }
}