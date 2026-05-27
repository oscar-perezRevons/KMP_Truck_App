package truck.project.features.auth.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class DriverPin(val value: String) {
    init {
        require(value.length == 4 && value.all { it.isDigit() }) {
            "El PIN debe ser de 4 dígitos"
        }
    }
}
