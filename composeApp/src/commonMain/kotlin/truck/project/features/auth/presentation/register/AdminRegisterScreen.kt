package truck.project.features.auth.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Shield
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = colors.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "PANEL ADMINISTRATIVO",
                    color = colors.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "CREAR CUENTA",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            VolvoTextField(value = name, onValueChange = { name = it }, label = "NOMBRE")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = company, onValueChange = { company = it }, label = "EMPRESA")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = email, onValueChange = { email = it }, label = "CORREO ELECTRÓNICO")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = password, onValueChange = { password = it }, label = "CONTRASEÑA")

            Spacer(modifier = Modifier.height(40.dp))

            VolvoButton(text = "CREAR CUENTA", onClick = onRegisterSuccess)

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = colors.primary)
            }
        }
    }
}
