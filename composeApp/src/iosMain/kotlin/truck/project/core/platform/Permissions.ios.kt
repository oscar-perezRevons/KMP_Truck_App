package truck.project.core.platform

import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocationPermissionState(onPermissionGranted: () -> Unit) {
    // For iOS, usually permissions are handled by Info.plist and system prompt
    // when location is accessed. This is a placeholder.
}
