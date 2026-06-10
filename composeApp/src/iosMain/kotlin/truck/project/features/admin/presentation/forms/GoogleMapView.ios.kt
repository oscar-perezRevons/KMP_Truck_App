package truck.project.features.admin.presentation.forms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    onMapClick: (lat: Double, lng: Double) -> Unit,
    startPoint: Pair<Double, Double>?,
    endPoint: Pair<Double, Double>?,
    currentPoint: Pair<Double, Double>?
) {
    Box(modifier = modifier.background(Color.Gray), contentAlignment = Alignment.Center) {
        Text("Maps not implemented for iOS yet", color = Color.White)
    }
}
