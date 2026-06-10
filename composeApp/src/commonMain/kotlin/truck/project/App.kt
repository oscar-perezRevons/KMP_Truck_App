package truck.project

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import truck.project.core.data.remote.FirebaseConfig
import truck.project.designsystem.theme.AppLanguage
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.ThemeMode
import truck.project.navigation.NavGraph

@Composable
fun App() {
    val navController = rememberNavController()
    val firebaseConfig = koinInject<FirebaseConfig>()
    
    var isMaintenanceMode by remember { mutableStateOf(false) }
    var currentThemeMode by remember { mutableStateOf(ThemeMode.DARK) }
    var currentLanguage by remember { mutableStateOf(AppLanguage.ES) }

    LaunchedEffect(Unit) {
        firebaseConfig.fetchAndActivate { success ->
            if (success) {
                isMaintenanceMode = firebaseConfig.getString("is_maintenance") == "true"
            }
        }
    }

    DsTheme(
        mode = currentThemeMode,
        language = currentLanguage
    ) {
        NavGraph(
            navController = navController,
            isMaintenanceMode = isMaintenanceMode,
            onSkipMaintenance = { isMaintenanceMode = false },
            onToggleTheme = {
                currentThemeMode = if (currentThemeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
            },
            onToggleLanguage = {
                currentLanguage = if (currentLanguage == AppLanguage.ES) AppLanguage.EN else AppLanguage.ES
            }
        )
    }
}
