package truck.project.features.driver.presentation.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import truck.project.core.domain.vo.OdometerValue
import truck.project.features.driver.domain.model.Expense
import truck.project.features.driver.domain.model.ExpenseCategory
import truck.project.features.driver.domain.model.Trip
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.driver.domain.repository.DriverTripRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DriverTripState(
    val currentTrip: Trip? = null,
    val assignedTruck: Truck? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDispatchComplete: Boolean = false,
    val isTripFinished: Boolean = false,
    val loginSuccess: Boolean = false,
    val loggedDriverId: String? = null,
    val driverProfile: Driver? = null,
    val profileUpdateSuccess: Boolean = false,
    val notificationMessage: String? = null,
    val expenses: List<Expense> = emptyList(),
    val totalExpenses: Double = 0.0,
    val currentSpeed: Double = 0.0
)

class DriverTripViewModel(
    private val repository: DriverTripRepository
) : ViewModel() {
    
    // ... rest of the class

    private val _state = MutableStateFlow(DriverTripState())
    val state: StateFlow<DriverTripState> = _state.asStateFlow()

    init {
        syncData()
    }

    private fun syncData() {
        viewModelScope.launch {
            repository.sync()
        }
    }

    fun loginWithCredentials(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.loginWithCredentials(email, password).onSuccess { driverId ->
                _state.update { it.copy(isLoading = false, loginSuccess = true, loggedDriverId = driverId) }
                loadAssignedTrip(driverId)
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Login fallido") }
            }
        }
    }

    fun loadAssignedTrip(driverId: String) {
        _state.update { it.copy(loggedDriverId = driverId, isLoading = true) }
        viewModelScope.launch {
            repository.getAssignedTrip(driverId).collect { trip ->
                if (trip != null) {
                    repository.getTruckById(trip.truckId).onSuccess { truck ->
                        _state.update { it.copy(
                            currentTrip = trip, 
                            assignedTruck = truck, 
                            isLoading = false,
                            currentSpeed = trip.currentSpeed
                        ) }
                    }.onFailure {
                        _state.update { it.copy(
                            currentTrip = trip, 
                            isLoading = false,
                            currentSpeed = trip.currentSpeed
                        ) }
                    }
                    
                    // Observe expenses for this trip
                    repository.getExpensesByTrip(trip.id).collect { expenses ->
                        _state.update { it.copy(
                            expenses = expenses,
                            totalExpenses = expenses.sumOf { e -> e.amount }
                        ) }
                    }
                } else {
                    _state.update { it.copy(currentTrip = null, assignedTruck = null, isLoading = false, expenses = emptyList(), totalExpenses = 0.0) }
                }
            }
        }
    }

    fun loadDriverProfile(driverId: String) {
        _state.update { it.copy(loggedDriverId = driverId) }
        viewModelScope.launch {
            repository.getDriverProfile(driverId).onSuccess { profile ->
                _state.update { it.copy(driverProfile = profile) }
            }
        }
    }

    fun updateProfile(name: String, password: String?, photoData: ByteArray?) {
        val driverId = _state.value.loggedDriverId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.updateDriverProfile(driverId, name, password, photoData).onSuccess {
                _state.update { it.copy(isLoading = false, profileUpdateSuccess = true) }
                loadDriverProfile(driverId)
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun startTrip(odometer: Double) {
        val trip = _state.value.currentTrip ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.startTrip(trip.id, OdometerValue(odometer)).onSuccess {
                syncData() // Refresh
                _state.update { it.copy(
                    isDispatchComplete = true, 
                    isLoading = false,
                    notificationMessage = "¡VIAJE INICIADO! Ruta hacia ${trip.destination}"
                ) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun clearNotification() {
        _state.update { it.copy(notificationMessage = null) }
    }

    fun registerExpense(amount: Double, category: String, note: String? = null, photoData: ByteArray? = null) {
        val tripId = _state.value.currentTrip?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val expense = Expense(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                tripId = tripId,
                amount = amount,
                category = try { ExpenseCategory.valueOf(category.uppercase()) } catch(e: Exception) { ExpenseCategory.OTHER },
                timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                note = note
            )
            repository.registerExpense(expense, photoData).onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            repository.deleteExpense(expenseId)
        }
    }

    fun updateSpeed(speed: Double) {
        val tripId = _state.value.currentTrip?.id ?: return
        _state.update { it.copy(currentSpeed = speed) }
        viewModelScope.launch {
            repository.updateTelemetry(tripId, speed, null)
        }
    }

    fun updateLocation(location: String) {
        val tripId = _state.value.currentTrip?.id ?: return
        viewModelScope.launch {
            repository.updateTelemetry(tripId, _state.value.currentSpeed, location)
        }
    }

    fun finishTrip(odometer: Double, signature: String) {
        val tripId = _state.value.currentTrip?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.finishTrip(tripId, OdometerValue(odometer), signature).onSuccess {
                _state.update { it.copy(isTripFinished = true, isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
