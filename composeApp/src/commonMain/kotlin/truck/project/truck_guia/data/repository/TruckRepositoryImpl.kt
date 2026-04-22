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

// Nota: Necesitamos obtener el idioma. Como estamos en Common, 
// lo ideal es pasar el idioma al sync o tener un LanguageProvider.
// Por ahora usaremos "en" por defecto o lo que detectemos.

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
        // Al guardar localmente, intentamos traducir de inmediato
        // Asumimos 'en' como destino común o podrías inyectar el idioma actual
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
                    needsTranslation = translated == null // Si falla, que lo intente el Worker luego
                )
                
                if (localEntity == null) truckDao.insert(finalEntity) else truckDao.update(finalEntity)
            } else {
                // No cambió el status: Mantener lo que ya tenemos
                truckDao.update(
                    remoteEntity.copy(
                        statusTranslated = localEntity.statusTranslated,
                        needsTranslation = localEntity.needsTranslation
                    )
                )
            }
        }
    }

    // Helper para mapear (asegúrate de que toEntity existe en tus mappers o úsalo aquí)
    private fun Truck.toEntity() = TruckEntity(
        id = id,
        licensePlate = licensePlate,
        model = model,
        status = status,
        statusTranslated = statusTranslated,
        needsTranslation = needsTranslation
    )
}
