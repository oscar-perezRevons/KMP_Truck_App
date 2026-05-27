package truck.project.features.fleet.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driver: DriverEntity): Long

    @Update
    suspend fun update(driver: DriverEntity)

    @Delete
    suspend fun delete(driver: DriverEntity)

    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers WHERE isActive = 1")
    fun getActiveDrivers(): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers WHERE id = :id")
    suspend fun getDriverById(id: Long): DriverEntity?

    @Query("SELECT count(*) FROM drivers")
    suspend fun getDriverCount(): Int

    @Query("SELECT count(*) FROM drivers")
    fun getDriverCountFlow(): Flow<Int>
}
