package truck.project.truck_guia.domain.usecase

import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.repository.TruckRepository
import kotlinx.coroutines.flow.Flow

class GetTrucksUseCase(private val repository: TruckRepository) {
    operator fun invoke(): Flow<List<Truck>> = repository.getAllTrucks()
}
