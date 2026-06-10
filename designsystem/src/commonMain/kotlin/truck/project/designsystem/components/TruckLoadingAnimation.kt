package truck.project.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import truck.project.designsystem.theme.Bone
import truck.project.designsystem.theme.DsTheme

@Composable
fun TruckLoadingAnimation(
    modifier: Modifier = Modifier,
    truckEmoji: String = "🚛"
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val xOffset by infiniteTransition.animateValue(
        initialValue = (-100).dp,
        targetValue = 300.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            // Road lines
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomCenter)
                    .background(Bone.copy(alpha = 0.1f))
            )
            
            Text(
                text = truckEmoji,
                fontSize = 40.sp,
                modifier = Modifier
                    .offset(x = xOffset)
                    .align(Alignment.BottomStart)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = DsTheme.strings.loading,
            color = DsTheme.colors.primary,
            style = DsTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
    }
}
