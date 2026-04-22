package com.example.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val primary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val isLight: Boolean
)


val LightPalette = AppColors(
    primary = Color(0xFF6200EE),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFF000000),
    isLight = true
)

val DarkPalette = AppColors(
    primary = Color(0xFFBB86FC),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    textPrimary = Color(0xFFFFFFFF),
    isLight = false
)

val HighContrastPalette = AppColors(
    primary = Color(0xFFFFFF00),
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    textPrimary = Color(0xFFFFFFFF),
    isLight = false
)
