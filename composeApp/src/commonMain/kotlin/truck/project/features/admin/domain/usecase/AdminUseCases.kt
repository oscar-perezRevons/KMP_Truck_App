package truck.project.features.admin.domain.usecase

import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver

class LoginAdminUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(email: Email, password: Password) = repository.login(email, password)
}

class GetAdminStatsUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke() = repository.getAdminProfile()
}

class AddTruckUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(truck: Truck) = repository.addTruck(truck)
}

class AddDriverUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(driver: Driver) = repository.addDriver(driver)
}
