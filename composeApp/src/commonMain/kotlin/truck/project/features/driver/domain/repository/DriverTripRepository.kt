package truck.project.features.driver.domain.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.fleet.domain.vo.DriverPin
import truck.project.core.domain.vo.OdometerValue
import truck.project.features.driver.domain.model.Expense
import truck.project.features.driver.domain.model.Trip

interface DriverTripRepository {
    suspend fun loginWithPin(pin: DriverPin): Result<Unit>
    suspend fun getAssignedTrip(driverId: String): Flow<Trip?>
    suspend fun startTrip(tripId: String, startOdometer: OdometerValue): Result<Unit>
    suspend fun registerExpense(expense: Expense): Result<Unit>
    suspend fun finishTrip(tripId: String, endOdometer: OdometerValue, signatureUrl: String): Result<Unit>
    suspend fun getTripHistory(driverId: String): Flow<List<Trip>>
}
