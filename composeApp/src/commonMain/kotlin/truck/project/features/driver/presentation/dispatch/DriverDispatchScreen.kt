package truck.project.features.driver.presentation.dispatch

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import truck.project.features.driver.presentation.trip.DriverTripViewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDispatchScreen(
    onBack: () -> Unit,
    onStartTrip: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var odometer by remember { mutableStateOf("") }
    val colors = DsTheme.colors
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.notificationMessage) {
        state.notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearNotification()
        }
    }

    LaunchedEffect(state.isDispatchComplete) {
        if (state.isDispatchComplete) {
            onStartTrip()
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
        // High-end background
        Image(
            painter = painterResource(Res.drawable.imagen5),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.1f else 0.3f
        )
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.8f), colors.background)
            )
        ))

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { 
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("INSPECCIÓN", style = DsTheme.typography.displayMedium.copy(fontSize = 24.sp), color = colors.textPrimary, fontWeight = FontWeight.Black)
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary)
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
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spectacular Logo Section
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 12.dp)) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = colors.secondary.copy(alpha = 0.1f * glowAlpha),
                        border = androidx.compose.foundation.BorderStroke(2.dp, colors.secondary.copy(alpha = glowAlpha))
                    ) {}
                    Image(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(65.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "PROTOCOLO DE SALIDA",
                    color = colors.secondary,
                    style = DsTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    "Verifica los sistemas antes de partir.",
                    color = colors.textSecondary,
                    style = DsTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Odometer Card (Premium White/Glass)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = colors.surface.copy(alpha = 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f)),
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("ODÓMETRO INICIAL (KM)", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(colors.textPrimary.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                                .border(2.dp, colors.primary, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("VALOR ACTUAL: ", color = colors.textSecondary, style = DsTheme.typography.bodyMedium)
                                BasicTextField(
                                    value = odometer,
                                    onValueChange = { odometer = it },
                                    textStyle = DsTheme.typography.headlineLarge.copy(color = colors.textPrimary, fontWeight = FontWeight.Black),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                var brakeChecked by remember { mutableStateOf(false) }
                var fuelChecked by remember { mutableStateOf(false) }
                var cargoChecked by remember { mutableStateOf(false) }

                // Matching the reference colors
                DispatchItem("Sistemas de Frenado", Icons.Default.Warning, Color(0xFFFB7185), brakeChecked) { brakeChecked = it }
                Spacer(modifier = Modifier.height(12.dp))
                DispatchItem("Nivel de Combustible", Icons.Default.LocalGasStation, Color(0xFF60A5FA), fuelChecked) { fuelChecked = it }
                Spacer(modifier = Modifier.height(12.dp))
                DispatchItem("Seguridad de Carga", Icons.Default.Inventory, Color(0xFF4ADE80), cargoChecked) { cargoChecked = it }

                Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth()) {
                    VolvoButton(
                        text = if (state.isLoading) DsTheme.strings.loading else "INICIAR RUTA OFICIAL",
                        onClick = { viewModel.startTrip(odometer.toDoubleOrNull() ?: 0.0) },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = colors.secondary,
                        enabled = !state.isLoading && brakeChecked && fuelChecked && cargoChecked && odometer.isNotBlank()
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DispatchItem(text: String, icon: ImageVector, iconColor: Color, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(20.dp),
        color = if (checked) iconColor.copy(alpha = 0.15f) else colors.surface.copy(alpha = 0.4f),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (checked) iconColor else colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, color = colors.textPrimary, modifier = Modifier.weight(1f), style = DsTheme.typography.bodyLarge, fontWeight = if (checked) FontWeight.Black else FontWeight.Bold)
            Icon(if (checked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = if (checked) iconColor else colors.textPrimary.copy(alpha = 0.2f), modifier = Modifier.size(28.dp))
        }
    }
}
