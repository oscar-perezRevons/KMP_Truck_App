package truck.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import truck.project.data.remote.FirebaseConfig
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.ThemeMode
import truck.project.navigation.NavGraph

@Composable
fun MainScreen() {
    val currentMode = ThemeMode.DARK
    val snackbarHostState = remember { SnackbarHostState() }
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

    DsTheme(mode = currentMode) {
        Scaffold(
            containerColor = DsTheme.colors.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                NavGraph(
                    navController = navController,
                    isMaintenanceMode = isMaintenanceMode,
                    onSkipMaintenance = { isMaintenanceMode = false }
                )
            }
        }
    }
}

@Composable
fun MaintenanceScreen(onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DsTheme.colors.error),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aplicación en mantenimiento",
            style = DsTheme.typography.heading,
            color = DsTheme.colors.background
        )
        Text(
            text = "Estamos trabajando para mejorar. Vuelve pronto.",
            style = DsTheme.typography.body,
            color = DsTheme.colors.background
        )
        Button(onClick = onSkip) {
            Text("Saltar Mantenimiento (Debug)")
        }
    }
}
