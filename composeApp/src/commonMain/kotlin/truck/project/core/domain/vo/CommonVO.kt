package truck.project.core.domain.vo

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Email(val value: String) {
    init {
        require(Regex("^[A-Za-z0-9+_.-]+@(.+)$").matches(value)) { "Invalid email format" }
    }
    
    companion object {
        fun isValid(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}\$".toRegex()
            return email.matches(emailRegex)
        }
    }
}

@JvmInline
@Serializable
value class Password(val value: String) {
    init {
        require(value.length >= 6) { "Password must be at least 6 characters" }
    }
}

@JvmInline
@Serializable
value class OdometerValue(val value: Double) {
    init {
        require(value >= 0) { "Odometer cannot be negative" }
    }
}
