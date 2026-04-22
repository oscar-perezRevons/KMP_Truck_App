package truck.project.truck_guia.domain.usecase

import truck.project.truck_guia.domain.repository.TruckRepository

class SyncTrucksUseCase(private val repository: TruckRepository) {
    suspend operator fun invoke() = repository.sync()
}
