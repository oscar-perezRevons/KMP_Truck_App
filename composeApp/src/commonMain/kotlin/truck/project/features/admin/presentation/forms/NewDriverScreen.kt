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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import truck.project.features.admin.domain.repository.StorageMode
import coil3.compose.AsyncImage
import truck.project.core.ui.rememberImagePickerLauncher
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDriverScreen(
    onBack: () -> Unit,
    viewModel: NewDriverViewModel = org.koin.compose.viewmodel.koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val colors = DsTheme.colors

    val imagePicker = rememberImagePickerLauncher { bytes ->
        viewModel.onPhotoSelected(bytes)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onBack()
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen2),
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
                // Profile Header
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(top = 20.dp)) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val borderAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 0.8f,
                        animationSpec = infiniteRepeatable(animation = tween(2000), repeatMode = RepeatMode.Reverse)
                    )

                    Surface(
                        modifier = Modifier.size(120.dp).clickable { imagePicker() },
                        shape = RoundedCornerShape(35.dp),
                        color = colors.textPrimary.copy(alpha = 0.03f),
                        border = androidx.compose.foundation.BorderStroke(2.dp, colors.secondary.copy(alpha = borderAlpha))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.photoDataList.isNotEmpty()) {
                                AsyncImage(model = state.photoDataList.first(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            } else if (state.photoUrls.isNotEmpty()) {
                                 AsyncImage(model = state.photoUrls.first(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp), tint = colors.textPrimary.copy(alpha = 0.1f))
                                    Text("FOTO", style = DsTheme.typography.labelSmall, color = colors.textPrimary.copy(alpha = 0.2f), fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier.size(38.dp).offset(x = 10.dp, y = 10.dp),
                        shape = CircleShape,
                        color = colors.secondary,
                        shadowElevation = 8.dp
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = colors.textOnPrimary, modifier = Modifier.padding(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
                
                Text(
                    text = if (state.isEditMode) "ACTUALIZAR\nPERSONAL" else "ALTA DE\nPERSONAL", 
                    color = colors.textPrimary, 
                    style = DsTheme.typography.displayMedium.copy(fontSize = 32.sp, lineHeight = 36.sp, letterSpacing = (-1).sp),
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(60.dp)
                        .height(6.dp)
                        .background(
                            Brush.horizontalGradient(listOf(colors.secondary, colors.primary)), 
                            RoundedCornerShape(3.dp)
                        )
                )

                Spacer(modifier = Modifier.height(40.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(36.dp),
                    color = colors.surface.copy(alpha = if (colors.isLight) 0.95f else 0.85f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.05f)),
                    shadowElevation = 12.dp
                ) {
                    Column(modifier = Modifier.padding(28.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
                            Text("DATOS PERSONALES", color = colors.primary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        }

                        VolvoTextField(value = state.name, onValueChange = viewModel::onNameChanged, label = "NOMBRE COMPLETO", placeholder = "Ej: Manuel Santos")
                        
                        // C.I. Section
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("CÉDULA DE IDENTIDAD (BOLIVIA)", color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                VolvoTextField(
                                    value = state.dniBase, 
                                    onValueChange = viewModel::onDniBaseChanged, 
                                    label = "NÚMERO BASE", 
                                    placeholder = "6755210",
                                    modifier = Modifier.weight(1.4f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                VolvoTextField(
                                    value = state.dniComplement, 
                                    onValueChange = viewModel::onDniComplementChanged, 
                                    label = "COMP.", 
                                    placeholder = "1A",
                                    modifier = Modifier.weight(0.7f)
                                )
                                
                                var extensionExpanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = extensionExpanded,
                                    onExpandedChange = { extensionExpanded = it },
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Box(modifier = Modifier.menuAnchor()) {
                                        VolvoTextField(
                                            value = state.dniExtension,
                                            onValueChange = {},
                                            readOnly = true,
                                            enabled = true,
                                            label = "EXT.",
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = extensionExpanded) }
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                                .clickable { extensionExpanded = !extensionExpanded }
                                        )
                                    }
                                    ExposedDropdownMenu(
                                        expanded = extensionExpanded,
                                        onDismissRequest = { extensionExpanded = false },
                                        modifier = Modifier.widthIn(min = 220.dp).background(colors.surface)
                                    ) {
                                        viewModel.availableExtensions.forEach { extension ->
                                            DropdownMenuItem(
                                                text = { 
                                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                        Surface(
                                                            modifier = Modifier.size(32.dp),
                                                            shape = RoundedCornerShape(8.dp),
                                                            color = colors.primary.copy(alpha = 0.1f)
                                                        ) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Text(extension, style = DsTheme.typography.labelSmall, color = colors.primary, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                        Text(viewModel.extensionNames[extension] ?: "", style = DsTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                                    }
                                                },
                                                onClick = {
                                                    viewModel.onDniExtensionChanged(extension)
                                                    extensionExpanded = false
                                                },
                                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // License Section
                        var licenseExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = licenseExpanded,
                            onExpandedChange = { licenseExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.menuAnchor()) {
                                VolvoTextField(
                                    value = state.licenseType,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = true,
                                    label = "LICENCIA DE CONDUCIR",
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = licenseExpanded) }
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp)
                                        .clickable { licenseExpanded = !licenseExpanded }
                                )
                            }

                            ExposedDropdownMenu(
                                expanded = licenseExpanded,
                                onDismissRequest = { licenseExpanded = false },
                                modifier = Modifier.fillMaxWidth().background(colors.surface)
                            ) {
                                viewModel.availableLicenses.forEach { license ->
                                    DropdownMenuItem(
                                        text = { 
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                Icon(Icons.Default.CardMembership, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(20.dp))
                                                Text(license, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        onClick = {
                                            viewModel.onLicenseTypeChanged(license)
                                            licenseExpanded = false
                                        },
                                        contentPadding = PaddingValues(16.dp)
                                    )
                                }
                            }
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = colors.textPrimary.copy(alpha = 0.05f))

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
                            Text("ACCESO Y SEGURIDAD", color = colors.primary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        }

                        VolvoTextField(
                            value = state.emailPrefix, 
                            onValueChange = viewModel::onEmailPrefixChanged, 
                            label = "CORREO ELECTRÓNICO", 
                            placeholder = "usuario",
                            trailingIcon = {
                                Surface(
                                    modifier = Modifier.padding(end = 4.dp),
                                    color = colors.textPrimary.copy(alpha = 0.05f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "@gmail.com ",
                                        color = colors.textSecondary,
                                        style = DsTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        )
                        
                        if (!state.isEditMode) {
                            VolvoTextField(
                                value = state.password,
                                onValueChange = viewModel::onPasswordChanged,
                                label = "CONTRASEÑA DE ACCESO",
                                placeholder = "••••••••",
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = colors.textSecondary.copy(alpha = 0.5f))
                                    }
                                }
                            )
                        }
                    }
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(
                        color = colors.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.error.copy(alpha = 0.2f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = colors.error, modifier = Modifier.size(20.dp))
                            Text(text = state.error!!, color = colors.error, style = DsTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                VolvoButton(
                    text = if (state.isLoading) DsTheme.strings.loading else if (state.isEditMode) "ACTUALIZAR DATOS" else "CREAR EXPEDIENTE",
                    onClick = viewModel::saveDriver,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    containerColor = colors.secondary,
                    enabled = !state.isLoading
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Al registrar, el conductor podrá acceder con su correo y contraseña.",
                    style = DsTheme.typography.labelSmall,
                    color = colors.textSecondary.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
