package truck.project.truck_guia.presentation.screen

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
import truck.project.designsystem.components.buttons.PrimaryButton
import truck.project.truck_guia.presentation.viewmodel.TruckViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TruckScreen(
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
                    text = "Gestión de Camiones",
                    style = DsTheme.typography.heading,
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

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = DsTheme.colors.background)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (state.editingTruck == null) "Registrar Nuevo Camión" else "Editar Camión",
                        style = DsTheme.typography.body,
                        color = DsTheme.colors.textPrimary
                    )

                    OutlinedTextField(
                        value = state.licensePlate,
                        onValueChange = { viewModel.onLicensePlateChange(it) },
                        label = { Text("Matrícula (Ej: ABC-123)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.model,
                        onValueChange = { viewModel.onModelChange(it) },
                        label = { Text("Modelo (Ej: Volvo FH)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    PrimaryButton(
                        text = if (state.editingTruck == null) "GUARDAR CAMIÓN" else "ACTUALIZAR DATOS",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.onSaveTruck() }
                    )

                    if (state.editingTruck != null) {
                        TextButton(
                            onClick = { viewModel.onCancelEdit() },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Cancelar Edición", color = DsTheme.colors.primary)
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                "Lista de Flota (${state.trucks.size})",
                style = DsTheme.typography.heading,
                color = DsTheme.colors.textPrimary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
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
                                    Text(truck.licensePlate, style = DsTheme.typography.body, color = DsTheme.colors.textPrimary)
                                },
                                supportingContent = {
                                    val displayStatus = if (truck.statusTranslated != null) {
                                        truck.statusTranslated
                                    } else {
                                        truck.status
                                    }
                                    Text("${truck.model} • $displayStatus", color = DsTheme.colors.textPrimary.copy(alpha = 0.7f))
                                },
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = { viewModel.onEditTruck(truck) }) {
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
