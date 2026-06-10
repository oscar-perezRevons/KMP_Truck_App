package truck.project.features.driver.presentation.trip

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverCloseTripScreen(
    onBack: () -> Unit,
    onFinish: () -> Unit,
    viewModel: DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var endOdometer by remember { mutableStateOf("") }
    val colors = DsTheme.colors

    LaunchedEffect(state.isTripFinished) {
        if (state.isTripFinished) onFinish()
    }

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(painter = painterResource(Res.drawable.imagen1), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = if (colors.isLight) 0.15f else 0.4f)
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background))))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("CIERRE DE MISIÓN", style = DsTheme.typography.headlineMedium.copy(fontSize = 20.sp), color = colors.textPrimary) } },
                    navigationIcon = { IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 12.dp)) {
                    Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = Color(0xFF4ADE80).copy(alpha = 0.1f * glowAlpha), border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4ADE80).copy(alpha = glowAlpha))) {}
                    Image(painter = painterResource(Res.drawable.logo), contentDescription = "Logo", modifier = Modifier.size(70.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), color = Color(0xFF4ADE80).copy(alpha = 0.15f), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80))) {
                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text("PUNTO DE LLEGADA", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                            Text("Confirma los datos de cierre.", color = colors.textPrimary.copy(alpha = 0.7f), style = DsTheme.typography.bodyLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(32.dp), color = colors.surface.copy(alpha = 0.8f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("ODÓMETRO FINAL (KM)", color = colors.textSecondary.copy(alpha = 0.4f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        VolvoTextField(value = endOdometer, onValueChange = { endOdometer = it }, label = "LECTURA DE CIERRE", placeholder = "000000")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("FIRMA DIGITAL", color = colors.textSecondary.copy(alpha = 0.5f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
                    Text("LIMPIAR", color = colors.primary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, modifier = Modifier.clickable { })
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(modifier = Modifier.fillMaxWidth().height(180.dp).background(colors.textPrimary.copy(alpha = 0.05f), RoundedCornerShape(32.dp)).border(1.dp, colors.textPrimary.copy(alpha = 0.1f), RoundedCornerShape(32.dp)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = colors.textPrimary.copy(alpha = 0.1f), modifier = Modifier.size(64.dp))
                        Text("ÁREA DE CONFORMIDAD", color = colors.textSecondary.copy(alpha = 0.2f), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth()) {
                    VolvoButton(
                        text = if (state.isLoading) DsTheme.strings.loading else "FINALIZAR MISIÓN",
                        onClick = { viewModel.finishTrip(endOdometer.toDoubleOrNull() ?: 0.0, "sign_ok") },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = colors.secondary,
                        enabled = !state.isLoading && endOdometer.isNotBlank()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
