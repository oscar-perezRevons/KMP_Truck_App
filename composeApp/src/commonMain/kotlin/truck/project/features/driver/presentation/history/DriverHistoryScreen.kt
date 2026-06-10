package truck.project.features.driver.presentation.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.theme.DsTheme
import truck.project.features.driver.presentation.trip.DriverTripViewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverHistoryScreen(
    onBack: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = DsTheme.colors

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(painter = painterResource(Res.drawable.imagen2), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = if (colors.isLight) 0.15f else 0.4f)
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background))))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("HISTORIAL", style = DsTheme.typography.headlineMedium.copy(fontSize = 20.sp), color = colors.textPrimary) } },
                    navigationIcon = { IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary) } },
                    actions = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(end = 12.dp)) {
                            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = colors.primary.copy(alpha = 0.1f * glowAlpha), border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary.copy(alpha = glowAlpha))) {}
                            Image(painter = painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(28.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.8f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
                    Row(modifier = Modifier.padding(24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        HistoryMetricBox("TOTAL KM", "1,240", colors.primary)
                        HistoryMetricBox("VIAJES", "14", Color(0xFF4ADE80))
                        HistoryMetricBox("EFICIENCIA", "94%", colors.secondary)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("ACTIVIDAD RECIENTE", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(5) { index ->
                        TripHistoryCardBox("LUNES, 24 MAR", "PLANTA CENTRAL", "TERMINAL NORTE", "FINALIZADO")
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun HistoryMetricBox(label: String, value: String, color: Color) {
    val colors = DsTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, style = DsTheme.typography.headlineLarge, fontWeight = FontWeight.Black)
        Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TripHistoryCardBox(date: String, origin: String, destination: String, status: String) {
    val colors = DsTheme.colors
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = colors.textPrimary.copy(alpha = 0.05f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(colors.primary.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Timeline, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(date, color = colors.textSecondary, style = DsTheme.typography.labelSmall)
                Text("$origin → $destination", color = colors.textPrimary, fontWeight = FontWeight.Bold, style = DsTheme.typography.bodyLarge)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF4ADE80).copy(alpha = 0.1f), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))) {
                Text(status, color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}
