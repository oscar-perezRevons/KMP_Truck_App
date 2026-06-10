package truck.project.features.fleet.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import truck.project.designsystem.theme.DsTheme
import truck.project.features.fleet.presentation.viewmodel.TruckViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TruckListScreen(
    viewModel: TruckViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    DsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DsTheme.colors.background)
                .padding(16.dp)
                .safeContentPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gestión de Flota",
                    style = DsTheme.typography.headlineLarge,
                    color = DsTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { viewModel.onSync() }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Sincronizar",
                        tint = DsTheme.colors.primary
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.trucks.isEmpty()) {
                    Text(
                        "No hay camiones registrados",
                        modifier = Modifier.align(Alignment.Center),
                        color = DsTheme.colors.textPrimary.copy(alpha = 0.6f)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.trucks) { truck ->
                            ListItem(
                                colors = ListItemDefaults.colors(containerColor = DsTheme.colors.background),
                                headlineContent = {
                                    Text(truck.plateNumber.value, style = DsTheme.typography.bodyLarge, color = DsTheme.colors.textPrimary)
                                },
                                supportingContent = {
                                    val displayStatus = if (truck.statusTranslated != null) {
                                        truck.statusTranslated
                                    } else {
                                        truck.status.name
                                    }
                                    Text("${truck.model} • $displayStatus", color = DsTheme.colors.textPrimary.copy(alpha = 0.7f), style = DsTheme.typography.bodyMedium)
                                },
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = { /* TODO Edit */ }) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = DsTheme.colors.primary)
                                        }
                                        IconButton(onClick = { viewModel.onDeleteTruck(truck) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
