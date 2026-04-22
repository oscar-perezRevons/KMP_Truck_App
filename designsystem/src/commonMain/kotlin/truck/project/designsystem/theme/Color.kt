package truck.project.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class DsColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val error: Color,
    val textPrimary: Color,
    val isLight: Boolean
)

val LightColors = DsColors(
    primary = Color(0xFF0061A4),
    secondary = Color(0xFF535F70),
    background = Color(0xFFFDFBFF),
    error = Color(0xFFBA1A1A),
    textPrimary = Color(0xFF191C1E),
    isLight = true
)

val DarkColors = DsColors(
    primary = Color(0xFF9ECAFF),
    secondary = Color(0xFFBBC7DB),
    background = Color(0xFF191C1E),
    error = Color(0xFFFFB4AB),
    textPrimary = Color(0xFFE2E2E6),
    isLight = false
)

val LocalDsColors = staticCompositionLocalOf { LightColors }
