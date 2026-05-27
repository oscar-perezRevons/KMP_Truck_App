package truck.project.features.fleet.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class Plate(val value: String) {
    init {
        require(value.isNotBlank()) { "La placa no puede estar vacía" }
    }
}
