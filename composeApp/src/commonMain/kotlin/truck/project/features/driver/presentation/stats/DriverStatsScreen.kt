package truck.project.features.driver.presentation.stats

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.theme.DsTheme
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverStatsScreen(onBack: () -> Unit) {
    val colors = DsTheme.colors
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(painter = painterResource(Res.drawable.imagen3), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = if (colors.isLight) 0.15f else 0.4f)
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background))))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("MÉTRICAS", style = DsTheme.typography.headlineMedium.copy(fontSize = 20.sp), color = colors.textPrimary) } },
                    navigationIcon = { IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary) } },
                    actions = {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(end = 12.dp)) {
                            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = colors.secondary.copy(alpha = 0.1f * glowAlpha), border = androidx.compose.foundation.BorderStroke(1.dp, colors.secondary.copy(alpha = glowAlpha))) {}
                            Image(painter = painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(28.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp).verticalScroll(rememberScrollState())) {
                Text("RESUMEN DE EFICIENCIA", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.8f), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = 0.3f))) {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PUNTUACIÓN ECO-DRIVER", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                        Text("92/100", color = Color(0xFF4ADE80), style = DsTheme.typography.displayLarge.copy(fontSize = 44.sp), fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("EXCELENCIA OPERATIVA", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MetricCard("VEL. PROMEDIO", "72 km/h", Icons.Default.Speed, colors.primary, Modifier.weight(1f))
                    MetricCard("TIEMPO MOTOR", "142 hrs", Icons.Default.Timer, colors.secondary, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    MetricCard("CONSUMO", "28L/100km", Icons.Default.LocalGasStation, Color(0xFFEF4444), Modifier.weight(1f))
                    MetricCard("POTENCIA", "540 HP", Icons.Default.Bolt, Color(0xFF4ADE80), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))
                Surface(modifier = Modifier.fillMaxWidth().height(80.dp), shape = RoundedCornerShape(20.dp), color = colors.primary.copy(alpha = 0.2f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary)) {
                    Box(contentAlignment = Alignment.Center) { Text("OPTIMIZA TU CONDUCCIÓN CON VOLVO I-SEE", color = colors.textPrimary, style = DsTheme.typography.labelLarge, fontWeight = FontWeight.Black, letterSpacing = 1.sp) }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun MetricCard(label: String, value: String, icon: ImageVector, iconColor: Color, modifier: Modifier) {
    val colors = DsTheme.colors
    Surface(modifier = modifier, shape = RoundedCornerShape(24.dp), color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.6f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f))) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(value, color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
        }
    }
}
