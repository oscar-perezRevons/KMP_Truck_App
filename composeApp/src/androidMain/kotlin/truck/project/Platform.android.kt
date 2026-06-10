package truck.project

import android.os.Build
import android.os.Environment
import android.util.Base64
import java.io.File

class AndroidPlatform(private val context: android.content.Context? = null) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    
    override val downloadFolder: String
        get() = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath 
            ?: "/storage/emulated/0/Download"

    override fun writeFile(path: String, content: String) {
        try {
            val file = File(path)
            file.parentFile?.mkdirs()
            file.writeText(content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLogoBase64(): String {
        return try {
            // Intentamos leer el logo desde los assets generados por Compose
            val inputStream = context?.assets?.open("composeResources/kotlinproject.composeapp.generated.resources/drawable/logo.png")
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if (bytes != null) {
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()