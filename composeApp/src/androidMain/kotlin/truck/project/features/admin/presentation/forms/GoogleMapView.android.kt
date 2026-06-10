package truck.project.features.admin.presentation.forms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*

@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    onMapClick: (lat: Double, lng: Double) -> Unit,
    startPoint: Pair<Double, Double>?,
    endPoint: Pair<Double, Double>?,
    currentPoint: Pair<Double, Double>?
) {
    val initialPos = currentPoint ?: startPoint ?: Pair(-16.5, -68.1) // Default to Bolivia
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(initialPos.first, initialPos.second), 12f)
    }

    val mapProperties = MapProperties(
        isMyLocationEnabled = true
    )
    
    val uiSettings = MapUiSettings(
        myLocationButtonEnabled = true,
        zoomControlsEnabled = true
    )

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapClick = { latLng ->
            onMapClick(latLng.latitude, latLng.longitude)
        }
    ) {
        startPoint?.let {
            Marker(
                state = MarkerState(position = LatLng(it.first, it.second)),
                title = "Origen",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }
        endPoint?.let {
            Marker(
                state = MarkerState(position = LatLng(it.first, it.second)),
                title = "Destino",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
        }
        currentPoint?.let {
            Marker(
                state = MarkerState(position = LatLng(it.first, it.second)),
                title = "Ubicación Actual",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
            )
        }
        
        if (startPoint != null && endPoint != null) {
            Polyline(
                points = listOf(
                    LatLng(startPoint.first, startPoint.second),
                    LatLng(endPoint.first, endPoint.second)
                ),
                color = androidx.compose.ui.graphics.Color.Blue,
                width = 5f
            )
        }
    }
}
