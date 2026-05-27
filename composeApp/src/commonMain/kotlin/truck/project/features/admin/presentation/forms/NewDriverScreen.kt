package truck.project.features.admin.presentation.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
fun NewDriverScreen(
    onBack: () -> Unit,
    viewModel: NewDriverViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = LocalDsColors.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBack()
    }

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { Text("NUEVO CONDUCTOR", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                color = colors.primary.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = colors.primary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            VolvoTextField(value = state.name, onValueChange = viewModel::onNameChanged, label = "NOMBRE COMPLETO", placeholder = "Carlos Rodríguez")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = state.dni, onValueChange = viewModel::onDniChanged, label = "DNI / CÉDULA", placeholder = "12.345.678")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = state.license, onValueChange = viewModel::onLicenseChanged, label = "N° DE LICENCIA", placeholder = "B-2345678")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("PIN DE 4 DÍGITOS", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(4) { index ->
                    Surface(
                        modifier = Modifier.size(56.dp).clickable { /* Abrir Teclado */ },
                        shape = RoundedCornerShape(12.dp),
                        color = colors.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.pin.length > index) {
                                Box(modifier = Modifier.size(8.dp).background(colors.primary, CircleShape))
                            }
                        }
                    }
                }
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = colors.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = if (state.isLoading) "REGISTRANDO..." else "REGISTRAR CONDUCTOR",
                onClick = viewModel::saveDriver
            )
        }
    }
}
