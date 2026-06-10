package truck.project.features.driver.presentation.trip

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.theme.DsTheme
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2
import kotlinproject.composeapp.generated.resources.imagen3
import kotlinproject.composeapp.generated.resources.imagen4
import kotlinproject.composeapp.generated.resources.imagen5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverAssignedTripScreen(
    onBack: () -> Unit,
    onStartInspection: () -> Unit,
    onProfile: (String) -> Unit,
    onHistory: () -> Unit,
    onStats: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = DsTheme.colors

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
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
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background)
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
                    // spectacular Logo and Status
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center) {
                            Surface(
                                modifier = Modifier.size(52.dp),
                                shape = CircleShape,
                                color = colors.secondary.copy(alpha = 0.1f * glowAlpha),
                                border = androidx.compose.foundation.BorderStroke(1.dp, colors.secondary.copy(alpha = glowAlpha))
                            ) {}
                            Image(
                                painter = painterResource(Res.drawable.logo),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF4ADE80).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color(0xFF4ADE80), CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ONLINE", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                    
                    // Profile Menu
                    Surface(
                        modifier = Modifier.size(48.dp).clickable { 
                            state.loggedDriverId?.let { onProfile(it) }
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = colors.textPrimary.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.2f))
                    ) {
                        val photoUrl = state.driverProfile?.photoUrl
                        if (!photoUrl.isNullOrBlank()) {
                            AsyncImage(model = photoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, tint = colors.textPrimary, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.currentTrip == null && !state.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = colors.textPrimary.copy(alpha = 0.2f), modifier = Modifier.size(100.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("SISTEMA A LA ESPERA", color = colors.textPrimary, style = DsTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                        Text("No tienes misiones asignadas en este ciclo.", color = colors.textSecondary, style = DsTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                    }
                } else {
                    Text(
                        "CENTRAL DE DESPACHOS",
                        color = colors.secondary,
                        style = DsTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(
                        "MISIÓN ASIGNADA",
                        color = colors.textPrimary,
                        style = DsTheme.typography.displayMedium.copy(fontSize = 32.sp, lineHeight = 38.sp),
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Route Card with more style
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = colors.surface.copy(alpha = 0.95f),
                        border = androidx.compose.foundation.BorderStroke(2.dp, colors.primary.copy(alpha = 0.3f)),
                        shadowElevation = 12.dp
                    ) {
                        Column(modifier = Modifier.padding(28.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = colors.primary.copy(alpha = 0.1f)) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = colors.primary, modifier = Modifier.padding(8.dp))
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Column {
                                    Text("PUNTO DE RECOGIDA", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    Text(state.currentTrip?.origin ?: "---", color = colors.textPrimary, style = DsTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                                }
                            }
                            
                            Box(modifier = Modifier.padding(start = 19.dp).width(2.dp).height(50.dp).background(Brush.verticalGradient(listOf(colors.primary, Color(0xFF4ADE80)))))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color(0xFF4ADE80).copy(alpha = 0.1f)) {
                                    Icon(Icons.Default.Flag, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.padding(8.dp))
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Column {
                                    Text("DESTINO FINAL", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    Text(state.currentTrip?.destination ?: "---", color = colors.textPrimary, style = DsTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Truck Details
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = colors.surface.copy(alpha = 0.7f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f)),
                        shadowElevation = 6.dp
                    ) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = Color(0xFF1A1A1A)
                            ) {
                                val truckPhoto = when(state.assignedTruck?.imageUrl) {
                                    "imagen2" -> Res.drawable.imagen2
                                    "imagen3" -> Res.drawable.imagen3
                                    "imagen4" -> Res.drawable.imagen4
                                    "imagen5" -> Res.drawable.imagen5
                                    else -> Res.drawable.imagen1
                                }
                                Image(
                                    painter = painterResource(truckPhoto),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("UNIDAD OPERATIVA", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Text(
                                    state.assignedTruck?.plateNumber?.value ?: "---",
                                    color = colors.textPrimary,
                                    style = DsTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black
                                )
                                Surface(color = colors.primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                    Text(
                                        state.assignedTruck?.model?.uppercase() ?: "S/M", 
                                        color = colors.primary, 
                                        style = DsTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(modifier = Modifier.padding(bottom = 40.dp).fillMaxWidth()) {
                        val isEnRoute = state.currentTrip?.status == truck.project.features.driver.domain.model.TripStatus.EN_ROUTE
                        VolvoButton(
                            text = if (isEnRoute) "CONTINUAR RUTA OFICIAL" else "INICIAR PROTOCOLO DE SALIDA",
                            onClick = onStartInspection,
                            modifier = Modifier.fillMaxWidth().height(70.dp),
                            containerColor = if (isEnRoute) Color(0xFF22C55E) else colors.secondary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
