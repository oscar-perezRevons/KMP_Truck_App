package truck.project.core.storage

interface ImageStorage {
    suspend fun saveImageLocally(imageData: ByteArray, fileName: String): String
    suspend fun uploadToCloud(imageData: ByteArray, cloudPath: String): String
}
