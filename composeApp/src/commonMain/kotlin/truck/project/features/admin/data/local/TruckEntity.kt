package truck.project.features.admin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class TruckEntity(
    @PrimaryKey val id: String,
    val plateNumber: String,
    val model: String,
    val capacity: Double,
    val imageUrl: String? = null,
    val status: String = "AVAILABLE"
)
