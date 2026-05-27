package truck.project.features.auth.data.remote

import truck.project.features.auth.data.remote.model.LoginRequest
import truck.project.features.auth.data.remote.model.LoginResponse

interface AuthApi {
    suspend fun loginAdmin(request: LoginRequest): LoginResponse
    suspend fun loginDriver(pin: String): LoginResponse
}
