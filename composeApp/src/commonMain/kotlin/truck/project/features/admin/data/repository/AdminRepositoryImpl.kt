package truck.project.features.admin.data.repository

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import truck.project.core.data.remote.RemoteDatabase
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password
import truck.project.features.admin.data.local.AdminDao
import truck.project.features.admin.data.mapper.toDomain
import truck.project.features.admin.data.mapper.toEntity
import truck.project.features.admin.domain.model.Admin
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.admin.domain.repository.StorageMode
import truck.project.features.fleet.data.local.DriverDao
import truck.project.features.fleet.data.local.TruckDao
import truck.project.features.fleet.data.mapper.toDomain
import truck.project.features.fleet.data.mapper.toEntity
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.trips.data.local.ExpenseDao
import truck.project.features.trips.data.local.TripDao
import truck.project.features.trips.data.local.TripEntity
import truck.project.core.storage.ImageStorage

class AdminRepositoryImpl(
    private val adminDao: AdminDao,
    private val truckDao: TruckDao,
    private val driverDao: DriverDao,
    private val tripDao: TripDao,
    private val expenseDao: ExpenseDao,
    private val remoteDatabase: RemoteDatabase,
    private val imageStorage: ImageStorage
) : AdminRepository {

    private suspend fun getCurrentAdminId(): String {
        return adminDao.getAdminSync()?.id ?: throw Exception("Sesión no iniciada")
    }
    
    override suspend fun login(email: Email, password: Password): Result<Admin> {
        return try {
            // Check local first
            val adminEntity = adminDao.getAdminSync()
            if (adminEntity != null && adminEntity.email == email.value) {
                if (adminEntity.password == password.value) {
                    val admin = adminEntity.toDomain()
                    sync(admin.id)
                    return Result.success(admin)
                } else {
                    return Result.failure(Exception("Contraseña incorrecta. Por favor, verifica tus datos."))
                }
            }

            // If not local or not matching, check remote
            val remoteAdmins = remoteDatabase.getAdmins()
            val remoteAdmin = remoteAdmins.find { it.email == email.value }
            
            if (remoteAdmin != null) {
                if (remoteAdmin.password == password.value) {
                    adminDao.clearAll()
                    adminDao.insert(remoteAdmin)
                    val admin = remoteAdmin.toDomain()
                    sync(admin.id)
                    Result.success(admin)
                } else {
                    Result.failure(Exception("Contraseña incorrecta. Por favor, verifica tus datos."))
                }
            } else {
                Result.failure(Exception("El usuario no existe. Regístrate para acceder."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        name: String,
        company: String,
        email: Email,
        password: Password
    ): Result<Admin> {
        return runCatching {
            val remoteAdmins = remoteDatabase.getAdmins()
            if (remoteAdmins.any { it.email == email.value }) {
                throw Exception("El correo ya está registrado por otro administrador.")
            }

            val admin = Admin(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                name = name,
                email = email,
                company = company
            )
            val entity = admin.toEntity(password.value)
            adminDao.clearAll()
            adminDao.insert(entity)
            remoteDatabase.saveAdmin(entity)
            admin
        }
    }

    override suspend fun getAdminProfile(): Flow<Admin?> {
        return adminDao.getAdminProfile().map { it?.toDomain() }
    }

    override fun getTrucks(): Flow<List<Truck>> {
        return adminDao.getAdminProfile().flatMapLatest { admin ->
            if (admin == null) flowOf(emptyList()) 
            else truckDao.getAllTrucks(admin.id).map { list -> list.map { it.toDomain() } }
        }
    }

    override suspend fun getTruckById(truckId: String): Truck? {
        return truckDao.getTruckById(truckId)?.toDomain()
    }

    override suspend fun addTruck(truck: Truck, photoData: ByteArray?, storageMode: StorageMode): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            var imageUrl = truck.imageUrl
            photoData?.let { data ->
                imageUrl = if (storageMode == StorageMode.CLOUD) {
                    imageStorage.uploadToCloud(data, "trucks/${adminId}_${truck.plateNumber.value}.jpg")
                } else {
                    imageStorage.saveImageLocally(data, "trucks_${adminId}_${truck.plateNumber.value}.jpg")
                }
            }
            
            val entity = truck.copy(adminId = adminId, imageUrl = imageUrl).toEntity()
            truckDao.insertTruck(entity)
            remoteDatabase.saveTruck(entity)
        }
    }

    override suspend fun updateTruck(truck: Truck, photoData: ByteArray?, storageMode: StorageMode): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            var imageUrl = truck.imageUrl
            photoData?.let { data ->
                imageUrl = if (storageMode == StorageMode.CLOUD) {
                    imageStorage.uploadToCloud(data, "trucks/${adminId}_${truck.plateNumber.value}.jpg")
                } else {
                    imageStorage.saveImageLocally(data, "trucks_${adminId}_${truck.plateNumber.value}.jpg")
                }
            }
            
            val entity = truck.copy(adminId = adminId, imageUrl = imageUrl).toEntity()
            truckDao.update(entity)
            remoteDatabase.saveTruck(entity)
        }
    }

    override suspend fun deleteTruck(truckId: String): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            truckDao.deleteTruckById(truckId)
            remoteDatabase.deleteTruck(adminId, truckId)
        }
    }

    override fun getDrivers(): Flow<List<Driver>> {
        return adminDao.getAdminProfile().flatMapLatest { admin ->
            if (admin == null) flowOf(emptyList())
            else driverDao.getAllDrivers(admin.id).map { list -> list.map { it.toDomain() } }
        }
    }

    override suspend fun getDriverById(driverId: String): Driver? {
        return driverDao.getDriverById(driverId)?.toDomain()
    }

    override suspend fun addDriver(driver: Driver, photoData: ByteArray?, storageMode: StorageMode): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            var photoUrl = driver.photoUrl
            photoData?.let { data ->
                photoUrl = if (storageMode == StorageMode.CLOUD) {
                    imageStorage.uploadToCloud(data, "drivers/${adminId}_${driver.dni}.jpg")
                } else {
                    imageStorage.saveImageLocally(data, "drivers_${adminId}_${driver.dni}.jpg")
                }
            }
            
            val entity = driver.copy(adminId = adminId, photoUrl = photoUrl).toEntity()
            driverDao.insert(entity)
            remoteDatabase.saveDriver(entity)
        }
    }

    override suspend fun updateDriver(driver: Driver, photoData: ByteArray?, storageMode: StorageMode): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            var photoUrl = driver.photoUrl
            photoData?.let { data ->
                photoUrl = if (storageMode == StorageMode.CLOUD) {
                    imageStorage.uploadToCloud(data, "drivers/${adminId}_${driver.dni}.jpg")
                } else {
                    imageStorage.saveImageLocally(data, "drivers_${adminId}_${driver.dni}.jpg")
                }
            }
            
            val entity = driver.copy(adminId = adminId, photoUrl = photoUrl).toEntity()
            driverDao.update(entity)
            remoteDatabase.saveDriver(entity)
        }
    }

    override suspend fun deleteDriver(driverId: String): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            driverDao.deleteDriverById(driverId)
            remoteDatabase.deleteDriver(adminId, driverId)
        }
    }

    override suspend fun deactivateDriver(driverId: String): Result<Unit> {
        return runCatching {
            val driver = driverDao.getDriverById(driverId)
            if (driver != null) {
                val updated = driver.copy(isActive = false)
                driverDao.update(updated)
                remoteDatabase.saveDriver(updated)
            }
        }
    }

    override suspend fun createTrip(
        origin: String,
        destination: String,
        startLat: Double?,
        startLng: Double?,
        endLat: Double?,
        endLng: Double?,
        driverId: String?,
        truckId: String?,
        images: List<ByteArray>,
        storageMode: StorageMode
    ): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            val timestamp = Clock.System.now().toEpochMilliseconds()
            val uploadedUrls: List<String> = images.mapIndexed { index, data ->
                if (storageMode == StorageMode.CLOUD) {
                    imageStorage.uploadToCloud(data, "trips/${adminId}_${timestamp}_$index.jpg")
                } else {
                    imageStorage.saveImageLocally(data, "trips_${adminId}_${timestamp}_$index.jpg")
                }
            }

            val isAssigned = driverId != null && truckId != null

            val tripEntity = TripEntity(
                id = timestamp.toString(),
                adminId = adminId,
                driverId = driverId,
                truckId = truckId,
                origin = origin,
                destination = destination,
                startLat = startLat,
                startLng = startLng,
                endLat = endLat,
                endLng = endLng,
                status = if (isAssigned) "PROGRAMMED" else "PLANNED",
                imageUrls = if (uploadedUrls.isEmpty()) null else uploadedUrls.joinToString(",")
            )
            tripDao.insert(tripEntity)
            remoteDatabase.saveTrip(tripEntity)
        }
    }

    override suspend fun updateTrip(
        tripId: String,
        origin: String,
        destination: String,
        startLat: Double?,
        startLng: Double?,
        endLat: Double?,
        endLng: Double?,
        driverId: String?,
        truckId: String?,
        status: String?
    ): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            val current = tripDao.getTripById(tripId)
                ?: throw Exception("Trip not found")
            
            val newStatus = if (status != null) status 
                            else if (driverId != null && truckId != null && current.status == "PLANNED") "PROGRAMMED"
                            else current.status

            val updated = current.copy(
                origin = origin,
                destination = destination,
                startLat = startLat ?: current.startLat,
                startLng = startLng ?: current.startLng,
                endLat = endLat ?: current.endLat,
                endLng = endLng ?: current.endLng,
                driverId = driverId ?: current.driverId,
                truckId = truckId ?: current.truckId,
                status = newStatus
            )
            tripDao.update(updated)
            remoteDatabase.saveTrip(updated)
        }
    }

    override suspend fun deleteTrip(tripId: String): Result<Unit> {
        return runCatching {
            tripDao.deleteById(tripId)
            // remoteDatabase.deleteTrip(tripId)
        }
    }

    override suspend fun addTruckPhoto(data: ByteArray, fileName: String): Result<String> {
        return runCatching {
            val adminId = getCurrentAdminId()
            imageStorage.uploadToCloud(data, "trucks/${adminId}_$fileName")
        }
    }

    override suspend fun updateAdminProfile(
        name: String, 
        company: String, 
        password: Password?,
        photoData: ByteArray?
    ): Result<Unit> {
        return runCatching {
            val current = adminDao.getAdminSync() ?: throw Exception("No admin found")
            
            var profileUrl = current.profileImageUrl
            photoData?.let { data ->
                profileUrl = imageStorage.saveImageLocally(data, "admin_profile_${current.id}.jpg")
            }

            val updated = current.copy(
                name = name,
                company = company,
                password = password?.value ?: current.password,
                profileImageUrl = profileUrl
            )
            adminDao.updateSync(updated)
            remoteDatabase.saveAdmin(updated)
        }
    }

    override fun getTruckCount(): Flow<Int> {
        return adminDao.getAdminProfile().flatMapLatest { admin ->
            if (admin == null) flowOf(0)
            else truckDao.getTruckCount(admin.id)
        }
    }

    override fun getDriverCount(): Flow<Int> {
        return adminDao.getAdminProfile().flatMapLatest { admin ->
            if (admin == null) flowOf(0)
            else driverDao.getDriverCountFlow(admin.id)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun sync(): Result<Unit> {
        return runCatching {
            val adminId = getCurrentAdminId()
            sync(adminId)
        }
    }

    private fun sync(adminId: String) {
        GlobalScope.launch {
            remoteDatabase.observeTrucks(adminId).collect { trucks ->
                trucks.forEach { truckDao.insertTruck(it) }
            }
        }
        
        GlobalScope.launch {
            remoteDatabase.observeTrips(adminId).collect { trips ->
                trips.forEach { tripDao.insert(it) }
            }
        }

        GlobalScope.launch {
            remoteDatabase.getAllDrivers(adminId).forEach { driverDao.insert(it) }
            remoteDatabase.getAllTrucks(adminId).forEach { truckDao.insertTruck(it) }
        }
    }

    override suspend fun logout() {
        truckDao.clearAll()
        driverDao.clearAll()
        tripDao.clearAll()
        expenseDao.clearAll()
        adminDao.clearAll()
    }
}
