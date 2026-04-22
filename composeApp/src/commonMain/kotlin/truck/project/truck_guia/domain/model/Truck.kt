package truck.project.truck_guia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Truck(
    val id: Long = 0,
    val licensePlate: String = "",
    val model: String = "",
    val status: String = "",
    val statusTranslated: String? = null,
    val needsTranslation: Boolean = true
)
