package truck.project.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

object DsTheme {
    val colors: DsColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDsColors.current

    val typography: DsTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalDsTypography.current

    val spacing: DsSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalDsSpacing.current

    val strings: DsStrings
        @Composable
        @ReadOnlyComposable
        get() = LocalDsStrings.current
}

@Composable
fun DsTheme(
    mode: ThemeMode = ThemeMode.SYSTEM,
    language: AppLanguage = AppLanguage.ES,
    content: @Composable () -> Unit
) {
    val darkTheme = when (mode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colors = if (darkTheme) DarkColors else LightColors
    val strings = if (language == AppLanguage.EN) EnStrings else EsStrings

    CompositionLocalProvider(
        LocalDsColors provides colors,
        LocalDsTypography provides Typography,
        LocalDsSpacing provides DsSpacing(),
        LocalDsStrings provides strings,
        content = content
    )
}
