package truck.project.features.trips.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val adminId: String = "",
    val tripId: String = "",
    val amount: Double = 0.0,
    val category: String = "OTHER",
    val timestamp: Long = 0,
    val note: String? = null,
    val receiptUrl: String? = null
)
