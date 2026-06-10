package truck.project.features.driver.data.mapper

import truck.project.features.driver.domain.model.Trip
import truck.project.features.driver.domain.model.TripStatus
import truck.project.features.driver.domain.model.Expense
import truck.project.features.driver.domain.model.ExpenseCategory
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.ExpenseEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun TripEntity.toDomain() = Trip(
    id = id,
    driverId = driverId ?: "",
    truckId = truckId ?: "",
    origin = origin,
    destination = destination,
    startLat = startLat,
    startLng = startLng,
    endLat = endLat,
    endLng = endLng,
    status = when(status) {
        "PROGRAMMED" -> TripStatus.ASSIGNED
        "EN_ROUTE" -> TripStatus.EN_ROUTE
        "COMPLETED" -> TripStatus.COMPLETED
        else -> TripStatus.ASSIGNED
    },
    currentSpeed = currentSpeed,
    currentLocation = currentLocation,
    currentLat = currentLat,
    currentLng = currentLng,
    startTime = startTime?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()) },
    endTime = endTime?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()) }
)

fun ExpenseEntity.toDomain() = Expense(
    id = id.toString(),
    tripId = tripId,
    amount = amount,
    category = ExpenseCategory.valueOf(category),
    timestamp = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault()),
    note = note
)
