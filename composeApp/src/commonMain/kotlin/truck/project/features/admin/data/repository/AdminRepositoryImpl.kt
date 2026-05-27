package truck.project.features.admin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import truck.project.features.admin.domain.model.Admin
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password
import truck.project.features.fleet.data.local.TruckDao
import truck.project.features.fleet.data.local.DriverDao
import truck.project.features.fleet.data.mapper.toDomain
import truck.project.features.fleet.data.mapper.toEntity
import truck.project.core.data.remote.RemoteDatabase

class AdminRepositoryImpl(
    private val truckDao: TruckDao,
    private val driverDao: DriverDao,
    private val remoteDatabase: RemoteDatabase
) : AdminRepository {
    
    override suspend fun login(email: Email, password: Password): Result<Admin> {
        return Result.success(
            Admin(
                id = "1",
                name = "María González",
                email = email,
                company = "Transportes del Norte S.A."
            )
        )
    }

    override suspend fun register(
        name: String,
        company: String,
        email: Email,
        password: Password
    ): Result<Admin> {
        return Result.success(Admin("1", name, email, company))
    }

    override suspend fun getAdminProfile(): Flow<Admin?> {
        return flowOf(Admin("1", "María González", Email("admin@flota.com"), "Transportes del Norte S.A."))
    }

    override suspend fun getTrucks(): Flow<List<Truck>> {
        return truckDao.getAllTrucks().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addTruck(truck: Truck): Result<Unit> {
        val entity = truck.toEntity()
        truckDao.insertTruck(entity)
        remoteDatabase.saveTruck(entity)
        return Result.success(Unit)
    }

    override suspend fun getDrivers(): Flow<List<Driver>> {
        return driverDao.getAllDrivers().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addDriver(driver: Driver): Result<Unit> {
        val entity = driver.toEntity()
        driverDao.insert(entity)
        remoteDatabase.saveDriver(entity)
        return Result.success(Unit)
    }

    override fun getTruckCount(): Flow<Int> = truckDao.getTruckCount()
    override fun getDriverCount(): Flow<Int> = driverDao.getDriverCountFlow()

    override suspend fun logout() {
        // Implementation
    }
}
