package truck.project.features.auth.data.repository

import truck.project.features.auth.data.remote.AuthApi
import truck.project.features.auth.data.remote.model.LoginRequest
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password
import truck.project.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun loginAdmin(email: Email, password: Password): Result<Unit> {
        return runCatching {
            api.loginAdmin(LoginRequest(email = email.value, password = password.value))
        }
    }

    override suspend fun loginDriver(pin: String): Result<Unit> {
        return runCatching {
            api.loginDriver(pin)
        }
    }
}
