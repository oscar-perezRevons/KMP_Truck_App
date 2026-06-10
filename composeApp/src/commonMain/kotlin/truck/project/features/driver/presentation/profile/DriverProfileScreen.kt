package truck.project.features.driver.presentation.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Logout
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
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.features.driver.presentation.trip.DriverTripViewModel
import org.koin.compose.viewmodel.koinViewModel
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: DriverTripViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val colors = DsTheme.colors

    LaunchedEffect(state.driverProfile) {
        state.driverProfile?.let { name = it.name }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(animation = tween(1500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(painter = painterResource(Res.drawable.imagen4), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = if (colors.isLight) 0.12f else 0.3f)
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background))))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(DsTheme.strings.profile, style = DsTheme.typography.headlineMedium, color = colors.textPrimary) } },
                    navigationIcon = { IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary) } },
                    actions = { IconButton(onClick = { isEditing = !isEditing }) { Icon(if (isEditing) Icons.Default.Close else Icons.Default.Edit, contentDescription = null, tint = colors.textPrimary) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp).verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(top = 12.dp)) {
                    Surface(modifier = Modifier.size(130.dp), shape = RoundedCornerShape(36.dp), color = colors.textPrimary.copy(alpha = 0.1f), border = androidx.compose.foundation.BorderStroke(3.dp, if (isEditing) colors.secondary else colors.textPrimary.copy(alpha = 0.2f * glowAlpha))) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.driverProfile?.photoUrl != null) AsyncImage(model = state.driverProfile?.photoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            else Image(painter = painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(75.dp))
                        }
                    }
                    if (!isEditing) Surface(modifier = Modifier.size(30.dp).padding(4.dp), shape = CircleShape, color = Color(0xFF4ADE80), border = androidx.compose.foundation.BorderStroke(2.dp, colors.background)) {}
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isEditing) {
                    VolvoTextField(value = name, onValueChange = { name = it }, label = "NOMBRE COMPLETO")
                    Spacer(modifier = Modifier.height(16.dp))
                    VolvoTextField(value = password, onValueChange = { password = it }, label = "NUEVA CONTRASEÑA")
                    Spacer(modifier = Modifier.height(32.dp))
                    VolvoButton(text = DsTheme.strings.save, onClick = { viewModel.updateProfile(name, if (password.isBlank()) null else password, null); isEditing = false }, modifier = Modifier.fillMaxWidth(), containerColor = colors.primary)
                } else {
                    Text(name.uppercase(), color = colors.textPrimary, style = DsTheme.typography.displayMedium.copy(fontSize = 28.sp, lineHeight = 34.sp), fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                    Text("OPERADOR CERTIFICADO", color = colors.secondary, style = DsTheme.typography.subHeading, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(40.dp))
                    DriverInfoBox("DETALLES TÉCNICOS") {
                        DriverRowInfo("DNI / RUT", state.driverProfile?.dni ?: "---")
                        DriverRowInfo("LICENCIA", state.driverProfile?.licenseNumber ?: "---")
                        DriverRowInfo("STATUS", "HABILITADO")
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(containerColor = colors.error.copy(alpha = 0.1f)), border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.error)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = colors.error)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(DsTheme.strings.logout, color = colors.error, style = DsTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DriverInfoBox(title: String, content: @Composable ColumnScope.() -> Unit) {
    val colors = DsTheme.colors
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), color = colors.textPrimary.copy(alpha = 0.05f), border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(title, color = colors.primary, style = DsTheme.typography.subHeading, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(20.dp)); content()
        }
    }
}

@Composable
fun DriverRowInfo(label: String, value: String) {
    val colors = DsTheme.colors
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(value, color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
