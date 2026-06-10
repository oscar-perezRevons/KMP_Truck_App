package truck.project.features.auth.presentation.login

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    state: AdminLoginState,
    onIntent: (AdminLoginIntent) -> Unit
) {
    val itemsVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        itemsVisible.value = true
    }

    var passwordVisible by remember { mutableStateOf(false) }

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
                            onClick = { onIntent(AdminLoginIntent.BackClicked) },
                            modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            AnimatedVisibility(
                visible = itemsVisible.value,
                enter = fadeIn(tween(1000)) + slideInHorizontally(tween(1000)) { -50 }
            ) {
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
                            modifier = Modifier.size(110.dp),
                            shape = CircleShape,
                            color = colors.primary.copy(alpha = 0.1f * glowAlpha),
                            border = androidx.compose.foundation.BorderStroke(2.dp, colors.primary.copy(alpha = glowAlpha))
                        ) {}
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(75.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "ACCESO\nADMINISTRADOR",
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
                            .background(colors.primary, RoundedCornerShape(2.dp))
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
                                value = state.email,
                                onValueChange = { onIntent(AdminLoginIntent.EmailChanged(it)) },
                                label = DsTheme.strings.email,
                                placeholder = "admin@truckflow.com"
                            )
                            state.emailError?.let {
                                Text(text = it, color = colors.error, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            VolvoTextField(
                                value = state.password,
                                onValueChange = { onIntent(AdminLoginIntent.PasswordChanged(it)) },
                                label = DsTheme.strings.password,
                                placeholder = "••••••••",
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = colors.textSecondary.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            )
                            state.passwordError?.let {
                                Text(text = it, color = colors.error, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                            }
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
                                text = it,
                                color = colors.error,
                                style = DsTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    VolvoButton(
                        text = if (state.isLoading) DsTheme.strings.loading else DsTheme.strings.login,
                        onClick = { onIntent(AdminLoginIntent.LoginClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = colors.secondary,
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        onClick = { onIntent(AdminLoginIntent.RegisterClicked) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "¿SIN CUENTA? REGÍSTRATE AQUÍ",
                            color = colors.textSecondary.copy(alpha = 0.7f),
                            style = DsTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
