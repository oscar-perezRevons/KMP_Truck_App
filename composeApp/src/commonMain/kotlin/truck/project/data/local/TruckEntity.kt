package truck.project.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class TruckEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val licensePlate: String = "",
    val model: String = "",
    val status: String = "",
    val statusTranslated: String? = null,
    val needsTranslation: Boolean = true
)
