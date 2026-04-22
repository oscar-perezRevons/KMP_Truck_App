package truck.project.truck_guia.domain.usecase

import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.repository.TruckRepository

class UpdateTruckUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repository.updateTruck(truck)
}
