package truck.project.features.admin.presentation.monitor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.VolvoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripMonitorScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val colors = LocalDsColors.current

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { Text("MONITOR EN VIVO", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    Text("● LIVE", color = Color(0xFF4ADE80), fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp))
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
            // Driver Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colors.surface.copy(alpha = 0.5f)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(VolvoYellow.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = VolvoYellow, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CONDUCTOR ACTIVO", color = colors.textSecondary, fontSize = 10.sp)
                        Text("Carlos Rodríguez", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("VLV-2891", color = VolvoYellow, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Actualizado", color = colors.textSecondary, fontSize = 10.sp)
                        Text("10:52", color = Color(0xFF4ADE80), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Map Placeholder
            Surface(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.DarkGray.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("MAPA EN VIVO", color = colors.textSecondary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Progreso del Trayecto", color = Color.White, fontWeight = FontWeight.Bold)
                Text("38%", color = VolvoYellow, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0.38f },
                modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape),
                color = VolvoYellow,
                trackColor = colors.surface
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tabs
            Row(modifier = Modifier.fillMaxWidth().height(48.dp).background(colors.surface, RoundedCornerShape(12.dp))) {
                TabButton("DETALLES", selectedTab == 0, modifier = Modifier.weight(1f)) { selectedTab = 0 }
                TabButton("GASTOS VIVO", selectedTab == 1, modifier = Modifier.weight(1f)) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedTab == 0) {
                InfoItem("ORIGEN", "Planta Central")
                Spacer(modifier = Modifier.height(16.dp))
                InfoItem("DESTINO", "Almacén Norte")
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.surface.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Total acumulado", color = Color.White, fontSize = 14.sp)
                            Text("$97.50", color = VolvoYellow, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Bar(0.85f, VolvoYellow, "Combustible", "$85.50")
                            Bar(0.25f, Color(0xFF10B981), "Peajes", "$12.00")
                            Bar(0.40f, Color(0xFF3B82F6), "Comida", "$25.00")
                            Bar(0.15f, Color(0xFFEF4444), "Otros", "$5.00")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("ÚLTIMOS MOVIMIENTOS", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ExpenseRow("Peaje", "09:15", "$12.00")
                ExpenseRow("Combustible", "08:32", "$85.50")
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) VolvoYellow else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (isSelected) Color.Black else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    val colors = LocalDsColors.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = colors.surface.copy(alpha = 0.3f)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(colors.primary, CircleShape))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, color = colors.textSecondary, fontSize = 10.sp)
                Text(value, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ExpenseRow(label: String, time: String, amount: String) {
    val colors = LocalDsColors.current
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = colors.surface.copy(alpha = 0.3f)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.05f), CircleShape), contentAlignment = Alignment.Center) {
                Text("$", color = VolvoYellow, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = Color.White, fontWeight = FontWeight.Bold)
                Text(time, color = colors.textSecondary, fontSize = 12.sp)
            }
            Text(amount, color = VolvoYellow, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Bar(heightFraction: Float, color: Color, label: String, amount: String) {
    Column(modifier = Modifier.width(60.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(amount, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight(heightFraction)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(color, color.copy(alpha = 0.3f))
                    ),
                    RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(label.take(4), color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}
