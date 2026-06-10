package truck.project.features.auth.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import truck.project.features.auth.data.remote.model.LoginRequest
import truck.project.features.auth.data.remote.model.LoginResponse

class KtorAuthApi(private val client: HttpClient) : AuthApi {
    override suspend fun loginAdmin(request: LoginRequest): LoginResponse {
        return client.post("auth/login/admin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun loginDriver(pin: String): LoginResponse {
        return client.post("auth/login/driver") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(pin = pin))
        }.body()
    }
}
