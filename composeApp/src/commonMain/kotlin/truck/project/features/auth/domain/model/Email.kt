package truck.project.features.auth.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class Email(val value: String) {
    init {
        require(isValid(value)) { "Formato de correo electrónico inválido" }
    }

    companion object {
        fun isValid(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}\$".toRegex()
            return email.matches(emailRegex)
        }
    }
}
