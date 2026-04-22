package truck.project.data.remote

import truck.project.data.local.TruckEntity

interface RemoteDatabase {
    suspend fun saveTruck(truck: TruckEntity)
    suspend fun deleteTruck(truckId: String)
    suspend fun getAllTrucks(): List<TruckEntity>
}