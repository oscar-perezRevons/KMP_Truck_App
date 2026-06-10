package truck.project.features.admin.domain.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.admin.domain.model.Admin
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.fleet.domain.model.Truck
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password

enum class StorageMode {
    LOCAL, CLOUD
}

interface AdminRepository {
    suspend fun login(email: Email, password: Password): Result<Admin>
    suspend fun register(name: String, company: String, email: Email, password: Password): Result<Admin>
    suspend fun getAdminProfile(): Flow<Admin?>
    
    fun getTrucks(): Flow<List<Truck>>
    suspend fun getTruckById(truckId: String): Truck?
    suspend fun addTruck(truck: Truck, photoData: ByteArray? = null, storageMode: StorageMode = StorageMode.CLOUD): Result<Unit>
    suspend fun updateTruck(truck: Truck, photoData: ByteArray? = null, storageMode: StorageMode = StorageMode.CLOUD): Result<Unit>
    suspend fun deleteTruck(truckId: String): Result<Unit>
    
    fun getDrivers(): Flow<List<Driver>>
    suspend fun getDriverById(driverId: String): Driver?
    suspend fun addDriver(driver: Driver, photoData: ByteArray? = null, storageMode: StorageMode = StorageMode.CLOUD): Result<Unit>
    suspend fun updateDriver(driver: Driver, photoData: ByteArray? = null, storageMode: StorageMode = StorageMode.CLOUD): Result<Unit>
    suspend fun deleteDriver(driverId: String): Result<Unit>
    suspend fun deactivateDriver(driverId: String): Result<Unit>

    suspend fun updateAdminProfile(
        name: String, 
        company: String, 
        password: Password? = null,
        photoData: ByteArray? = null
    ): Result<Unit>

    suspend fun createTrip(
        origin: String,
        destination: String,
        startLat: Double? = null,
        startLng: Double? = null,
        endLat: Double? = null,
        endLng: Double? = null,
        driverId: String? = null,
        truckId: String? = null,
        images: List<ByteArray> = emptyList(),
        storageMode: StorageMode = StorageMode.CLOUD
    ): Result<Unit>

    suspend fun updateTrip(
        tripId: String,
        origin: String,
        destination: String,
        startLat: Double? = null,
        startLng: Double? = null,
        endLat: Double? = null,
        endLng: Double? = null,
        driverId: String? = null,
        truckId: String? = null,
        status: String? = null
    ): Result<Unit>

    suspend fun deleteTrip(tripId: String): Result<Unit>
    
    suspend fun addTruckPhoto(data: ByteArray, fileName: String): Result<String>
    
    fun getTruckCount(): Flow<Int>
    fun getDriverCount(): Flow<Int>
    
    suspend fun sync(): Result<Unit>
    
    suspend fun logout()
}
