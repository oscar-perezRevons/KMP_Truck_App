package com.ucb.app.truck.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ucb.app.truck.data.database.model.TruckBrandEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckBrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brand: TruckBrandEntity)

    @Update
    suspend fun update(brand: TruckBrandEntity)

    @Delete
    suspend fun delete(brand: TruckBrandEntity)

    @Query("SELECT * FROM truck_brands")
    fun getAllBrands(): Flow<List<TruckBrandEntity>>

    @Query("SELECT * FROM truck_brands WHERE id = :id")
    suspend fun getBrandById(id: Int): TruckBrandEntity?
}
