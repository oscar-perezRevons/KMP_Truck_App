package truck.project.core.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class TranslationService(private val client: HttpClient) {
    private val apiKey = "tu_apiKey_de_loco" // TODO: Use BuildKonfig when available

    suspend fun translate(text: String, targetLang: String): String? {
        return try {
            val response = client.get("https://localise.biz/api/translate/$text.json") {
                parameter("key", apiKey)
                parameter("to", targetLang)
            }
            if (response.status.value in 200..299) {
                response.bodyAsText()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
