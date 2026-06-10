package truck.project.features.fleet.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {
    @Query("SELECT * FROM trucks WHERE adminId = :adminId")
    fun getAllTrucks(adminId: String): Flow<List<TruckEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTruck(truck: TruckEntity)

    @Update
    suspend fun update(truck: TruckEntity)

    @Delete
    suspend fun delete(truck: TruckEntity)

    @Query("SELECT * FROM trucks WHERE id = :id")
    suspend fun getTruckById(id: String): TruckEntity?

    @Query("DELETE FROM trucks WHERE id = :id")
    suspend fun deleteTruckById(id: String)
    
    @Query("SELECT COUNT(*) FROM trucks WHERE adminId = :adminId")
    fun getTruckCount(adminId: String): Flow<Int>

    @Query("SELECT * FROM trucks WHERE needsTranslation = 1")
    suspend fun getTrucksToTranslateOnce(): List<TruckEntity>

    @Query("DELETE FROM trucks")
    suspend fun clearAll()
}
