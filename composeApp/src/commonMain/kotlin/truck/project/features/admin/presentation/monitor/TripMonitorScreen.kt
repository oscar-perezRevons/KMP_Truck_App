package truck.project.features.admin.presentation.monitor

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.theme.DsTheme
import truck.project.features.admin.presentation.dashboard.AdminDashboardViewModel
import truck.project.features.admin.presentation.dashboard.AdminDashboardState
import truck.project.features.admin.presentation.dashboard.AdminDashboardIntent
import truck.project.features.admin.presentation.forms.GoogleMapView
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2
import kotlinproject.composeapp.generated.resources.imagen4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripMonitorScreen(
    onBack: () -> Unit,
    viewModel: AdminDashboardViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val colors = DsTheme.colors
    
    // Multi-trip support
    var currentTripIndex by remember { mutableStateOf(0) }
    val activeTrip = state.activeTrips.getOrNull(currentTripIndex)

    LaunchedEffect(activeTrip?.id) {
        activeTrip?.id?.let { 
            viewModel.onIntent(AdminDashboardIntent.SelectTripForMonitor(it)) 
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.1f else 0.2f
        )
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.8f), colors.background)
            )
        ))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("MONITOR EN VIVO", style = DsTheme.typography.displayMedium.copy(fontSize = 20.sp), color = colors.textPrimary, fontWeight = FontWeight.Black)
                            if (state.activeTrips.size > 1) {
                                Surface(
                                    modifier = Modifier.padding(start = 12.dp),
                                    shape = CircleShape,
                                    color = colors.primary.copy(alpha = 0.1f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary)
                                ) {
                                    Text("${currentTripIndex + 1} / ${state.activeTrips.size}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = colors.primary, style = DsTheme.typography.labelSmall)
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                        }
                    },
                    actions = {
                        if (state.activeTrips.size > 1) {
                            IconButton(onClick = { currentTripIndex = (currentTripIndex + 1) % state.activeTrips.size }) {
                                Icon(Icons.Default.SkipNext, contentDescription = "Siguiente Viaje", tint = colors.primary)
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4ADE80).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80)),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                             Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF4ADE80), CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("LIVE", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                             }
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
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (activeTrip == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CloudOff, contentDescription = null, tint = colors.textSecondary.copy(alpha = 0.2f), modifier = Modifier.size(100.dp))
                            Text("NO HAY VIAJES ACTIVOS", color = colors.textSecondary, style = DsTheme.typography.headlineMedium)
                        }
                    }
                } else {
                    // Operador Activo Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = colors.surface.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(contentAlignment = Alignment.Center) {
                                Surface(modifier = Modifier.size(52.dp), shape = CircleShape, color = colors.secondary.copy(alpha = 0.1f)) {
                                    val driver = state.drivers.find { it.id == activeTrip.driverId }
                                    if (driver?.photoUrl != null) {
                                        AsyncImage(model = driver.photoUrl, contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                                    } else {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = colors.secondary, modifier = Modifier.padding(12.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("CONDUCTOR ACTIVO", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Text(state.drivers.find { it.id == activeTrip.driverId }?.name ?: "OPERADOR", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                                val truck = state.trucks.find { it.id == activeTrip.truckId }
                                Text("${truck?.plateNumber?.value ?: "---"} • ${truck?.model ?: "Volvo"}", color = colors.secondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Actualizado", color = colors.textSecondary, style = DsTheme.typography.labelSmall)
                                Text("16:18", color = Color(0xFF4ADE80), fontWeight = FontWeight.Black, style = DsTheme.typography.bodyLarge)
                            }
                        }
                    }

                    // Route Progress Visual
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        shape = RoundedCornerShape(28.dp),
                        color = Color.Black.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        if (activeTrip != null) {
                            GoogleMapView(
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(28.dp)),
                                startPoint = if (activeTrip.startLat != null) Pair(activeTrip.startLat!!, activeTrip.startLng ?: 0.0) else null,
                                endPoint = if (activeTrip.endLat != null) Pair(activeTrip.endLat!!, activeTrip.endLng ?: 0.0) else null,
                                currentPoint = if (activeTrip.currentLat != null) Pair(activeTrip.currentLat!!, activeTrip.currentLng ?: 0.0) else null
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Map, contentDescription = null, tint = colors.textPrimary.copy(alpha = 0.1f), modifier = Modifier.size(64.dp))
                                    Text("GEOLOCALIZACIÓN SATELITAL", color = colors.textPrimary.copy(alpha = 0.2f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                                }
                            }
                        }
                    }

                    // Telemetry & Details
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = colors.surface.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Progreso del Trayecto", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                Text("${activeTrip.progress}%", color = colors.secondary, style = DsTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { activeTrip.progress / 100f },
                                modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                                color = colors.secondary,
                                trackColor = colors.textPrimary.copy(alpha = 0.1f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            activeTrip.currentLocation?.let {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = colors.primary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ubicación Reportada: $it", color = colors.textSecondary, style = DsTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    // Tabs
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = colors.textPrimary.copy(alpha = 0.05f)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            MonitorTabItem(text = "DETALLES", isSelected = selectedTab == 0, modifier = Modifier.weight(1f)) { selectedTab = 0 }
                            MonitorTabItem(text = "GASTOS VIVO", isSelected = selectedTab == 1, modifier = Modifier.weight(1f)) { selectedTab = 1 }
                        }
                    }

                    if (selectedTab == 0) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                            item { MonitorInfoRow("PUNTO DE SALIDA", activeTrip.origin, colors.primary) }
                            item { MonitorInfoRow("PUNTO DE LLEGADA", activeTrip.destination, colors.secondary) }
                            item { 
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    TelemetryCard(icon = Icons.Default.Speed, value = "${activeTrip.currentSpeed.toInt()} km/h", label = "Velocidad", modifier = Modifier.weight(1f))
                                    TelemetryCard(icon = Icons.Default.Timer, value = "1:42 hr", label = "En marcha", modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                            if (state.selectedTripExpenses.isEmpty()) {
                                item { 
                                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                        Text("Sin gastos reportados", color = colors.textSecondary.copy(alpha = 0.5f))
                                    }
                                }
                            }
                            items(state.selectedTripExpenses) { expense ->
                                LiveExpenseRow(expense)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonitorTabItem(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val colors = DsTheme.colors
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.secondary else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (isSelected) Color.Black else colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
    }
}

@Composable
fun MonitorInfoRow(label: String, value: String, color: Color) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall)
                Text(value.uppercase(), color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun TelemetryCard(icon: ImageVector, value: String, label: String, modifier: Modifier) {
    val colors = DsTheme.colors
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = colors.surface.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
            Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall)
        }
    }
}

@Composable
fun LiveExpenseRow(expense: truck.project.features.driver.domain.model.Expense) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.4f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = colors.primary.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Receipt, contentDescription = null, tint = colors.primary, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.category.name, color = colors.textPrimary, style = DsTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text("${expense.timestamp.hour}:${expense.timestamp.minute}", color = colors.textSecondary, style = DsTheme.typography.labelSmall)
            }
            Text("$${expense.amount}", color = colors.secondary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
        }
    }
}
