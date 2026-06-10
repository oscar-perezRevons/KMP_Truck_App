package truck.project.features.driver.domain.model

import truck.project.core.domain.vo.OdometerValue
import kotlinx.datetime.LocalDateTime

data class Trip(
    val id: String,
    val driverId: String,
    val truckId: String,
    val origin: String,
    val destination: String,
    val startLat: Double? = null,
    val startLng: Double? = null,
    val endLat: Double? = null,
    val endLng: Double? = null,
    val status: TripStatus,
    val currentSpeed: Double = 0.0,
    val currentLocation: String? = null,
    val currentLat: Double? = null,
    val currentLng: Double? = null,
    val startOdometer: OdometerValue? = null,
    val endOdometer: OdometerValue? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val receiverSignatureUrl: String? = null
)

enum class TripStatus {
    ASSIGNED, STARTED, EN_ROUTE, COMPLETED, CANCELLED
}

data class Expense(
    val id: String,
    val tripId: String,
    val amount: Double,
    val category: ExpenseCategory,
    val timestamp: LocalDateTime,
    val note: String? = null
)

enum class ExpenseCategory {
    FUEL, TOLL, FOOD, LODGING, OTHER
}
