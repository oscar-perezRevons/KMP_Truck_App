package truck.project.features.fleet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val dni: String,
    val licenseNumber: String,
    val pin: String, // Value Object DriverPin will be used in Domain
    val photoUrl: String? = null,
    val isActive: Boolean = true
)
