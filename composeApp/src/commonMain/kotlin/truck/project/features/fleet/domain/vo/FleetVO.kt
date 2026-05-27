package truck.project.features.fleet.domain.vo

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class PlateNumber(val value: String) {
    init {
        require(value.isNotBlank()) { "Plate number cannot be empty" }
    }
}

@JvmInline
@Serializable
value class DriverPin(val value: String) {
    init {
        require(value.length == 4 && value.all { it.isDigit() }) { "PIN must be 4 digits" }
    }
}
