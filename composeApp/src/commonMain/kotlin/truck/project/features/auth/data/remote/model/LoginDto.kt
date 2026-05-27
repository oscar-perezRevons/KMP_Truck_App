package truck.project.features.auth.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String? = null,
    val password: String? = null,
    val pin: String? = null
)

@Serializable
data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String
)
