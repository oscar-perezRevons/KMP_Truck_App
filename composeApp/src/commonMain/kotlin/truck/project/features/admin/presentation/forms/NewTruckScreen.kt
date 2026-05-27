package truck.project.features.admin.presentation.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocalShipping
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
fun NewTruckScreen(
    onBack: () -> Unit,
    viewModel: NewTruckViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = LocalDsColors.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBack()
    }

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { Text("NUEVO VEHÍCULO", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
                color = VolvoYellow.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, tint = VolvoYellow)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Ingresa los datos del camión para incorporarlo a la flota.",
                color = colors.textSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            VolvoTextField(value = state.plate, onValueChange = viewModel::onPlateChanged, label = "PLACA / PATENTE", placeholder = "ABC-1234")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = state.model, onValueChange = viewModel::onModelChanged, label = "MARCA / MODELO", placeholder = "Volvo FH 460")
            Spacer(modifier = Modifier.height(16.dp))
            VolvoTextField(value = state.capacity, onValueChange = viewModel::onCapacityChanged, label = "CAPACIDAD (TONELADAS)", placeholder = "20")

            state.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = colors.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("FOTOS DEL VEHÍCULO", color = colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                modifier = Modifier.size(100.dp).clickable { /* Abrir Selector de Imagen */ },
                shape = RoundedCornerShape(16.dp),
                color = colors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = colors.textSecondary)
                    Text("Foto", color = colors.textSecondary, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            VolvoButton(
                text = if (state.isLoading) "GUARDANDO..." else "GUARDAR E INGRESAR A LA FLOTA",
                onClick = viewModel::saveTruck,
                containerColor = VolvoYellow,
                contentColor = Color.Black
            )
        }
    }
}
