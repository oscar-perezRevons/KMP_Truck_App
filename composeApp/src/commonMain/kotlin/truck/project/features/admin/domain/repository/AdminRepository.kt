package truck.project.features.admin.domain.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.admin.domain.model.Admin
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.fleet.domain.model.Truck
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password

interface AdminRepository {
    suspend fun login(email: Email, password: Password): Result<Admin>
    suspend fun register(name: String, company: String, email: Email, password: Password): Result<Admin>
    suspend fun getAdminProfile(): Flow<Admin?>
    
    suspend fun getTrucks(): Flow<List<Truck>>
    suspend fun addTruck(truck: Truck): Result<Unit>
    
    suspend fun getDrivers(): Flow<List<Driver>>
    suspend fun addDriver(driver: Driver): Result<Unit>
    
    fun getTruckCount(): Flow<Int>
    fun getDriverCount(): Flow<Int>
    
    suspend fun logout()
}
