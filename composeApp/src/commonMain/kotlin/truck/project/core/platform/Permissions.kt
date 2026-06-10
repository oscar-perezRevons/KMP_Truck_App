package truck.project.core.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberLocationPermissionState(onPermissionGranted: () -> Unit)
