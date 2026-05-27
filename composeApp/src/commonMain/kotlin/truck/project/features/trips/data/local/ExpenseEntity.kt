package truck.project.features.trips.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: String,
    val amount: Double,
    val category: String,
    val timestamp: Long,
    val note: String? = null
)
