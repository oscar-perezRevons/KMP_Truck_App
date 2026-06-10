package truck.project.features.fleet.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import truck.project.core.data.remote.RemoteDatabase
import truck.project.core.data.remote.TranslationService
import truck.project.features.fleet.data.local.TruckDao
import truck.project.features.fleet.data.mapper.toDomain
import truck.project.features.fleet.data.mapper.toEntity
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.repository.TruckRepository

class TruckRepositoryImpl(
    private val truckDao: TruckDao,
    private val remoteDatabase: RemoteDatabase,
    private val translationService: TranslationService
) : TruckRepository {

    override fun getAllTrucks(): Flow<List<Truck>> {
        return truckDao.getAllTrucks("").map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveTruck(truck: Truck) {
        val entity = truck.toEntity()
        truckDao.insertTruck(entity)
        remoteDatabase.saveTruck(entity)
    }

    override suspend fun updateTruck(truck: Truck) {
        val entity = truck.toEntity()
        truckDao.update(entity)
        remoteDatabase.saveTruck(entity)
    }

    override suspend fun deleteTruck(truck: Truck) {
        val entity = truck.toEntity()
        truckDao.delete(entity)
        remoteDatabase.deleteTruck("", entity.id)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun sync() {
        // Start observing in real-time
        GlobalScope.launch {
            remoteDatabase.observeTrucks("").collect { remoteTrucks ->
                remoteTrucks.forEach { remoteEntity ->
                    val localEntity = truckDao.getTruckById(remoteEntity.id)
                    if (localEntity == null || localEntity.status != remoteEntity.status) {
                        truckDao.insertTruck(remoteEntity)
                    }
                }
            }
        }
    }
}
