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
        // Implementación simplificada de subida a Firebase Storage
        // En un caso real usaríamos FirebaseStorage.getInstance().reference.child(cloudPath).putBytes(imageData)
        return "https://firebasestorage.googleapis.com/v0/b/truckapp/o/${cloudPath.replace("/", "%2F")}?alt=media"
    }
}
