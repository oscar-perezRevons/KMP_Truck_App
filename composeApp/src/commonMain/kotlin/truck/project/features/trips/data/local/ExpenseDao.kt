package truck.project.features.trips.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM expenses WHERE tripId = :tripId")
    fun getExpensesByTrip(tripId: String): Flow<List<ExpenseEntity>>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE tripId = :tripId")
    fun getTotalExpensesByTrip(tripId: String): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE adminId = :adminId AND timestamp >= :startOfDay")
    fun getTodayTotalExpenses(adminId: String, startOfDay: Long): Flow<Double?>

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}
