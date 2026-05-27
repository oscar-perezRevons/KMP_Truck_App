package truck.project.features.driver.presentation.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.VolvoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverEnRouteScreen(
    onNotifyArrival: () -> Unit,
    onReportStop: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = LocalDsColors.current
    var expenseAmount by remember { mutableStateOf("100") }
    var selectedCategory by remember { mutableStateOf("Combustible") }

    VolvoScaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF4ADE80), CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EN RUTA", color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                }
                Text("10:54", color = colors.textSecondary)
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
            // Active Destination Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surface.copy(alpha = 0.5f)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("DESTINO ACTIVO", color = colors.textSecondary, fontSize = 10.sp)
                        Text(state.currentTrip?.destination ?: "Santa Cruz", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("${state.currentTrip?.origin} → ${state.currentTrip?.destination}", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    Box(modifier = Modifier.size(48.dp).background(VolvoYellow.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = VolvoYellow)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Map Placeholder
            Surface(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.DarkGray.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("MAPA EN TIEMPO REAL", color = colors.textSecondary)
                    Text("23 km rest.", color = Color.White, modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            VolvoButton(
                text = "REPORTAR PARADA",
                onClick = onReportStop,
                containerColor = Color.Black.copy(alpha = 0.5f),
                contentColor = VolvoYellow
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Expense Registration Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surface.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AttachMoney, contentDescription = null, tint = colors.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("REGISTRAR GASTO", color = colors.primary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text("CATEGORÍA", color = colors.textSecondary, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = colors.background,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(selectedCategory, color = Color.White)
                            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = colors.textSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("MONTO ($)", color = colors.textSecondary, fontSize = 10.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    VolvoTextField(value = expenseAmount, onValueChange = { expenseAmount = it }, label = "")

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TextButton(onClick = { expenseAmount = "" }, modifier = Modifier.weight(1f)) {
                            Text("Limpiar", color = colors.textSecondary)
                        }
                        Button(
                            onClick = { viewModel.registerExpense(expenseAmount.toDoubleOrNull() ?: 0.0, "Fuel") }, // Simplified category
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                        ) {
                            Text("Guardar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = "▼ NOTIFICAR LLEGADA",
                onClick = onNotifyArrival,
                containerColor = Color(0xFFEF4444)
            )
        }
    }
}
