package truck.project.features.driver.domain.usecase

import truck.project.features.fleet.domain.vo.DriverPin
import truck.project.core.domain.vo.OdometerValue
import truck.project.features.driver.domain.model.Expense
import truck.project.features.driver.domain.repository.DriverTripRepository

class DriverLoginUseCase(private val repository: DriverTripRepository) {
    suspend operator fun invoke(pin: DriverPin) = repository.loginWithPin(pin)
}

class GetAssignedTripUseCase(private val repository: DriverTripRepository) {
    suspend operator fun invoke(driverId: String) = repository.getAssignedTrip(driverId)
}

class StartTripUseCase(private val repository: DriverTripRepository) {
    suspend operator fun invoke(tripId: String, odometer: OdometerValue) = repository.startTrip(tripId, odometer)
}

class RegisterExpenseUseCase(private val repository: DriverTripRepository) {
    suspend operator fun invoke(expense: Expense) = repository.registerExpense(expense)
}

class FinishTripUseCase(private val repository: DriverTripRepository) {
    suspend operator fun invoke(tripId: String, odometer: OdometerValue, signature: String) = 
        repository.finishTrip(tripId, odometer, signature)
}
