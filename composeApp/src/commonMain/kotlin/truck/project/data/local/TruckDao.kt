package truck.project.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(truck: TruckEntity): Long

    @Update
    suspend fun update(truck: TruckEntity)

    @Delete
    suspend fun delete(truck: TruckEntity)

    @Query("SELECT * FROM trucks")
    fun getAllTrucks(): Flow<List<TruckEntity>>

    @Query("SELECT count(*) FROM trucks")
    suspend fun getTruckCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trucks: List<TruckEntity>)

    @Query("SELECT * FROM trucks WHERE needsTranslation = 1")
    fun getTrucksToTranslate(): Flow<List<TruckEntity>>

    @Query("SELECT * FROM trucks WHERE needsTranslation = 1")
    suspend fun getTrucksToTranslateOnce(): List<TruckEntity>

    @Query("SELECT * FROM trucks WHERE id = :id")
    suspend fun getTruckById(id: Long): TruckEntity?

    @Query("DELETE FROM trucks")
    suspend fun deleteAll()
}
