package truck.project.core.storage

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class AndroidImageStorage(private val context: Context) : ImageStorage {
    override suspend fun saveImageLocally(imageData: ByteArray, fileName: String): String {
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { 
            it.write(imageData)
        }
        return file.absolutePath
    }

    override suspend fun uploadToCloud(imageData: ByteArray, cloudPath: String): String {
        // En una implementación real subiríamos a Firebase/S3. 
        // Para esta versión, retornamos una URL de imagen de camión real para que sea visible en el Dashboard.
        val truckImages = listOf(
            "https://images.volvotrucks.com/latis/Image?f=P&id=16302&v=1&t=1690450531&c=0x0:7680x4320&s=1920",
            "https://images.volvotrucks.com/latis/Image?f=P&id=16303&v=1&t=1690450531&c=0x0:7680x4320&s=1920",
            "https://images.volvotrucks.com/latis/Image?f=P&id=16304&v=1&t=1690450531&c=0x0:7680x4320&s=1920"
        )
        return truckImages.random()
    }
}
