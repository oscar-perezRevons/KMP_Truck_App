package truck.project.features.fleet.domain.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.fleet.domain.model.Truck

interface TruckRepository {
    fun getAllTrucks(): Flow<List<Truck>>
    suspend fun saveTruck(truck: Truck)
    suspend fun updateTruck(truck: Truck)
    suspend fun deleteTruck(truck: Truck)
    suspend fun sync()
}
