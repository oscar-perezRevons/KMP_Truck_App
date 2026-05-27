package truck.project.features.auth.domain.repository

import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password

interface AuthRepository {
    suspend fun loginAdmin(email: Email, password: Password): Result<Unit>
    suspend fun loginDriver(pin: String): Result<Unit>
}
