package truck.project.features.trips.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity)

    @Update
    suspend fun update(trip: TripEntity)

    @Query("SELECT * FROM trips WHERE adminId = :adminId")
    fun getAllTrips(adminId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE adminId = :adminId AND status = 'EN_ROUTE'")
    fun getActiveTrips(adminId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: String): TripEntity?

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT count(*) FROM trips WHERE adminId = :adminId AND status = 'EN_ROUTE'")
    suspend fun getActiveTripsCount(adminId: String): Int

    @Query("DELETE FROM trips")
    suspend fun clearAll()
}
