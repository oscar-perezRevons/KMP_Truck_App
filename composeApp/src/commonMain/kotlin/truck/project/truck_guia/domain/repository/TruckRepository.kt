package truck.project.truck_guia.domain.repository

import truck.project.truck_guia.domain.model.Truck
import kotlinx.coroutines.flow.Flow

interface TruckRepository {
    fun getAllTrucks(): Flow<List<Truck>>
    suspend fun saveTruck(truck: Truck)
    suspend fun updateTruck(truck: Truck)
    suspend fun deleteTruck(truck: Truck)
    suspend fun sync()
}
