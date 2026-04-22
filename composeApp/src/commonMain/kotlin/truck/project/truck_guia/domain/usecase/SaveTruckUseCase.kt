package truck.project.truck_guia.domain.usecase

import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.repository.TruckRepository

class SaveTruckUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repository.saveTruck(truck)
}
