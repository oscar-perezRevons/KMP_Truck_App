package truck.project.core.ui

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (ByteArray) -> Unit): () -> Unit {
    return {
        // Implementación iOS pendiente
    }
}

@Composable
actual fun rememberCameraLauncher(onImageCaptured: (ByteArray) -> Unit): () -> Unit {
    return {
        // Implementación iOS pendiente
    }
}
