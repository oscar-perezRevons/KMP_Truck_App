package truck.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class TranslationService(private val httpClient: HttpClient) {
    
    // Usaremos MyMemory API (Gratuita y universal)
    private val baseUrl = "https://api.mymemory.translated.net/get"

    suspend fun translate(text: String, targetLang: String): String? {
        return try {
            // MyMemory usa el par de idiomas, ej: "es|en"
            val langPair = "es|$targetLang"
            
            val response = httpClient.get(baseUrl) {
                parameter("q", text)
                parameter("langpair", langPair)
            }
            
            if (response.status == HttpStatusCode.OK) {
                val body = response.bodyAsText()
                val json = Json.parseToJsonElement(body).jsonObject
                val translatedText = json["responseData"]?.jsonObject?.get("translatedText")?.jsonPrimitive?.content
                translatedText
            } else {
                val errorBody = response.bodyAsText()
                "ERROR_LOG_INTERNAL:HTTP ${response.status}: $errorBody"
            }
        } catch (e: Exception) {
            "ERROR_LOG_INTERNAL:Exception ${e.message}"
        }
    }
}
