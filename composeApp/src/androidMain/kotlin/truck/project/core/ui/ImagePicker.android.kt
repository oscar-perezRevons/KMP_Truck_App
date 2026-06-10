package truck.project.core.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import android.graphics.Bitmap

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (ByteArray) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    onImageSelected(bytes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    return { launcher.launch("image/*") }
}

@Composable
actual fun rememberCameraLauncher(onImageCaptured: (ByteArray) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val stream = java.io.ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            onImageCaptured(stream.toByteArray())
        }
    }
    return { launcher.launch(null) }
}
