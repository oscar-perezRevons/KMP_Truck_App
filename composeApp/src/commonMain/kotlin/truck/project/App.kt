package truck.project

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import truck.project.core.data.remote.FirebaseConfig
import truck.project.designsystem.theme.DsTheme
import truck.project.navigation.NavGraph

@Composable
fun App() {
    val navController = rememberNavController()
    val firebaseConfig = koinInject<FirebaseConfig>()
    
    var isMaintenanceMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firebaseConfig.fetchAndActivate { success ->
            if (success) {
                isMaintenanceMode = firebaseConfig.getString("is_maintenance") == "true"
            }
        }
    }

    DsTheme {
        NavGraph(
            navController = navController,
            isMaintenanceMode = isMaintenanceMode,
            onSkipMaintenance = { isMaintenanceMode = false }
        )
    }
}
