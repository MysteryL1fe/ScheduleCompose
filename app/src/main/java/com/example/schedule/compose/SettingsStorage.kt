package com.example.schedule.compose

import android.content.SharedPreferences

object SettingsStorage {
    private const val VERSION = 1.00011f
    const val SCHEDULE_SAVES = "ScheduleSaves"

    /*var textSize: Int = 1
    var displayModeFull: Boolean = true
    var backendBaseUrl: String = "http://192.168.1.128:8080"
    var useServer: Boolean = true*/

    /*// Update text size from SharedPreferences
    fun updateTextSize(saves: SharedPreferences) {
        textSize = saves.getInt("textSize", 1)
    }

    // Save text size to SharedPreferences
    fun saveTextSize(textSize: Int, saves: SharedPreferences) {
        SettingsStorage.textSize = textSize
        val editor = saves.edit()
        editor.putInt("textSize", textSize)
        editor.apply()
    }*/

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
    fun getCurFlow(saves: SharedPreferences): IntArray {
        return intArrayOf(
            saves.getInt("flowLvl", 1),
            saves.getInt("course", 0),
            saves.getInt("group", 0),
            saves.getInt("subgroup", 0)
        )
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

    /*// Get the current theme
    fun getTheme(saves: SharedPreferences): Int {
        return saves.getInt("theme", 0)
    }

    // Save the theme
    fun setTheme(theme: Int, saves: SharedPreferences) {
        val editor = saves.edit()
        editor.putInt("theme", theme)
        editor.apply()
    }

    // Update display mode (full or not) from SharedPreferences
    fun updateDisplayMode(saves: SharedPreferences) {
        displayModeFull = saves.getBoolean("displayFull", true)
    }

    // Save the display mode
    fun saveDisplayMode(isFullDisplay: Boolean, saves: SharedPreferences) {
        displayModeFull = isFullDisplay
        val editor = saves.edit()
        editor.putBoolean("displayFull", isFullDisplay)
        editor.apply()
    }

    // Update use server flag from SharedPreferences
    fun updateUseServer(saves: SharedPreferences) {
        useServer = saves.getBoolean("useServer", true)
    }

    // Save the use server flag
    fun saveUseServer(useServer: Boolean, saves: SharedPreferences) {
        SettingsStorage.useServer = useServer
        val editor = saves.edit()
        editor.putBoolean("useServer", useServer)
        editor.apply()
    }*/
}
