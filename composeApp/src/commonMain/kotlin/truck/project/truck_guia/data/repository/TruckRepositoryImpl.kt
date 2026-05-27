package truck.project.truck_guia.data.repository

import truck.project.data.local.TruckDao
import truck.project.data.remote.RemoteDatabase
import truck.project.data.remote.TranslationService
import truck.project.truck_guia.data.mapper.toDomain
import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import truck.project.data.local.TruckEntity

class TruckRepositoryImpl(
    private val truckDao: TruckDao,
    private val remoteDatabase: RemoteDatabase,
    private val translationService: TranslationService
) : TruckRepository {

    override fun getAllTrucks(): Flow<List<Truck>> {
        return truckDao.getAllTrucks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveTruck(truck: Truck) {
        val entity = truck.toEntity()
        val generatedId = truckDao.insert(entity)
        remoteDatabase.saveTruck(entity.copy(id = generatedId))
    }

    override suspend fun updateTruck(truck: Truck) {
        val entity = truck.toEntity()
        truckDao.update(entity)
        remoteDatabase.saveTruck(entity)
    }

    override suspend fun deleteTruck(truck: Truck) {
        val entity = truck.toEntity()
        truckDao.delete(entity)
        remoteDatabase.deleteTruck(entity.id.toString())
    }

    override suspend fun sync() {
        val remoteTrucks = remoteDatabase.getAllTrucks()
        
        remoteTrucks.forEach { remoteEntity ->
            val localEntity = truckDao.getTruckById(remoteEntity.id)
            
            if (localEntity == null || localEntity.status != remoteEntity.status) {
                // Es nuevo o cambió el status: TRADUCIR AHORA
                val translated = translationService.translate(remoteEntity.status, "en") 
                
                val finalEntity = remoteEntity.copy(
                    statusTranslated = translated,
                    needsTranslation = translated == null
                )
                
                if (localEntity == null) truckDao.insert(finalEntity) else truckDao.update(finalEntity)
            } else {
                truckDao.update(
                    remoteEntity.copy(
                        statusTranslated = localEntity.statusTranslated,
                        needsTranslation = localEntity.needsTranslation
                    )
                )
            }
        }
    }

    private fun Truck.toEntity() = TruckEntity(
        id = id,
        licensePlate = licensePlate.value,
        model = model,
        status = status,
        statusTranslated = statusTranslated,
        needsTranslation = needsTranslation
    )
}
