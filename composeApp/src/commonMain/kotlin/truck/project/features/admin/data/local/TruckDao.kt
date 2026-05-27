package truck.project.features.admin.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {
    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): Flow<List<TruckEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: TruckEntity)

    @Query("DELETE FROM trucks WHERE id = :id")
    suspend fun deleteTruckById(id: String)
    
    @Query("SELECT COUNT(*) FROM trucks")
    fun getTruckCount(): Flow<Int>
}
