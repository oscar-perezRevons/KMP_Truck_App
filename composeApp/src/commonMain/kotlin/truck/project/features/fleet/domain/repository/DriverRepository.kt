package truck.project.features.fleet.domain.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.fleet.data.local.DriverEntity

interface DriverRepository {
    fun getAllDrivers(): Flow<List<DriverEntity>>
    suspend fun saveDriver(driver: DriverEntity)
    suspend fun deleteDriver(driver: DriverEntity)
}
