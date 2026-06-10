package truck.project.features.admin.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(admin: AdminEntity)

    @Update
    suspend fun updateSync(admin: AdminEntity)

    @Query("SELECT * FROM admins LIMIT 1")
    fun getAdminProfile(): Flow<AdminEntity?>

    @Query("SELECT * FROM admins LIMIT 1")
    suspend fun getAdminSync(): AdminEntity?

    @Query("DELETE FROM admins")
    suspend fun clearAll()
}
