package truck.project.features.driver.presentation.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverPinScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: truck.project.features.driver.presentation.trip.DriverTripViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) onLoginSuccess()
    }

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val colors = DsTheme.colors

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.15f else 0.4f
        )
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spectacular Logo Section
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 20.dp)) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = colors.secondary.copy(alpha = 0.1f * glowAlpha),
                        border = androidx.compose.foundation.BorderStroke(2.dp, colors.secondary.copy(alpha = glowAlpha))
                    ) {}
                    Image(
                        painter = painterResource(Res.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "BIENVENIDO\nCONDUCTOR",
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
                        .background(colors.secondary, RoundedCornerShape(2.dp))
                )
                
                Spacer(modifier = Modifier.height(48.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        VolvoTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = DsTheme.strings.email,
                            placeholder = "conductor@truckflow.com"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        VolvoTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = DsTheme.strings.password,
                            placeholder = "••••••••",
                            visualTransformation = if (showPassword) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = colors.textSecondary.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        )
                    }
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.error.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.error)
                    ) {
                        Text(
                            it, 
                            color = colors.error, 
                            style = DsTheme.typography.bodyMedium, 
                            fontWeight = FontWeight.Bold, 
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                VolvoButton(
                    text = if (state.isLoading) DsTheme.strings.loading else DsTheme.strings.login,
                    onClick = { viewModel.loginWithCredentials(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = colors.secondary,
                    enabled = !state.isLoading
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    "v2.5.0 • Terminal Operativa",
                    color = colors.textSecondary.copy(alpha = 0.5f),
                    style = DsTheme.typography.labelSmall,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
