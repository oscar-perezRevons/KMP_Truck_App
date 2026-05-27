package truck.project.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.DeepNavy

@Composable
fun VolvoScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val colors = LocalDsColors.current
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con gradiente profesional Volvo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DeepNavy,
                            Color(0xFF020617)
                        )
                    )
                )
        )
        
        // Imagen de fondo (Volvo Truck) con transparencia
        // Nota: Res.drawable.volvo_bg debería ser añadido a los recursos
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )
    }
}
