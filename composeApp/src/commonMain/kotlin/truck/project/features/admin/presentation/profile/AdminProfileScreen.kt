package truck.project.features.admin.presentation.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
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
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import truck.project.core.ui.rememberImagePickerLauncher
import coil3.compose.AsyncImage
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AdminProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val colors = DsTheme.colors

    val imagePicker = rememberImagePickerLauncher { bytes ->
        viewModel.onPhotoSelected(bytes)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            isEditing = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen5),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.12f else 0.3f
        )
        
        Box(modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, colors.background.copy(alpha = 0.9f), colors.background)
            )
        ))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                DsTheme.strings.profile,
                                style = DsTheme.typography.headlineMedium, 
                                color = colors.textPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.background(colors.textPrimary.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = colors.textPrimary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(if (isEditing) Icons.Default.Close else Icons.Default.Edit, contentDescription = "Editar", tint = colors.textPrimary)
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
                // Avatar Section
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        modifier = Modifier.size(140.dp).clickable(enabled = isEditing) { imagePicker() },
                        shape = RoundedCornerShape(40.dp),
                        color = colors.textPrimary.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(3.dp, if (isEditing) colors.secondary else colors.textPrimary.copy(alpha = 0.3f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (state.photoData != null) {
                                AsyncImage(
                                    model = state.photoData,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (!state.admin?.profileImageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = state.admin?.profileImageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(Res.drawable.logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                            
                            if (isEditing) {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                                }
                            }
                        }
                    }
                    
                    if (!isEditing) {
                        Surface(
                            modifier = Modifier.size(32.dp).padding(3.dp),
                            shape = CircleShape,
                            color = Color(0xFF4ADE80),
                            border = androidx.compose.foundation.BorderStroke(3.dp, colors.background)
                        ) {}
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Name and Title
                Text(
                    (state.admin?.name ?: "").uppercase(), 
                    color = colors.textPrimary, 
                    style = DsTheme.typography.displayMedium.copy(fontSize = 28.sp, lineHeight = 34.sp),
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    "ADMINISTRADOR DE LOGÍSTICA", 
                    color = colors.secondary, 
                    style = DsTheme.typography.subHeading,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                if (state.statusMessage != null || state.error != null) {
                    val msgColor = if (state.error != null) colors.error else Color(0xFF4ADE80)
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = msgColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, msgColor)
                    ) {
                        Text(
                            text = state.error ?: state.statusMessage!!,
                            color = msgColor,
                            style = DsTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Main Info Section
                ProfileSectionItem("INFORMACIÓN CORPORATIVA") {
                    if (isEditing) {
                        VolvoTextField(value = state.name, onValueChange = viewModel::onNameChanged, label = "NOMBRE COMPLETO")
                        VolvoTextField(value = state.company, onValueChange = viewModel::onCompanyChanged, label = "EMPRESA / CONCESIONARIO")
                        VolvoTextField(value = state.password, onValueChange = viewModel::onPasswordChanged, label = "NUEVA CONTRASEÑA (OPCIONAL)")
                    } else {
                        InfoRowDataItem("IDENTIFICADOR", state.admin?.name ?: "---")
                        InfoRowDataItem("CANAL OFICIAL", state.admin?.email?.value ?: "---")
                        InfoRowDataItem("OPERACIÓN", state.admin?.company ?: "---")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Service Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.08f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text("VOLVO ACTION SERVICE", color = Color(0xFF10B981), style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Text("SOPORTE 24/7 GLOBAL", color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Bottom Actions
                if (isEditing) {
                    VolvoButton(
                        text = if (state.isLoading) DsTheme.strings.loading else DsTheme.strings.save,
                        onClick = viewModel::updateProfile,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = colors.secondary,
                        enabled = !state.isLoading
                    )
                } else {
                    Button(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colors.error.copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.error)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = colors.error)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(DsTheme.strings.logout, color = colors.error, style = DsTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProfileSectionItem(title: String, content: @Composable ColumnScope.() -> Unit) {
    val colors = DsTheme.colors
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = colors.textPrimary.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.textPrimary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title, 
                color = colors.primary, 
                style = DsTheme.typography.subHeading,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            content()
        }
    }
}

@Composable
fun InfoRowDataItem(label: String, value: String) {
    val colors = DsTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = colors.textSecondary, style = DsTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(value, color = colors.textPrimary, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
