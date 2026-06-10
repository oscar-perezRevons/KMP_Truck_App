package truck.project.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import truck.project.designsystem.theme.DsTheme
import truck.project.designsystem.theme.LocalDsColors
import truck.project.designsystem.theme.Noir
import truck.project.designsystem.theme.Denim
import truck.project.designsystem.theme.Bone

@Composable
fun VolvoScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    backgroundUrl: String = "https://images.volvotrucks.com/latis/Image?f=P&id=16260&v=1&t=1690450531&c=0x0:7680x4320&s=1920",
    isClear: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    val currentColors = DsTheme.colors
    
    Box(modifier = Modifier.fillMaxSize().background(currentColors.background)) {
        // Fondo con imagen Volvo Truck
        AsyncImage(
            model = backgroundUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = if (isClear) 0.8f else if (currentColors.isLight) 0.4f else 0.6f
        )

        // Geometric Pattern in Top Left
        GeometricBackground(
            modifier = Modifier.size(220.dp).align(Alignment.TopStart),
            isClear = isClear
        )

        // Overlay dinámico para legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = if (isClear) {
                             listOf(
                                Color.Transparent,
                                currentColors.background.copy(alpha = 0.2f),
                                currentColors.background.copy(alpha = 0.5f)
                            )
                        } else if (currentColors.isLight) {
                            listOf(
                                Color.White.copy(alpha = 0.05f),
                                currentColors.background.copy(alpha = 0.6f),
                                currentColors.background
                            )
                        } else {
                            listOf(
                                Color.Transparent,
                                currentColors.background.copy(alpha = 0.7f),
                                currentColors.background
                            )
                        }
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

@Composable
fun GeometricBackground(modifier: Modifier = Modifier, isClear: Boolean = false) {
    val colors = DsTheme.colors
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Parallelogram 1 (Primary)
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(width * 0.7f, 0f)
                    lineTo(width * 0.4f, height)
                    lineTo(0f, height)
                    close()
                },
                color = colors.primary.copy(alpha = if (isClear) 0.6f else 0.8f)
            )

            // Parallelogram 2 (Accent)
            drawPath(
                path = Path().apply {
                    moveTo(width * 0.2f, 0f)
                    lineTo(width * 0.5f, 0f)
                    lineTo(width * 0.2f, height * 0.6f)
                    lineTo(width * 0.1f, height * 0.6f)
                    close()
                },
                color = (if (colors.isLight) colors.secondary else Bone).copy(alpha = 0.5f)
            )

            // Small Accent line
            drawRect(
                color = if (colors.isLight) colors.secondary else Bone,
                topLeft = Offset(width * 0.05f, height * 0.8f),
                size = Size(width * 0.3f, 4f)
            )
        }
    }
}
