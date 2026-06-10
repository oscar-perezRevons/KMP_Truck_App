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
                    val titleParts = DsTheme.strings.controlPanel.split(" ")
                    Column {
                        Text(titleParts.first(), color = colors.textPrimary, style = DsTheme.typography.displayMedium.copy(fontSize = 28.sp), fontWeight = FontWeight.Black)
                        if (titleParts.size > 1) {
                            Text(titleParts.drop(1).joinToString(" "), color = colors.secondary, style = DsTheme.typography.displayMedium.copy(fontSize = 36.sp), fontWeight = FontWeight.Black)
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
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = colors.error)
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
                            StatItemSmall(Modifier.weight(1f), icon = Icons.Default.LocalShipping, value = state.truckCount.toString(), label = DsTheme.strings.activeTrucks, color = colors.secondary)
                            StatItemSmall(Modifier.weight(1f), icon = Icons.Default.Person, value = state.driverCount.toString(), label = DsTheme.strings.drivers, color = Color(0xFF4ADE80))
                            StatItemSmall(Modifier.weight(1.2f), icon = Icons.Default.AttachMoney, value = "Bs ${state.todayExpenses}", label = DsTheme.strings.todayExpenses, color = colors.primary)
                        }
                    }

                    // Live Monitor Banner
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth().clickable { onNavigateToTrips() },
                            shape = RoundedCornerShape(24.dp),
                            color = colors.surface.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.secondary.copy(alpha = 0.3f))
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = colors.secondary.copy(alpha = 0.1f)) {
                                    Icon(Icons.Default.SettingsInputAntenna, contentDescription = null, tint = colors.secondary, modifier = Modifier.padding(10.dp))
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(6.dp).background(colors.secondary, CircleShape))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(DsTheme.strings.live, color = colors.secondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                                    }
                                    Text("${state.activeTripsCount} ${DsTheme.strings.tripsInProgress} — Ver monitor", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
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
                title = { Text("REPORTE GENERADO", fontWeight = FontWeight.Black) },
                text = { Text(state.reportMessage ?: "") },
                confirmButton = {
                    TextButton(onClick = { 
                        state.reportData?.let { viewModel.onIntent(AdminDashboardIntent.OpenReport(it)) }
                        viewModel.onIntent(AdminDashboardIntent.DismissReportDialog)
                    }) {
                        Text("ABRIR PDF", color = colors.secondary, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onIntent(AdminDashboardIntent.DismissReportDialog) }) {
                        Text("ENTENDIDO")
                    }
                },
                containerColor = colors.surface,
                shape = RoundedCornerShape(28.dp)
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
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = colors.textPrimary.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = colors.textPrimary, fontWeight = FontWeight.Bold, style = DsTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.Download, contentDescription = null, tint = colors.textSecondary.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun StatItemSmall(modifier: Modifier, icon: ImageVector, value: String, label: String, color: Color) {
    val colors = DsTheme.colors
    Surface(modifier = modifier, shape = RoundedCornerShape(20.dp), color = colors.surface.copy(alpha = 0.6f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color.copy(alpha = 0.8f), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = color, style = DsTheme.typography.displayMedium.copy(fontSize = 22.sp), fontWeight = FontWeight.Black)
            Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
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
        modifier = Modifier.width(240.dp).height(210.dp).clickable { onEdit() },
        shape = RoundedCornerShape(24.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                if (!truck.imageUrl.isNullOrBlank()) {
                    AsyncImage(model = truck.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Image(painter = painterResource(Res.drawable.imagen1), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)))))

                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.4f),
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(if (isAvailable) Color(0xFF4ADE80) else colors.primary, CircleShape))
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
            
            Column(modifier = Modifier.background(colors.surfaceVariant.copy(alpha = 0.3f)).padding(16.dp).fillMaxWidth()) {
                Text(truck.model.uppercase(), color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("${truck.capacity} ton", color = colors.textSecondary, style = DsTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun DriverCard(driver: Driver, onEdit: () -> Unit, onDelete: () -> Unit) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.width(180.dp).height(210.dp).clickable { onEdit() },
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
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = colors.primary.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, colors.primary.copy(alpha = 0.2f))
                ) {
                    val photoModel = if (!driver.photoUrl.isNullOrBlank()) driver.photoUrl 
                                     else if (driver.photoUrls.isNotEmpty()) driver.photoUrls.first()
                                     else null
                    
                    if (photoModel != null) {
                        AsyncImage(model = photoModel, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().clip(CircleShape))
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = driver.name.take(2).uppercase(), style = DsTheme.typography.headlineLarge, color = colors.primary)
                        }
                    }
                }
                Surface(
                    modifier = Modifier.size(16.dp).offset(x = (-2).dp, y = (-2).dp),
                    shape = CircleShape,
                    color = if (driver.isOnline) Color(0xFF4ADE80) else Color.Gray,
                    border = androidx.compose.foundation.BorderStroke(2.dp, colors.surface)
                ) {}
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = driver.name, color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
            
            Surface(shape = RoundedCornerShape(8.dp), color = colors.textPrimary.copy(alpha = 0.05f)) {
                Text(text = "ID: ${driver.dni}", color = colors.textSecondary.copy(alpha = 0.6f), style = DsTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
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
                    modifier = Modifier.fillMaxWidth().height(40.dp),
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
        Box {
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
    Surface(modifier = Modifier.width(140.dp).height(180.dp).clickable { onClick() }, shape = RoundedCornerShape(24.dp), color = Color.Transparent, border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Add, contentDescription = null, tint = colors.textSecondary)
            Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, textAlign = TextAlign.Center)
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
