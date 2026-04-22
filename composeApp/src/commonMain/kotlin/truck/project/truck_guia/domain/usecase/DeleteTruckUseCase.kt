package truck.project.truck_guia.domain.usecase

import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.repository.TruckRepository

class DeleteTruckUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke(truck: Truck) = repository.deleteTruck(truck)
}
