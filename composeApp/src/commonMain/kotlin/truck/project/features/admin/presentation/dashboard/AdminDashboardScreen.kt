package truck.project.features.admin.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.VolvoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel,
    onNavigateToFleet: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToTrips: () -> Unit,
    onNavigateToNewTruck: () -> Unit,
    onNavigateToNewDriver: () -> Unit,
    onNavigateToNewRoute: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState(initial = null)
    val colors = LocalDsColors.current

    LaunchedEffect(effect) {
        when (effect) {
            AdminDashboardEffect.NavigateToFleet -> onNavigateToFleet()
            AdminDashboardEffect.NavigateToDrivers -> onNavigateToDrivers()
            AdminDashboardEffect.NavigateToTrips -> onNavigateToTrips()
            AdminDashboardEffect.NavigateToLogin -> onLogout()
            null -> {}
        }
    }

    VolvoScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "BIENVENIDO DE VUELTA",
                        color = colors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "PANEL DE",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "CONTROL",
                        color = VolvoYellow,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = colors.surface
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = colors.textSecondary
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    "● SISTEMA ACTIVO · MIÉRCOLES, 27 MAY",
                    color = Color(0xFF4ADE80),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.LocalShipping,
                        value = state.truckCount.toString(),
                        label = "Activos",
                        color = VolvoYellow
                    )
                    StatItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Person,
                        value = state.driverCount.toString(),
                        label = "Conductores",
                        color = Color(0xFF4ADE80)
                    )
                    StatItem(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.AttachMoney,
                        value = "$97.50",
                        label = "Gastos hoy",
                        color = Color(0xFF60A5FA)
                    )
                }
            }

            // Live Monitor Card
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTrips() },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VolvoYellow.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(VolvoYellow.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = VolvoYellow, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "● EN VIVO",
                                color = VolvoYellow,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "1 viaje en curso — Ver monitor",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = VolvoYellow)
                    }
                }
            }

            // Mi Flota Section
            item {
                SectionHeader("MI FLOTA", onAddClick = onNavigateToNewTruck)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        TruckCard(
                            plate = "SCN-1145",
                            model = "SCANIA R 500",
                            status = "EN VIAJE"
                        )
                    }
                    item {
                        AddCard("Registrar Camión", onNavigateToNewTruck)
                    }
                }
            }

            // Conductores Section
            item {
                SectionHeader("CONDUCTORES", onAddClick = onNavigateToNewDriver)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        DriverCircleCard("Carlos Rodríguez")
                    }
                    item {
                        DriverCircleCard("Ana Martínez")
                    }
                    item {
                        AddCircleCard("Nuevo Chofer", onNavigateToNewDriver)
                    }
                }
            }

            // Acciones Rápidas
            item {
                Text("ACCIONES RÁPIDAS", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        text = "NUEVA RUTA",
                        icon = Icons.Default.Navigation,
                        color = Color(0xFF059669),
                        onClick = onNavigateToNewRoute
                    )
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        text = "MONITOR",
                        icon = Icons.Default.Timeline,
                        color = VolvoYellow,
                        onClick = onNavigateToTrips
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    val colors = LocalDsColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(label, color = colors.textSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
fun SectionHeader(title: String, onAddClick: () -> Unit) {
    val colors = LocalDsColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(
            "+ Agregar",
            color = VolvoYellow,
            fontSize = 12.sp,
            modifier = Modifier.clickable { onAddClick() }
        )
    }
}

@Composable
fun TruckCard(plate: String, model: String, status: String) {
    Surface(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.05f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.Gray.copy(alpha = 0.2f))
            ) {
                 Text("🚛", modifier = Modifier.align(Alignment.Center), fontSize = 40.sp)
                 Text(
                    status,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp)
                 )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(plate, color = Color.White, fontWeight = FontWeight.Bold)
                Text(model, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DriverCircleCard(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1) + name.split(" ").last().take(1), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(name.split(" ").first(), color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun AddCard(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = VolvoYellow)
            Text(label, color = VolvoYellow, fontSize = 12.sp)
        }
    }
}

@Composable
fun AddCircleCard(label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .clickable { onClick() }
                .background(Color.Transparent)
                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White.copy(alpha = 0.5f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}
