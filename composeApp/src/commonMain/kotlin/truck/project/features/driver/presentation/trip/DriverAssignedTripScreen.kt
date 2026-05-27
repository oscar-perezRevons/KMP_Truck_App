package truck.project.features.driver.presentation.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.VolvoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverAssignedTripScreen(
    onBack: () -> Unit,
    onStartInspection: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = LocalDsColors.current

    VolvoScaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("VIAJE ASIGNADO", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = colors.surface) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = VolvoYellow, modifier = Modifier.padding(8.dp))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Hola. Tu viaje está listo:", color = colors.textSecondary, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))

            // Route Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colors.surface.copy(alpha = 0.5f)
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(Color.DarkGray.copy(alpha = 0.3f)))
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Navigation, contentDescription = null, tint = VolvoYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("RUTA PROGRAMADA", color = VolvoYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        RoutePoint("ORIGEN", state.currentTrip?.origin ?: "Cargando...", colors.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        RoutePoint("DESTINO", state.currentTrip?.destination ?: "Cargando...", VolvoYellow)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("DOCUMENTOS DE RUTA", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(modifier = Modifier.size(64.dp), shape = RoundedCornerShape(12.dp), color = Color.White) {
                Box(contentAlignment = Alignment.Center) { Text("📄") }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Vehicle Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surface.copy(alpha = 0.3f)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(VolvoYellow.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Text("🚚", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("VEHÍCULO ASIGNADO", color = colors.textSecondary, fontSize = 10.sp)
                        Text("SCN-1145 · Scania R 500", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Text("LISTO", color = Color(0xFF4ADE80), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = "VER DETALLES E INICIAR",
                onClick = onStartInspection
            )
        }
    }
}

@Composable
fun RoutePoint(label: String, value: String, dotColor: Color) {
    val colors = LocalDsColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(dotColor, CircleShape))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, color = colors.textSecondary, fontSize = 10.sp)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
