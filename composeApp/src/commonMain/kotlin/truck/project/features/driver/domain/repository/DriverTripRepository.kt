package truck.project.features.driver.domain.repository

import kotlinx.coroutines.flow.Flow
import truck.project.features.fleet.domain.vo.DriverPin
import truck.project.core.domain.vo.OdometerValue
import truck.project.features.driver.domain.model.Expense
import truck.project.features.driver.domain.model.Trip

interface DriverTripRepository {
    suspend fun loginWithPin(pin: DriverPin): Result<Unit>
    suspend fun loginWithCredentials(email: String, password: String): Result<String>
    
    suspend fun getAssignedTrip(driverId: String): Flow<Trip?>
    
    suspend fun getTruckById(truckId: String): Result<truck.project.features.fleet.domain.model.Truck>
    
    suspend fun getDriverProfile(driverId: String): Result<truck.project.features.fleet.domain.model.Driver>
    suspend fun updateDriverProfile(driverId: String, name: String, password: String?, photoData: ByteArray?): Result<Unit>

    suspend fun startTrip(tripId: String, startOdometer: OdometerValue): Result<Unit>
    suspend fun registerExpense(expense: Expense, photoData: ByteArray? = null): Result<Unit>
    suspend fun updateExpense(expense: Expense): Result<Unit>
    suspend fun deleteExpense(expenseId: String): Result<Unit>
    fun getExpensesByTrip(tripId: String): Flow<List<Expense>>
    suspend fun finishTrip(tripId: String, endOdometer: OdometerValue, signatureUrl: String): Result<Unit>
    suspend fun updateTelemetry(tripId: String, speed: Double, location: String? = null): Result<Unit>
    suspend fun getTripHistory(driverId: String): Flow<List<Trip>>
    
    suspend fun sync(): Result<Unit>
}
