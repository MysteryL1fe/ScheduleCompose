package com.example.schedule.compose.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ThemeManager {
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> get() = _theme

    fun setTheme(theme: Theme) {
        _theme.value = theme
    }
}