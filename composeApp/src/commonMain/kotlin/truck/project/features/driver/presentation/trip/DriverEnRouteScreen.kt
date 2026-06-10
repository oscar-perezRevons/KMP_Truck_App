package truck.project.features.driver.presentation.trip

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.delay
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.core.ui.rememberImagePickerLauncher
import truck.project.core.ui.rememberCameraLauncher
import truck.project.features.admin.presentation.forms.GoogleMapView
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2
import kotlinproject.composeapp.generated.resources.imagen3
import kotlinproject.composeapp.generated.resources.imagen4
import kotlinproject.composeapp.generated.resources.imagen5

@Composable
fun TelemetryDisplayEditable(label: String, value: String, unit: String, color: Color, modifier: Modifier, onValueChange: (String) -> Unit) {
    val colors = DsTheme.colors
    var isEditing by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(value) }

    Surface(
        modifier = modifier.clickable { isEditing = true },
        shape = RoundedCornerShape(24.dp),
        color = colors.surface.copy(alpha = 0.8f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = colors.textSecondary.copy(alpha = 0.5f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            if (isEditing) {
                TextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    modifier = Modifier.width(80.dp),
                    singleLine = true,
                    textStyle = DsTheme.typography.displayMedium.copy(fontSize = 32.sp, color = color, textAlign = TextAlign.Center),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, 
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = color,
                        unfocusedIndicatorColor = color.copy(alpha = 0.5f)
                    )
                )
                IconButton(onClick = { onValueChange(textValue); isEditing = false }) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4ADE80))
                }
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(value, color = color, style = DsTheme.typography.displayMedium.copy(fontSize = 32.sp), fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(unit, color = colors.textSecondary.copy(alpha = 0.4f), style = DsTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 6.dp))
                }
            }
        }
    }
}

@Composable
fun TelemetryDisplay(label: String, value: String, unit: String, color: Color, modifier: Modifier) {
    val colors = DsTheme.colors
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = colors.surface.copy(alpha = 0.8f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = colors.textSecondary.copy(alpha = 0.5f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, color = color, style = DsTheme.typography.displayMedium.copy(fontSize = 32.sp), fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text(unit, color = colors.textSecondary.copy(alpha = 0.4f), style = DsTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 6.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverEnRouteScreen(
    onNotifyArrival: () -> Unit,
    onReportStop: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var expenseAmount by remember { mutableStateOf("") }
    var expensePhoto by remember { mutableStateOf<ByteArray?>(null) }
    var selectedCategory by remember { mutableStateOf(truck.project.features.driver.domain.model.ExpenseCategory.OTHER) }
    var customCategoryName by remember { mutableStateOf("") }
    
    var editingExpense by remember { mutableStateOf<truck.project.features.driver.domain.model.Expense?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    var reportedLocation by remember { mutableStateOf("") }

    val colors = DsTheme.colors

    val imagePicker = rememberImagePickerLauncher { bytes -> expensePhoto = bytes }
    val cameraLauncher = rememberCameraLauncher { bytes -> expensePhoto = bytes }

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )

    val backgroundImages = listOf(
        Res.drawable.imagen1,
        Res.drawable.imagen2,
        Res.drawable.imagen3,
        Res.drawable.imagen4,
        Res.drawable.imagen5
    )
    var currentBgIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(5000)
            currentBgIndex = (currentBgIndex + 1) % backgroundImages.size
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        // Automatic Background Slideshow
        AnimatedContent(
            targetState = backgroundImages[currentBgIndex],
            transitionSpec = { fadeIn(tween(1500)) togetherWith fadeOut(tween(1500)) }
        ) { targetRes ->
            Image(
                painter = painterResource(targetRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = if (colors.isLight) 0.15f else 0.4f
            )
        }
        
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.8f), colors.background))))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center) {
                            Surface(modifier = Modifier.size(52.dp), shape = CircleShape, color = Color(0xFF4ADE80).copy(alpha = 0.1f * glowAlpha), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = glowAlpha))) {}
                            
                            val photoModel = if (!state.driverProfile?.photoUrl.isNullOrBlank()) state.driverProfile?.photoUrl 
                                             else if (state.driverProfile?.photoUrls?.isNotEmpty() == true) state.driverProfile?.photoUrls?.first()
                                             else null
                            
                            if (photoModel != null) {
                                AsyncImage(model = photoModel, contentDescription = null, modifier = Modifier.size(36.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                            } else {
                                Image(painter = painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(32.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(shape = CircleShape, color = Color(0xFF4ADE80).copy(alpha = 0.15f), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))) {
                            Text("● ${DsTheme.strings.inRoute.uppercase()}", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), fontWeight = FontWeight.Black)
                        }
                    }
                    Text("GPS ACTIVO", color = colors.textSecondary.copy(alpha = 0.6f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = colors.surface.copy(alpha = 0.8f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
                        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("OBJETIVO ACTUAL", color = colors.primary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                                Text(state.currentTrip?.destination?.uppercase() ?: "---", color = colors.textPrimary, style = DsTheme.typography.headlineLarge.copy(fontSize = 24.sp), fontWeight = FontWeight.Black)
                                Text("ORIGEN: ${state.currentTrip?.origin}", color = colors.textSecondary, style = DsTheme.typography.labelMedium)
                            }
                            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = colors.primary.copy(alpha = 0.1f)) {
                                Icon(Icons.Default.Navigation, contentDescription = null, tint = colors.primary, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }
                }

                item {
                    // Real Map in Driver Screen
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        shape = RoundedCornerShape(32.dp),
                        color = Color.Black.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        state.currentTrip?.let { trip ->
                            GoogleMapView(
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)),
                                startPoint = if (trip.startLat != null) Pair(trip.startLat!!, trip.startLng ?: 0.0) else null,
                                endPoint = if (trip.endLat != null) Pair(trip.endLat!!, trip.endLng ?: 0.0) else null,
                                currentPoint = if (trip.currentLat != null) Pair(trip.currentLat!!, trip.currentLng ?: 0.0) else null
                            )
                        } ?: Box(contentAlignment = Alignment.Center) {
                            Text("Cargando Mapa...", color = colors.textSecondary)
                        }
                    }
                }

                item {
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = colors.surface.copy(alpha = 0.5f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("REPORTE DE UBICACIÓN", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.height(16.dp))
                            VolvoTextField(
                                value = reportedLocation,
                                onValueChange = { reportedLocation = it },
                                label = "LUGAR ACTUAL",
                                placeholder = "Ej: Cruce Machacamarca"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            VolvoButton(
                                text = "ENVIAR POSICIÓN",
                                onClick = { viewModel.updateLocation(reportedLocation); reportedLocation = "" },
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = colors.primary,
                                enabled = reportedLocation.isNotBlank()
                            )
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TelemetryDisplayEditable("VELOCIDAD", state.currentSpeed.toInt().toString(), "KM/H", colors.primary, Modifier.weight(1f)) {
                            viewModel.updateSpeed(it.toDoubleOrNull() ?: 0.0)
                        }
                        TelemetryDisplay("CARGA", "12.4", "TN", colors.textPrimary, Modifier.weight(1f))
                    }
                }

                item {
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = colors.surface.copy(alpha = 0.5f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("REGISTRAR GASTO", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Category Selection
                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val categories = truck.project.features.driver.domain.model.ExpenseCategory.entries
                                items(categories) { category ->
                                    val label = when(category) {
                                        truck.project.features.driver.domain.model.ExpenseCategory.FUEL -> DsTheme.strings.fuel
                                        truck.project.features.driver.domain.model.ExpenseCategory.TOLL -> DsTheme.strings.toll
                                        truck.project.features.driver.domain.model.ExpenseCategory.FOOD -> DsTheme.strings.food
                                        truck.project.features.driver.domain.model.ExpenseCategory.LODGING -> DsTheme.strings.lodging
                                        truck.project.features.driver.domain.model.ExpenseCategory.OTHER -> DsTheme.strings.others
                                    }
                                    FilterChip(
                                        selected = selectedCategory == category,
                                        onClick = { selectedCategory = category },
                                        label = { Text(label) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = colors.secondary,
                                            selectedLabelColor = Color.Black
                                        )
                                    )
                                }
                            }

                            if (selectedCategory == truck.project.features.driver.domain.model.ExpenseCategory.OTHER) {
                                Spacer(modifier = Modifier.height(8.dp))
                                VolvoTextField(
                                    value = customCategoryName,
                                    onValueChange = { customCategoryName = it },
                                    label = "TIPO DE GASTO",
                                    placeholder = "Ej: Reparación neumático"
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            VolvoTextField(value = expenseAmount, onValueChange = { expenseAmount = it }, label = "MONTO (Bs)", placeholder = "0.00")
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Surface(modifier = Modifier.size(64.dp).clickable { imagePicker() }, shape = RoundedCornerShape(20.dp), color = colors.textPrimary.copy(alpha = 0.1f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.2f))) {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (expensePhoto != null) AsyncImage(model = expensePhoto, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                        else Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = colors.textSecondary.copy(alpha = 0.5f))
                                    }
                                }
                                IconButton(onClick = { cameraLauncher() }, modifier = Modifier.size(60.dp).background(colors.primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))) {
                                    Icon(Icons.Default.Camera, contentDescription = null, tint = colors.primary)
                                }
                                Text(if (expensePhoto != null) "TICKET OK" else "SUBIR TICKET", style = DsTheme.typography.labelSmall, color = if (expensePhoto != null) Color(0xFF4ADE80) else colors.textSecondary.copy(alpha = 0.4f), fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            VolvoButton(
                                text = "REGISTRAR", 
                                onClick = { 
                                    viewModel.registerExpense(
                                        amount = expenseAmount.toDoubleOrNull() ?: 0.0, 
                                        category = selectedCategory.name, 
                                        note = if (selectedCategory == truck.project.features.driver.domain.model.ExpenseCategory.OTHER) customCategoryName else null,
                                        photoData = expensePhoto
                                    )
                                    expenseAmount = ""
                                    expensePhoto = null
                                    customCategoryName = ""
                                }, 
                                modifier = Modifier.fillMaxWidth(), 
                                containerColor = colors.secondary
                            )
                        }
                    }
                }

                // Expenses List
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("LISTA DE GASTOS", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Text("TOTAL: Bs ${state.totalExpenses}", color = colors.primary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (state.expenses.isEmpty()) {
                            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = colors.textPrimary.copy(alpha = 0.03f)) {
                                Text("No hay gastos registrados", modifier = Modifier.padding(20.dp), color = colors.textSecondary.copy(alpha = 0.5f), style = DsTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                items(state.expenses) { expense ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                editingExpense = expense
                                showEditDialog = true
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = colors.surface.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).background(colors.primary.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Receipt, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(expense.category.name, color = colors.textPrimary, style = DsTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text("${expense.timestamp.hour}:${expense.timestamp.minute}", color = colors.textSecondary, style = DsTheme.typography.labelSmall)
                            }
                            Text("Bs ${expense.amount}", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                            
                            IconButton(onClick = { viewModel.deleteExpense(expense.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = colors.error.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    VolvoButton(text = "NOTIFICAR LLEGADA", onClick = onNotifyArrival, modifier = Modifier.fillMaxWidth(), containerColor = Color(0xFF4ADE80))
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Edit Expense Dialog
        if (showEditDialog && editingExpense != null) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("EDITAR GASTO", style = DsTheme.typography.headlineMedium, color = colors.textPrimary) },
                text = {
                    var amount by remember { mutableStateOf(editingExpense!!.amount.toString()) }
                    Column {
                        VolvoTextField(value = amount, onValueChange = { amount = it }, label = "NUEVO MONTO (Bs)")
                        Spacer(modifier = Modifier.height(16.dp))
                        VolvoButton(
                            text = "GUARDAR CAMBIOS",
                            onClick = {
                                viewModel.updateExpense(editingExpense!!.copy(amount = amount.toDoubleOrNull() ?: 0.0))
                                showEditDialog = false
                            }
                        )
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("CANCELAR", color = colors.error) }
                },
                containerColor = colors.background,
                shape = RoundedCornerShape(28.dp)
            )
        }
    }
}
