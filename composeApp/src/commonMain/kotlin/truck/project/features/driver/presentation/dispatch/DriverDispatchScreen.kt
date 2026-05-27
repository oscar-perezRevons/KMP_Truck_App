package truck.project.features.driver.presentation.dispatch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.LocalDsColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDispatchScreen(
    onBack: () -> Unit,
    onStartTrip: () -> Unit,
    viewModel: truck.project.features.driver.presentation.trip.DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var odometer by remember { mutableStateOf("10") }
    val colors = LocalDsColors.current

    LaunchedEffect(state.isDispatchComplete) {
        if (state.isDispatchComplete) {
            onStartTrip()
        }
    }

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { Text("DESPACHO E INSPECCIÓN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                "Completa la inspección pre-viaje antes de salir.",
                color = colors.textSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            VolvoTextField(
                value = odometer,
                onValueChange = { odometer = it },
                label = "ODÓMETRO INICIAL (KM)"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("LISTA DE VERIFICACIÓN", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            CheckItem("¿Frenos en buen estado?", Icons.Default.Warning, Color(0xFFEF4444))
            Spacer(modifier = Modifier.height(12.dp))
            CheckItem("¿Combustible suficiente?", Icons.Default.LocalGasStation, Color(0xFF10B981))
            Spacer(modifier = Modifier.height(12.dp))
            CheckItem("¿Carga asegurada ok?", Icons.Default.Work, Color(0xFF10B981))

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = if (state.isLoading) "INICIANDO..." else "INICIAR VIAJE",
                onClick = { viewModel.startTrip(odometer.toDoubleOrNull() ?: 0.0) },
                containerColor = Color(0xFF10B981)
            )
        }
    }
}

@Composable
fun CheckItem(text: String, icon: ImageVector, iconColor: Color) {
    val colors = LocalDsColors.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = iconColor.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, iconColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, color = Color.White, modifier = Modifier.weight(1f))
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = iconColor)
        }
    }
}
