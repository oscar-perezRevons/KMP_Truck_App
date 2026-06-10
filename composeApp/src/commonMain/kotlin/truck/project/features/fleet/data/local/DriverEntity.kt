package truck.project.features.fleet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey val id: String = "",
    val adminId: String = "", // Associated admin/company
    val fullName: String = "",
    val dni: String = "",
    val licenseNumber: String = "",
    val pin: String? = null,
    val email: String? = null,
    val password: String? = null,
    val photoUrl: String? = null,
    val photoUrls: String? = null, // Comma separated URLs
    val isActive: Boolean = true
)
