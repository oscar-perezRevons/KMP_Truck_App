package truck.project.features.admin.presentation.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.TruckLoadingAnimation
import truck.project.designsystem.theme.DsTheme
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.TruckStatus
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.trips.data.local.TripEntity
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2
import kotlinproject.composeapp.generated.resources.imagen3
import kotlinproject.composeapp.generated.resources.imagen4
import kotlinproject.composeapp.generated.resources.imagen5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel,
    onNavigateToFleet: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToTrips: () -> Unit,
    onNavigateToNewTruck: (Truck?) -> Unit,
    onNavigateToNewDriver: (Driver?) -> Unit,
    onNavigateToNewRoute: (String?, String?, String?) -> Unit,
    onNavigateToAssignTrip: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToShowroom: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState(initial = null)
    val colors = DsTheme.colors

    LaunchedEffect(effect) {
        val currentEffect = effect
        when (currentEffect) {
            AdminDashboardEffect.NavigateToFleet -> onNavigateToFleet()
            AdminDashboardEffect.NavigateToDrivers -> onNavigateToDrivers()
            AdminDashboardEffect.NavigateToTrips -> onNavigateToTrips()
            AdminDashboardEffect.NavigateToProfile -> onNavigateToProfile()
            AdminDashboardEffect.NavigateToLogin -> onLogout()
            is AdminDashboardEffect.NavigateToEditTruck -> onNavigateToNewTruck(currentEffect.truck) 
            is AdminDashboardEffect.NavigateToEditDriver -> onNavigateToNewDriver(currentEffect.driver)
            is AdminDashboardEffect.NavigateToEditTrip -> onNavigateToNewRoute(currentEffect.trip.id, currentEffect.trip.origin, currentEffect.trip.destination)
            null -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.12f else 0.25f
        )
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.85f), colors.background)
            )
        ))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = colors.secondary.copy(alpha = 0.1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.secondary.copy(alpha = 0.2f))
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.logo),
                                contentDescription = "Logo App",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        val titleParts = DsTheme.strings.controlPanel.split(" ")
                        Column {
                            Text(titleParts.first(), color = colors.textPrimary, style = DsTheme.typography.displayMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.Black)
                            if (titleParts.size > 1) {
                                Text(titleParts.drop(1).joinToString(" "), color = colors.secondary, style = DsTheme.typography.displayMedium.copy(fontSize = 32.sp), fontWeight = FontWeight.Black)
                            }
                        }
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            modifier = Modifier.size(52.dp).clickable { viewModel.onIntent(AdminDashboardIntent.NavigateToProfile) },
                            shape = RoundedCornerShape(16.dp),
                            color = colors.textPrimary.copy(alpha = 0.1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.2f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val photoUrl = state.admin?.profileImageUrl
                                if (!photoUrl.isNullOrBlank()) {
                                    AsyncImage(model = photoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                } else {
                                    Text(state.admin?.name?.take(1).orEmpty(), color = colors.textPrimary, style = DsTheme.typography.headlineMedium)
                                }
                            }
                        }
                        
                        IconButton(onClick = { viewModel.onIntent(AdminDashboardIntent.Logout) }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar Sesión", tint = colors.error)
                        }
                    }
                }
            }
        ) { padding ->
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TruckLoadingAnimation()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Feature Banner
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            shape = RoundedCornerShape(32.dp),
                            color = Color.Black
                        ) {
                            Box {
                                Image(
                                    painter = painterResource(Res.drawable.imagen2),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    alpha = 0.6f
                                )
                                Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Black, Color.Transparent))))
                                Column(modifier = Modifier.padding(24.dp).align(Alignment.CenterStart)) {
                                    Text("ESTADO DE LA", color = Color.White, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                                    Text("OPERACIÓN", color = Color.White, style = DsTheme.typography.displayMedium.copy(fontSize = 24.sp), fontWeight = FontWeight.Black)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).background(Color(0xFF4ADE80), CircleShape))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("${DsTheme.strings.systemActive}", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // High-End Stats Row
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatItemSmall(
                                Modifier.weight(1f).clickable { onNavigateToFleet() }, 
                                icon = Icons.Default.LocalShipping, 
                                value = state.truckCount.toString(), 
                                label = if (state.truckCount == 1) "Unidad" else "Unidades", 
                                color = colors.secondary,
                                gradient = Brush.verticalGradient(listOf(Color(0xFFFACC15), Color(0xFFCA8A04)))
                            )
                            StatItemSmall(
                                Modifier.weight(1f).clickable { onNavigateToDrivers() }, 
                                icon = Icons.Default.Person, 
                                value = state.driverCount.toString(), 
                                label = if (state.driverCount == 1) "Chofer" else "Choferes", 
                                color = Color(0xFF4ADE80),
                                gradient = Brush.verticalGradient(listOf(Color(0xFF4ADE80), Color(0xFF16A34A)))
                            )
                            StatItemSmall(
                                Modifier.weight(1.2f).clickable { onNavigateToTrips() }, 
                                icon = Icons.Default.AccountBalanceWallet,
                                value = state.todayExpenses.toInt().toString(), 
                                label = "Gastos hoy", 
                                color = colors.primary,
                                isCurrency = true,
                                gradient = Brush.verticalGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8)))
                            )
                        }
                    }

                    // Live Monitor Banner
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth().clickable { onNavigateToTrips() },
                            shape = RoundedCornerShape(28.dp),
                            color = colors.surface.copy(alpha = 0.8f),
                            border = androidx.compose.foundation.BorderStroke(2.dp, colors.secondary.copy(alpha = 0.4f)),
                            shadowElevation = 8.dp
                        ) {
                            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                                Surface(modifier = Modifier.size(52.dp), shape = CircleShape, color = colors.secondary.copy(alpha = 0.15f)) {
                                    Icon(Icons.Default.Radar, contentDescription = null, tint = colors.secondary, modifier = Modifier.padding(12.dp))
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(8.dp).background(Color(0xFF4ADE80), CircleShape))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("CENTRO DE MONITOREO", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                                    }
                                    val tripsLabel = if (state.activeTripsCount == 1) "viaje activo" else "viajes activos"
                                    Text("${state.activeTripsCount} $tripsLabel — EXPLORAR", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold)
                                }
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = colors.secondary)
                            }
                        }
                    }

                    // MI FLOTA Section
                    item {
                        Column {
                            SectionHeader(DsTheme.strings.fleet, onAddClick = { onNavigateToNewTruck(null) })
                            Text("Gestión de unidades y mantenimiento", color = colors.textSecondary.copy(alpha = 0.5f), style = DsTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.trucks.size) { index -> 
                                TruckCard(
                                    truck = state.trucks[index],
                                    onEdit = { viewModel.onIntent(AdminDashboardIntent.EditTruck(state.trucks[index])) },
                                    onDelete = { viewModel.onIntent(AdminDashboardIntent.DeleteTruck(state.trucks[index].id)) }
                                )
                            }
                            item { AddCard("ALTA CAMIÓN", onClick = { onNavigateToNewTruck(null) }) }
                        }
                    }

                    // REPORTS Section
                    item {
                        Column {
                            Text("CENTRO DE REPORTES", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                color = colors.surface.copy(alpha = 0.4f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    ReportButton("Exportar Rutas Maestras", Icons.Default.Description) { viewModel.onIntent(AdminDashboardIntent.GenerateTripsReport) }
                                    ReportButton("Inventario de Flota PDF", Icons.Default.LocalShipping) { viewModel.onIntent(AdminDashboardIntent.GenerateFleetReport) }
                                    ReportButton("Nómina de Operadores", Icons.Default.Groups) { viewModel.onIntent(AdminDashboardIntent.GenerateDriversReport) }
                                }
                            }
                        }
                    }

                    // CONDUCTORES Section
                    item {
                        Column {
                            SectionHeader(DsTheme.strings.drivers, onAddClick = { onNavigateToNewDriver(null) })
                            Text("Personal operativo y asignaciones", color = colors.textSecondary.copy(alpha = 0.5f), style = DsTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.drivers.size) { index ->
                                DriverCard(
                                    driver = state.drivers[index],
                                    onEdit = { viewModel.onIntent(AdminDashboardIntent.EditDriver(state.drivers[index])) },
                                    onDelete = { viewModel.onIntent(AdminDashboardIntent.DeleteDriver(state.drivers[index].id)) }
                                )
                            }
                            item { AddCard("ALTA CHOFER", onClick = { onNavigateToNewDriver(null) }) }
                        }
                    }

                    // Showroom shortcut with image
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(140.dp).clickable { onNavigateToShowroom() },
                            shape = RoundedCornerShape(32.dp),
                            color = Color.Black
                        ) {
                            Box {
                                Image(
                                    painter = painterResource(Res.drawable.imagen3),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    alpha = 0.5f
                                )
                                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black))))
                                Column(modifier = Modifier.padding(24.dp).align(Alignment.BottomStart)) {
                                    Text("UNIVERSO VOLVO", color = Color.White, style = DsTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                                    Text("Explora la ingeniería sueca", color = Color.White.copy(alpha = 0.6f), style = DsTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    // RUTAS ACTIVAS Section
                    item {
                        SectionHeader(DsTheme.strings.routes, onAddClick = { onNavigateToNewRoute(null, null, null) })
                        Spacer(modifier = Modifier.height(16.dp))
                        if (state.activeTrips.isEmpty() && state.plannedTrips.isEmpty()) {
                            EmptySectionCard("No hay rutas planificadas en este momento.")
                        } else {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                // Planned Trips (Need Assignment)
                                items(state.plannedTrips.size) { index ->
                                    val trip = state.plannedTrips[index]
                                    PlannedTripCard(
                                        trip = trip,
                                        onAssign = { onNavigateToAssignTrip(trip.id) },
                                        onDelete = { viewModel.onIntent(AdminDashboardIntent.DeleteTrip(trip.id)) }
                                    )
                                }
                                
                                // Active Trips
                                items(state.activeTrips.size) { index -> 
                                    val trip = state.activeTrips[index]
                                    ActiveTripCard(
                                        trip = trip,
                                        onEdit = { onNavigateToNewRoute(trip.id, trip.origin, trip.destination) },
                                        onDelete = { viewModel.onIntent(AdminDashboardIntent.DeleteTrip(trip.id)) }
                                    ) 
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(40.dp)) }
                }
            }
        }
        
        // Report Ready Dialog
        if (state.showReportDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onIntent(AdminDashboardIntent.DismissReportDialog) },
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = colors.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("REPORTE EXPORTADO", fontWeight = FontWeight.Black) 
                    }
                },
                text = { 
                    Column {
                        Text(state.reportMessage ?: "", style = DsTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("El archivo se ha guardado como documento de texto para asegurar compatibilidad total.", style = DsTheme.typography.labelSmall, color = colors.textSecondary)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { 
                            state.reportData?.let { viewModel.onIntent(AdminDashboardIntent.OpenReport(it)) }
                            viewModel.onIntent(AdminDashboardIntent.DismissReportDialog)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.secondary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("ABRIR DOCUMENTO", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onIntent(AdminDashboardIntent.DismissReportDialog) }) {
                        Text("ENTENDIDO", color = colors.textSecondary)
                    }
                },
                containerColor = colors.surface,
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 6.dp
            )
        }
        
        if (state.isGeneratingReport) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = colors.secondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(state.reportMessage ?: "Procesando...", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReportButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.fillMaxWidth().height(64.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = colors.textPrimary.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = colors.textPrimary, fontWeight = FontWeight.Bold, style = DsTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.Download, contentDescription = null, tint = colors.textSecondary.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun StatItemSmall(
    modifier: Modifier, 
    icon: ImageVector, 
    value: String, 
    label: String, 
    color: Color,
    isCurrency: Boolean = false,
    gradient: Brush? = null
) {
    val colors = DsTheme.colors
    Surface(
        modifier = modifier.height(130.dp), 
        shape = RoundedCornerShape(24.dp), 
        color = if (gradient != null) Color.Transparent else colors.surface.copy(alpha = 0.7f), 
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        shadowElevation = 8.dp
    ) {
        Box(modifier = if (gradient != null) Modifier.background(gradient) else Modifier) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(
                        icon, 
                        contentDescription = null, 
                        tint = Color.White, 
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Column {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isCurrency) {
                            Text(
                                text = "Bs.", 
                                color = Color.White.copy(alpha = 0.9f), 
                                style = DsTheme.typography.labelSmall.copy(fontSize = 11.sp), 
                                fontWeight = FontWeight.Bold, 
                                modifier = Modifier.padding(bottom = 4.dp, end = 2.dp)
                            )
                        }
                        Text(
                            text = value, 
                            color = Color.White, 
                            style = DsTheme.typography.displayMedium.copy(fontSize = 24.sp), 
                            fontWeight = FontWeight.Black,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = label.uppercase(), 
                        color = Color.White.copy(alpha = 0.8f), 
                        style = DsTheme.typography.labelSmall.copy(fontSize = 9.sp), 
                        fontWeight = FontWeight.ExtraBold, 
                        maxLines = 1, 
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onAddClick: () -> Unit) {
    val colors = DsTheme.colors
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
        TextButton(onClick = onAddClick) { 
            Text("+ ${DsTheme.strings.add}", color = colors.secondary, fontWeight = FontWeight.Black, style = DsTheme.typography.labelSmall) 
        }
    }
}

@Composable
fun TruckCard(truck: Truck, onEdit: () -> Unit, onDelete: () -> Unit) {
    val colors = DsTheme.colors
    val plate = truck.plateNumber.value
    val isAvailable = truck.status == TruckStatus.AVAILABLE
    
    Surface(
        modifier = Modifier.width(240.dp).height(240.dp).clickable { onEdit() },
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .background(Color(0xFF1A1A1A))
            ) {
                // Selección determinística del recurso de la carpeta drawable
                val imageRes = when(truck.imageUrl) {
                    "imagen2" -> Res.drawable.imagen2
                    "imagen3" -> Res.drawable.imagen3
                    "imagen4" -> Res.drawable.imagen4
                    "imagen5" -> Res.drawable.imagen5
                    else -> Res.drawable.imagen1
                }

                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradiente Volvo Blue sutil
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.4f to Color.Transparent,
                            1.0f to Color(0xFF001E4D).copy(alpha = 0.85f)
                        )
                    )
                ))

                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                    shape = CircleShape,
                    color = if (isAvailable) Color(0xFF4ADE80).copy(alpha = 0.9f) else colors.primary.copy(alpha = 0.9f),
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAvailable) "LIBRE" else "EN RUTA",
                            color = Color.White,
                            style = DsTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Text(
                    text = plate,
                    color = Color.White,
                    style = DsTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                )
            }
            
            Column(modifier = Modifier.background(colors.surfaceVariant).padding(14.dp).fillMaxWidth()) {
                Text(truck.model.uppercase(), color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Scale, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${truck.capacity} tn", color = colors.textSecondary, style = DsTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun DriverCard(driver: Driver, onEdit: () -> Unit, onDelete: () -> Unit) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.width(180.dp).height(240.dp).clickable { onEdit() },
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color(0xFF1A1A1A),
                    border = androidx.compose.foundation.BorderStroke(2.dp, colors.primary.copy(alpha = 0.2f))
                ) {
                    val photoModel = if (!driver.photoUrl.isNullOrBlank() && driver.photoUrl != "null") driver.photoUrl 
                                     else if (driver.photoUrls.isNotEmpty()) driver.photoUrls.first()
                                     else null
                    
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = driver.name.take(2).uppercase(), style = DsTheme.typography.headlineLarge, color = Color.White.copy(alpha = 0.2f))
                        
                        if (photoModel != null) {
                            AsyncImage(
                                model = photoModel, 
                                contentDescription = null, 
                                contentScale = ContentScale.Crop, 
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(48.dp))
                        }
                    }
                }
                Surface(
                    modifier = Modifier.size(20.dp).offset(x = (-2).dp, y = (-2).dp),
                    shape = CircleShape,
                    color = if (driver.isOnline) Color(0xFF4ADE80) else Color.Gray,
                    border = androidx.compose.foundation.BorderStroke(3.dp, colors.surface)
                ) {}
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = driver.name, color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, maxLines = 1)
            
            Surface(shape = RoundedCornerShape(8.dp), color = colors.textPrimary.copy(alpha = 0.05f)) {
                Text(text = "C.I.: ${driver.dni}", color = colors.textSecondary.copy(alpha = 0.6f), style = DsTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}

@Composable
fun PlannedTripCard(trip: TripEntity, onAssign: () -> Unit, onDelete: () -> Unit) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.width(280.dp).height(180.dp).clickable { onAssign() },
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary.copy(alpha = 0.3f))
    ) {
        Box {
            Image(
                painter = painterResource(Res.drawable.imagen3),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)))))
            
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                shape = CircleShape,
                color = colors.primary.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary)
            ) {
                Text("PENDIENTE", color = colors.primary, style = DsTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Black)
            }

            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(trip.destination.uppercase(), color = Color.White, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black, maxLines = 1)
                Text("De: ${trip.origin}", color = Color.White.copy(alpha = 0.6f), style = DsTheme.typography.labelSmall)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                VolvoButton(
                    text = "ASIGNAR RECURSOS",
                    onClick = onAssign,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    containerColor = colors.secondary
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.align(Alignment.TopStart).padding(8.dp).size(32.dp).background(Color.Red.copy(alpha = 0.4f), CircleShape)) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun ActiveTripCard(trip: TripEntity, onEdit: () -> Unit, onDelete: () -> Unit) {
    val colors = DsTheme.colors
    val allImages = remember(trip) {
        val list = mutableListOf<String>()
        trip.imageUrls?.split(",")?.filter { it.isNotBlank() }?.let { list.addAll(it) }
        if (list.isEmpty()) list.add("https://images.volvotrucks.com/latis/Image?f=P&id=16302&v=1&t=1690450531&c=0x0:7680x4320&s=1920")
        list
    }
    
    var currentImageIndex by remember { mutableStateOf(0) }
    if (allImages.size > 1) {
        LaunchedEffect(trip.id) {
            while(true) { delay(5000); currentImageIndex = (currentImageIndex + 1) % allImages.size }
        }
    }

    Surface(
        modifier = Modifier.width(280.dp).height(180.dp).clickable { onEdit() },
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Box(modifier = Modifier.background(Color(0xFF1A1A1A))) {
            AnimatedContent(targetState = allImages[currentImageIndex], modifier = Modifier.fillMaxSize()) { img ->
                AsyncImage(model = img, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
            
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                shape = CircleShape,
                color = Color(0xFF4ADE80),
            ) {
                Text("● ${DsTheme.strings.live}", color = Color.White, style = DsTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontWeight = FontWeight.Black)
            }

            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(trip.destination.uppercase(), color = Color.White, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black, maxLines = 1, modifier = Modifier.weight(1f))
                    if (trip.currentSpeed > 0) {
                        Surface(shape = RoundedCornerShape(4.dp), color = colors.secondary) {
                            Text("${trip.currentSpeed.toInt()} km/h", color = Color.Black, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }
                Text("De: ${trip.origin}", color = Color.White.copy(alpha = 0.6f), style = DsTheme.typography.labelSmall)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { trip.progress / 100f },
                        modifier = Modifier.weight(1f).height(6.dp).clip(CircleShape),
                        color = colors.secondary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("${trip.progress}%", color = Color.White, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                }
            }

            IconButton(onClick = onDelete, modifier = Modifier.align(Alignment.TopStart).padding(8.dp).size(32.dp).background(Color.Red.copy(alpha = 0.4f), CircleShape)) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun AddCard(label: String, onClick: () -> Unit) {
    val colors = DsTheme.colors
    Surface(modifier = Modifier.width(160.dp).height(240.dp).clickable { onClick() }, shape = RoundedCornerShape(24.dp), color = Color.Transparent, border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Add, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, textAlign = TextAlign.Center, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun EmptySectionCard(message: String) {
    val colors = DsTheme.colors
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = colors.surface.copy(alpha = 0.3f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))) {
        Text(message, modifier = Modifier.padding(24.dp), color = colors.textSecondary, textAlign = TextAlign.Center, style = DsTheme.typography.bodyMedium)
    }
}
