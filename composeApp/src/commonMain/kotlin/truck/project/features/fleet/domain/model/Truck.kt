package truck.project.features.fleet.domain.model

import kotlinx.serialization.Serializable
import truck.project.features.fleet.domain.vo.PlateNumber

@Serializable
data class Truck(
    val id: String = "",
    val adminId: String = "",
    val plateNumber: PlateNumber,
    val model: String = "",
    val origin: String = "",
    val capacity: Double = 0.0,
    val imageUrl: String? = null,
    val imageUrls: List<String> = emptyList(),
    val status: TruckStatus = TruckStatus.AVAILABLE,
    val statusTranslated: String? = null,
    val needsTranslation: Boolean = false
)

enum class TruckStatus {
    AVAILABLE, IN_ROUTE, MAINTENANCE
}
