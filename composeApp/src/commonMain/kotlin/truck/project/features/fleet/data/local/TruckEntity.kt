package truck.project.features.fleet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class TruckEntity(
    @PrimaryKey val id: String,
    val plateNumber: String,
    val model: String,
    val capacity: Double = 0.0,
    val imageUrl: String? = null,
    val status: String = "AVAILABLE",
    val statusTranslated: String? = null,
    val needsTranslation: Boolean = false
)
