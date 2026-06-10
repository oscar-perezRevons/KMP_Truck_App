package truck.project.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import truck.project.core.data.remote.FirebaseConfig
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.ThemeMode
import truck.project.navigation.NavGraph
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoButton
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.imagen4

@Composable
fun MainScreen() {
    var currentThemeMode by remember { mutableStateOf(ThemeMode.DARK) }
    var currentLanguage by remember { mutableStateOf(truck.project.designsystem.theme.AppLanguage.ES) }
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

    DsTheme(
        mode = currentThemeMode,
        language = currentLanguage
    ) {
        Scaffold(
            containerColor = DsTheme.colors.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                NavGraph(
                    navController = navController,
                    isMaintenanceMode = isMaintenanceMode,
                    onSkipMaintenance = { isMaintenanceMode = false },
                    onToggleTheme = {
                        currentThemeMode = if (currentThemeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
                    },
                    onToggleLanguage = {
                        currentLanguage = if (currentLanguage == truck.project.designsystem.theme.AppLanguage.ES) 
                            truck.project.designsystem.theme.AppLanguage.EN 
                        else 
                            truck.project.designsystem.theme.AppLanguage.ES
                    }
                )
            }
        }
    }
}

@Composable
fun MaintenanceScreen(onSkip: () -> Unit) {
    val colors = DsTheme.colors
    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen4),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, colors.background))))
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("⚒️", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "MANTENIMIENTO TÉCNICO",
                color = colors.primary,
                style = DsTheme.typography.labelSmall.copy(fontSize = 11.sp),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Text(
                text = "SISTEMA FUERA DE LÍNEA",
                color = colors.textPrimary,
                style = DsTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Estamos optimizando los motores de despacho. Volveremos a la ruta en unos minutos.",
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                style = DsTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(48.dp))
            VolvoButton(
                text = "REINTENTAR ACCESO",
                onClick = onSkip,
                containerColor = colors.primary
            )
        }
    }
}
