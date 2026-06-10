package truck.project.features.trips.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String = "",
    val adminId: String = "", // Associated admin/company
    val truckId: String? = null,
    val driverId: String? = null,
    val origin: String = "",
    val destination: String = "",
    val startLat: Double? = null,
    val startLng: Double? = null,
    val endLat: Double? = null,
    val endLng: Double? = null,
    val status: String = "PROGRAMMED", // "PROGRAMMED", "EN_ROUTE", "COMPLETED"
    val imageUrls: String? = null, // JSON string or comma-separated
    val progress: Int = 0,
    val currentSpeed: Double = 0.0,
    val currentLocation: String? = null,
    val currentLat: Double? = null,
    val currentLng: Double? = null,
    val estimatedTime: String? = null,
    val remainingDistance: String? = null,
    val startTime: Long? = null,
    val endTime: Long? = null
)
