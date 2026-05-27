package truck.project.features.driver.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.VolvoYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverPinScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val colors = LocalDsColors.current

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = VolvoYellow.copy(alpha = 0.1f)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.padding(6.dp), tint = VolvoYellow)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("ACCESO SEGURO", color = VolvoYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("INGRESO DE CONDUCTOR", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("PIN de 4 dígitos asignado por tu empresa", color = colors.textSecondary, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(48.dp))

            // PIN Display
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(4) { index ->
                    PinBox(isActive = pin.length > index)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Demo: 1234", color = colors.textSecondary.copy(alpha = 0.5f), fontSize = 12.sp)

            Spacer(modifier = Modifier.height(48.dp))

            // Keypad
            val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "DEL")
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                numbers.chunked(3).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        row.forEach { char ->
                            if (char.isEmpty()) {
                                Spacer(modifier = Modifier.size(80.dp))
                            } else {
                                KeypadButton(
                                    char = char,
                                    onClick = {
                                        if (char == "DEL") {
                                            if (pin.isNotEmpty()) pin = pin.dropLast(1)
                                        } else if (pin.length < 4) {
                                            pin += char
                                            if (pin.length == 4) {
                                                if (pin == "1234") onLoginSuccess()
                                                else pin = "" // Reset on wrong PIN
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PinBox(isActive: Boolean) {
    val colors = LocalDsColors.current
    Surface(
        modifier = Modifier.size(64.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isActive) Color.Transparent else colors.surface,
        border = androidx.compose.foundation.BorderStroke(2.dp, if (isActive) VolvoYellow else Color.White.copy(alpha = 0.1f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isActive) {
                Box(modifier = Modifier.size(12.dp).background(VolvoYellow, CircleShape))
            }
        }
    }
}

@Composable
fun KeypadButton(char: String, onClick: () -> Unit) {
    val colors = LocalDsColors.current
    Surface(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.5f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (char == "DEL") {
                Icon(Icons.Default.Backspace, contentDescription = null, tint = Color.White)
            } else {
                Text(char, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
