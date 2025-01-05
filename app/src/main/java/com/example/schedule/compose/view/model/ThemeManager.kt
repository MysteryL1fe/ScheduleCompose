package com.example.schedule.compose.view.model

import com.example.schedule.compose.ui.theme.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ThemeManager {
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> get() = _theme

    fun setTheme(theme: Theme) {
        _theme.value = theme
    }
}