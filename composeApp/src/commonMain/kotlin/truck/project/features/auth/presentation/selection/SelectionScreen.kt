package truck.project.features.auth.presentation.selection

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2
import kotlinproject.composeapp.generated.resources.imagen3
import kotlinproject.composeapp.generated.resources.imagen4
import kotlinproject.composeapp.generated.resources.imagen5

@Composable
fun SelectionScreen(
    onSelectDriver: () -> Unit,
    onSelectAdmin: () -> Unit,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit = {}
) {
    val itemsVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        itemsVisible.value = true
    }

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
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

    val colors = DsTheme.colors

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        // Dynamic Background Slideshow
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

        // Main Gradient Overlay
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = if (colors.isLight) {
                    listOf(Color.Transparent, colors.background.copy(alpha = 0.6f), colors.background)
                } else {
                    listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background)
                }
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
                    // Status Badge
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4ADE80).copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4ADE80).copy(alpha = 0.4f))
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFF4ADE80), CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SISTEMA ACTIVO", color = Color(0xFF4ADE80), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = onToggleLanguage,
                            modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = "Language",
                                tint = colors.textPrimary
                            )
                        }

                        IconButton(
                            onClick = onToggleTheme,
                            modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                if (colors.isLight) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = "Theme",
                                tint = colors.textPrimary
                            )
                        }
                    }
                }
            }
        ) { padding ->
            AnimatedVisibility(
                visible = itemsVisible.value,
                enter = fadeIn(tween(1000)) + slideInVertically(tween(1000)) { 40 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Centered Logo with McLaren Glow
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            color = colors.secondary.copy(alpha = 0.1f * glowAlpha),
                            border = androidx.compose.foundation.BorderStroke(2.dp, colors.secondary.copy(alpha = glowAlpha))
                        ) {}
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(90.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("TRUCK")
                            withStyle(style = SpanStyle(color = colors.secondary)) {
                                append("FLOW")
                            }
                        },
                        color = colors.textPrimary,
                        style = DsTheme.typography.displayMedium.copy(fontSize = 36.sp),
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "GESTIÓN DE FLOTA INTELIGENTE",
                        color = colors.textSecondary,
                        style = DsTheme.typography.subHeading.copy(fontSize = 11.sp),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp).background(
                            Brush.horizontalGradient(listOf(Color.Transparent, colors.divider, Color.Transparent))
                        )
                    )
                    Text(
                        "SELECCIONA TU PERFIL",
                        color = colors.textSecondary,
                        style = DsTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 16.dp),
                        letterSpacing = 1.5.sp
                    )

                    // Profile Selection Buttons
                    SelectionCard(
                        title = "SOY ${DsTheme.strings.driver}",
                        subtitle = "Acceso con correo y contraseña",
                        icon = Icons.Default.LocalShipping,
                        color = colors.secondary,
                        onClick = onSelectDriver
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SelectionCard(
                        title = "SOY ${DsTheme.strings.admin}",
                        subtitle = "Acceso con correo y contraseña",
                        icon = Icons.Default.BarChart,
                        color = colors.primary,
                        onClick = onSelectAdmin
                    )
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    Text(
                        text = "v2.5.0 • TruckFlow Inc.",
                        color = colors.textSecondary.copy(alpha = 0.5f),
                        style = DsTheme.typography.labelSmall,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SelectionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = color.copy(alpha = 0.15f)
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.padding(12.dp))
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    style = DsTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = subtitle,
                    color = colors.textSecondary,
                    style = DsTheme.typography.labelMedium
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = color.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
