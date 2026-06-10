package truck.project.features.admin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
data class AdminEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val company: String = "",
    val profileImageUrl: String? = null
)
