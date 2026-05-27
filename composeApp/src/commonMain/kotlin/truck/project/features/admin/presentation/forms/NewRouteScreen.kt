package truck.project.features.admin.presentation.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.LocalDsColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRouteScreen(
    onBack: () -> Unit,
    onAssign: () -> Unit
) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    val colors = LocalDsColors.current

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { Text("PROGRAMAR VIAJE", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
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
                .padding(24.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF10B981).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF10B981))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("SELECCIONAR CHOFER", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownPlaceholder("Seleccionar...")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("SELECCIONAR CAMIÓN", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownPlaceholder("Seleccionar...")

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surface.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PUNTOS DE RUTA", color = colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    VolvoTextField(value = origin, onValueChange = { origin = it }, label = "PUNTO DE ORIGEN", placeholder = "La Paz")
                    Spacer(modifier = Modifier.height(16.dp))
                    VolvoTextField(value = destination, onValueChange = { destination = it }, label = "PUNTO DE DESTINO", placeholder = "Santa Cruz")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = "ASIGNAR Y ENVIAR A LA APP",
                onClick = onAssign,
                containerColor = Color(0xFF10B981)
            )
        }
    }
}

@Composable
fun DropdownPlaceholder(text: String) {
    val colors = LocalDsColors.current
    Surface(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text, color = Color.White)
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(20.dp).graphicsLayer(rotationZ = -90f))
        }
    }
}
