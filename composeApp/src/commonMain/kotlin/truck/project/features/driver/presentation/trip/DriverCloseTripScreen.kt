package truck.project.features.driver.presentation.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.VolvoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverCloseTripScreen(
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    var endOdometer by remember { mutableStateOf("100") }
    val colors = LocalDsColors.current

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIERRE DE VIAJE", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF10B981).copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("¡Llegaste a destino! Completa el cierre del viaje.", color = Color(0xFF10B981), fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            VolvoTextField(value = endOdometer, onValueChange = { endOdometer = it }, label = "ODÓMETRO FINAL (KM)")

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("FIRMA DEL RECEPTOR", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Limpiar", color = colors.textSecondary, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Signature Pad Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(colors.surface.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("✍️", fontSize = 48.sp) // Representing signature with emoji
            }

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = "FINALIZAR VIAJE Y LIBERAR CAMIÓN",
                onClick = onFinish,
                containerColor = VolvoYellow,
                contentColor = Color.Black
            )
        }
    }
}
