package truck.project.features.fleet.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(driver: DriverEntity)

    @Update
    suspend fun update(driver: DriverEntity)

    @Delete
    suspend fun delete(driver: DriverEntity)

    @Query("SELECT * FROM drivers WHERE adminId = :adminId")
    fun getAllDrivers(adminId: String): Flow<List<DriverEntity>>

    @Query("SELECT * FROM drivers WHERE adminId = :adminId")
    suspend fun getAllDriversSync(adminId: String): List<DriverEntity>

    @Query("SELECT * FROM drivers WHERE id = :id")
    suspend fun getDriverById(id: String): DriverEntity?

    @Query("DELETE FROM drivers WHERE id = :id")
    suspend fun deleteDriverById(id: String)

    @Query("SELECT count(*) FROM drivers WHERE adminId = :adminId")
    fun getDriverCountFlow(adminId: String): Flow<Int>

    @Query("DELETE FROM drivers")
    suspend fun clearAll()

    // For login: drivers can be found by email globally across all companies
    @Query("SELECT * FROM drivers WHERE email = :email LIMIT 1")
    suspend fun getDriverByEmail(email: String): DriverEntity?
}
