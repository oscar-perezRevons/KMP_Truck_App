package truck.project.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.theme.DsTheme

@Composable
fun VolvoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp,
        label = "elevation"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .scale(scale)
            .graphicsLayer {
                shadowElevation = elevation.toPx()
                shape = RoundedCornerShape(20.dp)
                clip = true
            },
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor ?: DsTheme.colors.primary,
            contentColor = contentColor ?: DsTheme.colors.textOnPrimary,
            disabledContainerColor = (containerColor ?: DsTheme.colors.primary).copy(alpha = 0.3f),
            disabledContentColor = (contentColor ?: DsTheme.colors.textOnPrimary).copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp, // Handled by graphicsLayer for more realism
            pressedElevation = 0.dp
        )
    ) {
        Text(
            text = text.uppercase(),
            style = DsTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
    }
}
