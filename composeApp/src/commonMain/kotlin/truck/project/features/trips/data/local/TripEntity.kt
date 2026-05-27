package truck.project.features.trips.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val truckId: Long,
    val driverId: Long,
    val origin: String,
    val destination: String,
    val status: String, // "PROGRAMMED", "EN_ROUTE", "COMPLETED"
    val progress: Int = 0,
    val estimatedTime: String? = null,
    val remainingDistance: String? = null,
    val startTime: Long? = null,
    val endTime: Long? = null
)
