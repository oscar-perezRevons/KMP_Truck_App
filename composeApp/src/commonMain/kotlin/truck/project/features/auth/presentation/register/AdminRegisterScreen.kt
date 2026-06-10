package truck.project.features.auth.presentation.register

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.koin.compose.viewmodel.koinViewModel
import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.Denim
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.logo
import kotlinproject.composeapp.generated.resources.imagen1
import kotlinproject.composeapp.generated.resources.imagen4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AdminRegisterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val colors = DsTheme.colors

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onRegisterSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Image(
            painter = painterResource(Res.drawable.imagen4),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (colors.isLight) 0.15f else 0.4f
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
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spectacular Logo Section
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 20.dp)) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = colors.primary.copy(alpha = 0.05f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary.copy(alpha = 0.2f))
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "REGISTRO DE\nADMINISTRADOR",
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
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        VolvoTextField(value = state.name, onValueChange = viewModel::onNameChanged, label = "NOMBRE COMPLETO", placeholder = "Ej: John Doe")
                        VolvoTextField(value = state.company, onValueChange = viewModel::onCompanyChanged, label = "EMPRESA / CORPORACIÓN", placeholder = "Logística S.A.")
                        VolvoTextField(value = state.email, onValueChange = viewModel::onEmailChanged, label = "CORREO ELECTRÓNICO", placeholder = "admin@empresa.com")
                        VolvoTextField(value = state.password, onValueChange = viewModel::onPasswordChanged, label = "CONTRASEÑA", placeholder = "••••••••", visualTransformation = PasswordVisualTransformation())
                    }
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = state.error!!, color = colors.error, style = DsTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(48.dp))

                VolvoButton(
                    text = if (state.isLoading) DsTheme.strings.loading else "CREAR CUENTA MASTER",
                    onClick = viewModel::register,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = colors.secondary,
                    enabled = !state.isLoading
                )
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
