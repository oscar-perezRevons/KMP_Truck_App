package truck.project.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class DsColors(
    val primary: Color,      // Volvo Blue
    val secondary: Color,    // Volvo Gold/Orange
    val background: Color,   // Deep Navy
    val surface: Color,      // Card Background
    val error: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val isLight: Boolean
)

val VolvoBlue = Color(0xFF215CC2)
val VolvoYellow = Color(0xFFFFB600)
val DeepNavy = Color(0xFF030712)
val SurfaceDark = Color(0xFF111827)

val DarkColors = DsColors(
    primary = VolvoBlue,
    secondary = VolvoYellow,
    background = DeepNavy,
    surface = SurfaceDark,
    error = Color(0xFFEF4444),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF9CA3AF),
    isLight = false
)

val LightColors = DarkColors // Forzamos Dark Theme por diseño de marca Volvo

val LocalDsColors = staticCompositionLocalOf { DarkColors }
