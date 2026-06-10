package truck.project.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class DsColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val error: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textOnPrimary: Color,
    val divider: Color,
    val isLight: Boolean
)

val Noir = Color(0xFF030706)
val Denim = Color(0xFF20394A)
val Bone = Color(0xFFF9F5ED)
val SurfaceDark = Color(0xFF121417)
val SurfaceDarkVariant = Color(0xFF1E2125)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceLightVariant = Color(0xFFF3F4F6)

val DarkColors = DsColors(
    primary = Color(0xFF60A5FA), // Vibrant Sky Blue
    secondary = Color(0xFFFACC15), // McLaren Yellow/Gold
    background = Color(0xFF0F172A), // Slate 900 for modern deep feel
    surface = Color(0xFF1E293B), // Slate 800
    surfaceVariant = Color(0xFF334155), // Slate 700
    error = Color(0xFFFB7185), // Vibrant Rose
    textPrimary = Color(0xFFF8FAFC),
    textSecondary = Color(0xFF94A3B8),
    textOnPrimary = Color(0xFF0F172A),
    divider = Color(0xFF334155),
    isLight = false
)

val LightColors = DsColors(
    primary = Color(0xFF2563EB), // Richer Blue
    secondary = Color(0xFFF59E0B), // Vibrant McLaren Orange/Yellow
    background = Color(0xFFF1F5F9), // Cleaner Slate 50/100
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE2E8F0),
    error = Color(0xFFF43F5E),
    textPrimary = Color(0xFF1E293B),
    textSecondary = Color(0xFF64748B),
    textOnPrimary = Color.White,
    divider = Color(0xFFE2E8F0),
    isLight = true
)

val LocalDsColors = staticCompositionLocalOf { DarkColors }
