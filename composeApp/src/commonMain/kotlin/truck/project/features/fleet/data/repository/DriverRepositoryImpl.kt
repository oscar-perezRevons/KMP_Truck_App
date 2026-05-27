package truck.project.features.fleet.data.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.fleet.data.local.DriverDao
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.fleet.domain.repository.DriverRepository

class DriverRepositoryImpl(
    private val driverDao: DriverDao
) : DriverRepository {
    override fun getAllDrivers(): Flow<List<DriverEntity>> = driverDao.getAllDrivers()

    override suspend fun saveDriver(driver: DriverEntity) {
        driverDao.insert(driver)
    }

    override suspend fun deleteDriver(driver: DriverEntity) {
        driverDao.delete(driver)
    }
}
