package truck.project.features.admin.presentation.showroom

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolvoShowroomScreen(onBack: () -> Unit) {
    val colors = DsTheme.colors
    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.15f else 0.3f
        )
        
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("UNIVERSO VOLVO", color = colors.textPrimary, style = DsTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)) {
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Image
                Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                    Image(
                        painter = painterResource(Res.drawable.imagen3),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, colors.background)
                                )
                            )
                    )
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)
                    ) {
                        Text("VOLVO ELITE", color = colors.primary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                        Text("EL FUTURO DE LA LOGÍSTICA", color = colors.textPrimary, style = DsTheme.typography.displayMedium.copy(fontSize = 26.sp, lineHeight = 32.sp), fontWeight = FontWeight.Black)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Catalog section
                Text(
                    "TECNOLOGÍA SUECA", 
                    color = colors.textSecondary, 
                    style = DsTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold, 
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                val models = listOf(
                    TruckModel("Volvo FH16", Res.drawable.imagen1, "Potencia extrema para largas distancias."),
                    TruckModel("Volvo FMX", Res.drawable.imagen2, "Resistencia total en terrenos difíciles."),
                    TruckModel("Volvo FM", Res.drawable.imagen3, "La versatilidad redefinida.")
                )

                models.forEach { model ->
                    ShowroomModelCard(model)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ShowroomModelCard(model: TruckModel) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.2f)
            ) {
                Image(
                    painter = painterResource(model.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(model.name, color = colors.textPrimary, fontWeight = FontWeight.Black, style = DsTheme.typography.bodyLarge)
                Text(model.desc, color = colors.textSecondary, style = DsTheme.typography.labelMedium)
            }
        }
    }
}

data class TruckModel(val name: String, val imageRes: org.jetbrains.compose.resources.DrawableResource, val desc: String)
