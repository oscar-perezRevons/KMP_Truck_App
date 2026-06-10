package truck.project.features.admin.presentation.forms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import truck.project.core.ui.rememberImagePickerLauncher
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.components.TruckLoadingAnimation
import truck.project.designsystem.theme.DsTheme
import truck.project.core.platform.rememberLocationPermissionState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen3

@Composable
expect fun GoogleMapView(
    modifier: Modifier,
    onMapClick: (lat: Double, lng: Double) -> Unit = { _, _ -> },
    startPoint: Pair<Double, Double>? = null,
    endPoint: Pair<Double, Double>? = null,
    currentPoint: Pair<Double, Double>? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRouteScreen(
    onBack: () -> Unit,
    onAssign: () -> Unit,
    viewModel: NewRouteViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val colors = DsTheme.colors

    var permissionGranted by remember { mutableStateOf(false) }
    rememberLocationPermissionState { permissionGranted = true }

    val imagePicker = rememberImagePickerLauncher { bytes ->
        viewModel.onAddImage(bytes)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onAssign()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen3),
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
                    title = { },
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
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TruckLoadingAnimation()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 12.dp)) {
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = RoundedCornerShape(24.dp),
                            color = colors.textPrimary.copy(alpha = 0.05f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = if (state.isEditMode) "MODIFICAR\nRUTA" else "PLANIFICACIÓN\nDE RUTA",
                        color = colors.textPrimary, 
                        style = DsTheme.typography.displayMedium.copy(fontSize = 26.sp, lineHeight = 32.sp),
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .background(Color(0xFF4ADE80), RoundedCornerShape(2.dp))
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.8f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            
                            // Map Selection Area
                            Text("SELECCIONAR RUTA EN MAPA", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                            
                            GoogleMapView(
                                modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(20.dp)),
                                startPoint = if (state.startLat != null && state.startLng != null) Pair(state.startLat!!, state.startLng!!) else null,
                                endPoint = if (state.endLat != null && state.endLng != null) Pair(state.endLat!!, state.endLng!!) else null,
                                onMapClick = { lat, lng ->
                                    if (state.startLat == null) {
                                        viewModel.onStartPointSelected(lat, lng, "Lat: $lat, Lng: $lng")
                                    } else {
                                        viewModel.onEndPointSelected(lat, lng, "Lat: $lat, Lng: $lng")
                                    }
                                }
                            )

                            VolvoTextField(value = state.origin, onValueChange = viewModel::onOriginChanged, label = "UBICACIÓN ORIGEN", placeholder = "Planta / Terminal")
                            VolvoTextField(value = state.destination, onValueChange = viewModel::onDestinationChanged, label = "PUNTO DE DESTINO", placeholder = "Cliente / Punto de Entrega")

                            Text(
                                "MULTIMEDIA DE CARGA", 
                                color = colors.textSecondary, 
                                style = DsTheme.typography.labelSmall, 
                                fontWeight = FontWeight.ExtraBold, 
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.weight(1f)) {
                                    VolvoTextField(value = state.currentUrlInput, onValueChange = viewModel::onUrlInputChanged, label = "URL IMAGEN CARGA", placeholder = "https://...")
                                }
                                IconButton(
                                    onClick = viewModel::addUrl, 
                                    modifier = Modifier.padding(top = 8.dp).size(56.dp).background(Color(0xFF4ADE80).copy(alpha = 0.1f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF4ADE80))
                                }
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Surface(
                                    modifier = Modifier.size(80.dp).clickable { imagePicker() },
                                    shape = RoundedCornerShape(20.dp),
                                    color = colors.textPrimary.copy(alpha = 0.05f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF4ADE80))
                                    }
                                }
                                
                                androidx.compose.foundation.lazy.LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(state.images.size) { index ->
                                         Surface(
                                            modifier = Modifier.size(80.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4ADE80))
                                        ) {
                                            AsyncImage(model = state.images[index], contentDescription = null, contentScale = ContentScale.Crop)
                                        }
                                    }

                                    items(state.imageUrls.size) { index ->
                                        Surface(
                                            modifier = Modifier.size(80.dp),
                                            shape = RoundedCornerShape(20.dp),
                                            color = Color.Black.copy(alpha = 0.2f)
                                        ) {
                                            AsyncImage(model = state.imageUrls[index], contentDescription = null, contentScale = ContentScale.Crop)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (state.error != null) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = state.error!!, color = colors.error, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    VolvoButton(
                        text = if (state.isLoading) DsTheme.strings.loading else if (state.isEditMode) DsTheme.strings.save else "GUARDAR PLAN DE RUTA",
                        onClick = viewModel::assignTrip,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = colors.secondary,
                        enabled = !state.isLoading
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}
