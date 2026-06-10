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
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(top = 12.dp)) {
                    Surface(
                        modifier = Modifier.size(110.dp).clickable { imagePicker() },
                        shape = RoundedCornerShape(28.dp),
                        color = colors.textPrimary.copy(alpha = 0.05f),
                        border = androidx.compose.foundation.BorderStroke(2.dp, colors.secondary)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.photoDataList.isNotEmpty()) {
                                AsyncImage(model = state.photoDataList.first(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            } else if (state.photoUrls.isNotEmpty()) {
                                 AsyncImage(model = state.photoUrls.first(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                            } else {
                                Image(painter = painterResource(Res.drawable.logo), contentDescription = null, modifier = Modifier.size(64.dp))
                            }
                        }
                    }
                    Surface(
                        modifier = Modifier.size(32.dp).offset(x = 8.dp, y = 8.dp),
                        shape = CircleShape,
                        color = colors.secondary
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = colors.textOnPrimary, modifier = Modifier.padding(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = if (state.isEditMode) "ACTUALIZAR\nPERSONAL" else "ALTA DE\nPERSONAL", 
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
                        .background(colors.secondary, RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.height(48.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    color = colors.surface.copy(alpha = if (colors.isLight) 0.9f else 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        VolvoTextField(value = state.name, onValueChange = viewModel::onNameChanged, label = "NOMBRE COMPLETO", placeholder = "Ej: Manuel Santos")
                        VolvoTextField(value = state.dni, onValueChange = viewModel::onDniChanged, label = "DNI / IDENTIFICACIÓN", placeholder = "000000000")
                        VolvoTextField(value = state.license, onValueChange = viewModel::onLicenseChanged, label = "LICENCIA DE CONDUCIR", placeholder = "Categoría Profesional")
                        
                        Text(
                            "ACCESO Y SEGURIDAD", 
                            color = colors.textSecondary, 
                            style = DsTheme.typography.labelSmall, 
                            fontWeight = FontWeight.ExtraBold, 
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        VolvoTextField(value = state.email, onValueChange = viewModel::onEmailChanged, label = "CORREO ELECTRÓNICO", placeholder = "conductor@truckflow.com")
                        
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
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = state.error!!, color = colors.error, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(40.dp))

                VolvoButton(
                    text = if (state.isLoading) DsTheme.strings.loading else if (state.isEditMode) DsTheme.strings.save else "REGISTRAR OPERADOR",
                    onClick = viewModel::saveDriver,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = colors.secondary,
                    enabled = !state.isLoading
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
