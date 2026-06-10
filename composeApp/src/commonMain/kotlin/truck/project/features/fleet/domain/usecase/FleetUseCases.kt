package truck.project.features.fleet.domain.usecase

import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.repository.TruckRepository

class GetTrucksUseCase(private val repository: TruckRepository) {
    operator fun invoke() = repository.getAllTrucks()
}

class SaveTruckUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repository.saveTruck(truck)
}

class UpdateTruckUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repository.updateTruck(truck)
}

class DeleteTruckUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repository.deleteTruck(truck)
}

class SyncTrucksUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke() = repository.sync()
}
