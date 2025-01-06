package com.example.schedule.compose.utils

import android.content.SharedPreferences
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.schedule.compose.entity.Flow
import com.example.schedule.compose.theme.Theme

object SettingsStorage {
    private const val VERSION = 1.00011f
    const val SCHEDULE_SAVES = "ScheduleSaves"

    var textSize: TextUnit = 1.sp
        private set
    var displayModeFull = false
        private set
    var useServer = true
        private set

    fun init(saves: SharedPreferences) {
        textSize = saves.getInt("textSize", 16).sp
        displayModeFull = saves.getBoolean("displayModeFull", false)
        useServer = saves.getBoolean("useServer", true)
    }

    fun saveTextSize(textSize: TextUnit, saves: SharedPreferences) {
        val editor = saves.edit()
        editor.putInt("textSize", textSize.value.toInt())
        editor.apply()
        SettingsStorage.textSize = textSize
    }

    // Get current version from SharedPreferences
    fun getCurVersion(saves: SharedPreferences): Float {
        return saves.getFloat("version", 0f)
    }

    // Check if the current version is the last version
    fun isLastVersion(saves: SharedPreferences): Boolean {
        return VERSION == getCurVersion(saves)
    }

    // Set the current version to the last version
    fun changeToLastVersion(saves: SharedPreferences) {
        val editor = saves.edit()
        editor.putFloat("version", VERSION)
        editor.apply()
    }

    // Get the current flow data
    fun getCurFlow(saves: SharedPreferences): Flow {
        return Flow(saves.getInt("flowLvl", 1), saves.getInt("course", 0), saves.getInt("group", 0), saves.getInt("subgroup", 0))
    }

    // Save the current flow data
    fun saveCurFlow(flowLvl: Int, course: Int, group: Int, subgroup: Int, saves: SharedPreferences) {
        val editor = saves.edit()
        editor.putInt("flowLvl", flowLvl)
        editor.putInt("course", course)
        editor.putInt("group", group)
        editor.putInt("subgroup", subgroup)
        editor.apply()
    }

    // Get the current theme
    fun getTheme(saves: SharedPreferences): Theme {
        return Theme.valueOf(saves.getString("theme", "SYSTEM")!!)
    }

    // Save the theme
    fun setTheme(theme: Theme, saves: SharedPreferences) {
        val editor = saves.edit()
        editor.putString("theme", theme.name)
        editor.apply()
    }

    fun saveDisplayMode(displayModeFull: Boolean, saves: SharedPreferences) {
        SettingsStorage.displayModeFull = displayModeFull
        val editor = saves.edit()
        editor.putBoolean("displayModeFull", displayModeFull)
        editor.apply()
    }

    fun saveUseServer(useServer: Boolean, saves: SharedPreferences) {
        SettingsStorage.useServer = useServer
        val editor = saves.edit()
        editor.putBoolean("useServer", useServer)
        editor.apply()
    }
}
