package truck.project.features.admin.presentation.forms

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.theme.DsTheme
import truck.project.features.fleet.domain.model.TruckStatus
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignTripScreen(
    tripId: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AssignTripViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = DsTheme.colors

    LaunchedEffect(tripId) {
        viewModel.loadTrip(tripId)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onSuccess()
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.15f else 0.4f
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
                    title = { Text("ASIGNACIÓN", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            if (state.selectedTrip == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.secondary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Trip Info
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = colors.surface.copy(alpha = 0.8f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("RUTA PLANIFICADA", color = colors.primary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                            Text("${state.selectedTrip?.origin} → ${state.selectedTrip?.destination}", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                        }
                    }

                    // Resource Selection
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = colors.surface.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            
                            // Driver Select
                            Text("OPERADOR ASIGNADO", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
                            var driverExpanded by remember { mutableStateOf(false) }
                            val selectedDriver = state.drivers.find { it.id == state.selectedDriverId }
                            
                            Box {
                                OutlinedButton(
                                    onClick = { driverExpanded = true },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(selectedDriver?.name ?: "ELEGIR CONDUCTOR", style = DsTheme.typography.bodyLarge)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                }
                                DropdownMenu(expanded = driverExpanded, onDismissRequest = { driverExpanded = false }, modifier = Modifier.fillMaxWidth(0.8f)) {
                                    state.drivers.forEach { driver ->
                                        DropdownMenuItem(
                                            text = { Text(driver.name) },
                                            onClick = { viewModel.onDriverSelected(driver.id); driverExpanded = false }
                                        )
                                    }
                                }
                            }

                            // Truck Select
                            Text("UNIDAD DE TRANSPORTE", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
                            var truckExpanded by remember { mutableStateOf(false) }
                            val selectedTruck = state.trucks.find { it.id == state.selectedTruckId }
                            
                            Box {
                                OutlinedButton(
                                    onClick = { truckExpanded = true },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(selectedTruck?.let { "${it.plateNumber.value} - ${it.model}" } ?: "ELEGIR CAMIÓN", style = DsTheme.typography.bodyLarge)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                }
                                DropdownMenu(expanded = truckExpanded, onDismissRequest = { truckExpanded = false }, modifier = Modifier.fillMaxWidth(0.8f)) {
                                    state.trucks.filter { it.status == truck.project.features.fleet.domain.model.TruckStatus.AVAILABLE || it.id == state.selectedTruckId }.forEach { truck ->
                                        DropdownMenuItem(
                                            text = { Text("${truck.plateNumber.value} - ${truck.model}") },
                                            onClick = { viewModel.onTruckSelected(truck.id); truckExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (state.error != null) {
                        Text(state.error!!, color = colors.error, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    VolvoButton(
                        text = if (state.isLoading) "PROCESANDO..." else "CONFIRMAR DESPACHO",
                        onClick = viewModel::confirmAssignment,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = colors.secondary,
                        enabled = !state.isLoading && state.selectedDriverId != null && state.selectedTruckId != null
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}
