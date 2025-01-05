package com.example.schedule.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TextDark
)

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    primary = PrimaryLight,
    secondary = SecondaryLight,
    tertiary = TextLight
)

@Composable
fun ScheduleComposeTheme(
    theme: Theme = Theme.SYSTEM,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        Theme.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

enum class Theme {
    SYSTEM,
    DARK,
    LIGHT
}