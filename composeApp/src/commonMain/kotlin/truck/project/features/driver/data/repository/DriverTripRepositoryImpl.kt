package truck.project.features.driver.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import truck.project.features.fleet.domain.vo.DriverPin
import truck.project.core.domain.vo.OdometerValue
import truck.project.features.driver.domain.model.Expense
import truck.project.features.driver.domain.model.Trip
import truck.project.features.driver.domain.model.TripStatus
import truck.project.features.driver.domain.repository.DriverTripRepository
import truck.project.features.trips.data.local.TripDao
import truck.project.features.trips.data.local.ExpenseDao
import truck.project.features.trips.data.local.ExpenseEntity
import truck.project.features.fleet.data.local.DriverDao
import truck.project.core.data.remote.RemoteDatabase
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DriverTripRepositoryImpl(
    private val tripDao: TripDao,
    private val expenseDao: ExpenseDao,
    private val driverDao: DriverDao,
    private val remoteDatabase: RemoteDatabase
) : DriverTripRepository {

    override suspend fun loginWithPin(pin: DriverPin): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getAssignedTrip(driverId: String): Flow<Trip?> {
        return tripDao.getAllTrips().map { list ->
            list.find { it.driverId.toString() == driverId && it.status != "COMPLETED" }?.let { entity ->
                Trip(
                    id = entity.id.toString(),
                    driverId = entity.driverId.toString(),
                    truckId = entity.truckId.toString(),
                    origin = entity.origin,
                    destination = entity.destination,
                    status = TripStatus.valueOf(entity.status)
                )
            }
        }
    }

    override suspend fun startTrip(tripId: String, startOdometer: OdometerValue): Result<Unit> {
        val trip = tripDao.getTripById(tripId.toLong()) ?: return Result.failure(Exception("Trip not found"))
        val updatedTrip = trip.copy(status = "EN_ROUTE", startTime = 123456789L)
        tripDao.update(updatedTrip)
        remoteDatabase.saveTrip(updatedTrip)
        return Result.success(Unit)
    }

    override suspend fun registerExpense(expense: Expense): Result<Unit> {
        val entity = ExpenseEntity(
            tripId = expense.tripId,
            amount = expense.amount,
            category = expense.category.name,
            timestamp = 123456789L,
            note = expense.note
        )
        expenseDao.insert(entity)
        remoteDatabase.saveExpense(entity)
        return Result.success(Unit)
    }

    override suspend fun finishTrip(
        tripId: String,
        endOdometer: OdometerValue,
        signatureUrl: String
    ): Result<Unit> {
        val trip = tripDao.getTripById(tripId.toLong()) ?: return Result.failure(Exception("Trip not found"))
        val updatedTrip = trip.copy(status = "COMPLETED", endTime = 123456789L)
        tripDao.update(updatedTrip)
        remoteDatabase.saveTrip(updatedTrip)
        return Result.success(Unit)
    }

    override suspend fun getTripHistory(driverId: String): Flow<List<Trip>> {
        return tripDao.getAllTrips().map { list ->
            list.filter { it.driverId.toString() == driverId && it.status == "COMPLETED" }
                .map { entity ->
                    Trip(
                        id = entity.id.toString(),
                        driverId = entity.driverId.toString(),
                        truckId = entity.truckId.toString(),
                        origin = entity.origin,
                        destination = entity.destination,
                        status = TripStatus.COMPLETED
                    )
                }
        }
    }
}
