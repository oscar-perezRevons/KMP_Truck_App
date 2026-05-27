package truck.project.features.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.theme.LocalDsColors

import truck.project.designsystem.components.VolvoButton
import truck.project.designsystem.components.VolvoScaffold
import truck.project.designsystem.components.VolvoTextField
import truck.project.designsystem.theme.LocalDsColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    state: AdminLoginState,
    onIntent: (AdminLoginIntent) -> Unit
) {
    val colors = LocalDsColors.current

    VolvoScaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onIntent(AdminLoginIntent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
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
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = colors.surface
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = colors.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "PANEL ADMINISTRATIVO",
                    color = colors.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "INGRESO\nADMINISTRADOR",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            VolvoTextField(
                value = state.email,
                onValueChange = { onIntent(AdminLoginIntent.EmailChanged(it)) },
                label = "CORREO ELECTRÓNICO",
                placeholder = "admin@flota.com"
            )
            state.emailError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            VolvoTextField(
                value = state.password,
                onValueChange = { onIntent(AdminLoginIntent.PasswordChanged(it)) },
                label = "CONTRASEÑA",
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = colors.textSecondary)
                }
            )
            state.passwordError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            VolvoButton(
                text = "INICIAR SESIÓN",
                onClick = { onIntent(AdminLoginIntent.LoginClicked) },
                modifier = Modifier.fillMaxWidth(),
                containerColor = colors.primary
            )

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = colors.primary)
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { /* Navegar a registro */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí",
                    color = colors.primary
                )
            }
        }
    }
}
