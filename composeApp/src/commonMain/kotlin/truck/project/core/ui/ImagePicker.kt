package truck.project.core.ui

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onImageSelected: (ByteArray) -> Unit): () -> Unit

@Composable
expect fun rememberCameraLauncher(onImageCaptured: (ByteArray) -> Unit): () -> Unit
