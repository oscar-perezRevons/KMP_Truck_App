package truck.project.features.admin.presentation.forms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen2
import kotlinproject.composeapp.generated.resources.imagen3
import kotlinproject.composeapp.generated.resources.imagen4
import kotlinproject.composeapp.generated.resources.imagen5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTruckScreen(
    onBack: () -> Unit,
    viewModel: NewTruckViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val colors = DsTheme.colors
    
    var originExpanded by remember { mutableStateOf(false) }
    var modelExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBack()
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.15f else 0.4f
        )
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.8f), colors.background)
            )
        ))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)
                        ) {
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
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 12.dp)) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = colors.textPrimary.copy(alpha = 0.05f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = if (state.isEditMode) "MODIFICAR\nUNIDAD" else "REGISTRO\nDE UNIDAD", 
                    color = colors.textPrimary, 
                    style = DsTheme.typography.displayMedium.copy(fontSize = 26.sp, lineHeight = 32.sp),
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(colors.primary, RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.height(48.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        
                        // PLACA VALIDATION
                        VolvoTextField(
                            value = state.plate, 
                            onValueChange = viewModel::onPlateChanged, 
                            label = "PLACA / MATRÍCULA (1000AAA)", 
                            placeholder = "Ej: 4825LKA"
                        )
                        
                        // ORIGEN COMBO BOX
                        ExposedDropdownMenuBox(
                            expanded = originExpanded,
                            onExpandedChange = { originExpanded = !originExpanded }
                        ) {
                            VolvoTextField(
                                value = state.origin,
                                onValueChange = {},
                                readOnly = true,
                                label = "LUGAR DE ORIGEN",
                                placeholder = "Seleccionar Departamento",
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originExpanded) },
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )
                            ExposedDropdownMenu(
                                expanded = originExpanded,
                                onDismissRequest = { originExpanded = false },
                                modifier = Modifier.background(colors.surface)
                            ) {
                                viewModel.availableOrigins.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption, style = DsTheme.typography.bodyLarge) },
                                        onClick = {
                                            viewModel.onOriginChanged(selectionOption)
                                            originExpanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }

                        // MODELO COMBO BOX
                        ExposedDropdownMenuBox(
                            expanded = modelExpanded,
                            onExpandedChange = { modelExpanded = !modelExpanded }
                        ) {
                            VolvoTextField(
                                value = state.model,
                                onValueChange = {},
                                readOnly = true,
                                label = "MODELO / SERIE VOLVO",
                                placeholder = "Seleccionar Modelo",
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )
                            ExposedDropdownMenu(
                                expanded = modelExpanded,
                                onDismissRequest = { modelExpanded = false },
                                modifier = Modifier.background(colors.surface)
                            ) {
                                viewModel.availableModels.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption, style = DsTheme.typography.bodyLarge) },
                                        onClick = {
                                            viewModel.onModelChanged(selectionOption)
                                            modelExpanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }

                        // CAPACIDAD NUMERICA
                        VolvoTextField(
                            value = state.capacity, 
                            onValueChange = viewModel::onCapacityChanged, 
                            label = "CAPACIDAD DE CARGA (TN)", 
                            placeholder = "Ej: 25.5",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            "VISTA PREVIA DE LA UNIDAD", 
                            color = colors.textSecondary, 
                            style = DsTheme.typography.labelSmall, 
                            fontWeight = FontWeight.ExtraBold, 
                            letterSpacing = 2.sp
                        )

                        Surface(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            shape = RoundedCornerShape(24.dp),
                            color = colors.textPrimary.copy(alpha = 0.05f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.imagen1),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.error)
                    ) {
                        Text(
                            text = state.error!!, 
                            color = colors.error, 
                            style = DsTheme.typography.bodyMedium, 
                            fontWeight = FontWeight.Bold, 
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                VolvoButton(
                    text = if (state.isLoading) DsTheme.strings.loading else if (state.isEditMode) DsTheme.strings.save else "REGISTRAR EN FLOTA",
                    onClick = viewModel::saveTruck,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = colors.secondary,
                    enabled = !state.isLoading
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
