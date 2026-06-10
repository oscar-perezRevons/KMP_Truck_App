package truck.project.truck_guia.domain.model

import kotlinx.serialization.Serializable
import truck.project.truck_guia.domain.vo.Placa

@Serializable
data class Truck(
    val id: String = "",
    val licensePlate: Placa,
    val model: String = "",
    val status: String = "",
    val statusTranslated: String? = null,
    val needsTranslation: Boolean = true
)
