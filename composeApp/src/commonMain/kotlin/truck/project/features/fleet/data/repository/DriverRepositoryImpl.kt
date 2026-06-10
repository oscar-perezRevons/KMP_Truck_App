package truck.project.features.fleet.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import truck.project.features.fleet.data.local.DriverDao
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.fleet.domain.repository.DriverRepository

class DriverRepositoryImpl(
    private val driverDao: DriverDao
) : DriverRepository {
    // Note: This repository should ideally be admin-aware. 
    // For now, we use an empty string or consider it deprecated in favor of AdminRepository
    override fun getAllDrivers(): Flow<List<DriverEntity>> = driverDao.getAllDrivers("")

    override suspend fun saveDriver(driver: DriverEntity) {
        driverDao.insert(driver)
    }

    override suspend fun deleteDriver(driver: DriverEntity) {
        driverDao.delete(driver)
    }
}
