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
import truck.project.features.driver.domain.repository.DriverTripRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DriverTripState(
    val currentTrip: Trip? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDispatchComplete: Boolean = false,
    val isTripFinished: Boolean = false
)

class DriverTripViewModel(
    private val repository: DriverTripRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DriverTripState())
    val state: StateFlow<DriverTripState> = _state.asStateFlow()

    fun loadAssignedTrip(driverId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getAssignedTrip(driverId).collect { trip ->
                _state.update { it.copy(currentTrip = trip, isLoading = false) }
            }
        }
    }

    fun startTrip(odometer: Double) {
        val tripId = _state.value.currentTrip?.id ?: return
        viewModelScope.launch {
            repository.startTrip(tripId, OdometerValue(odometer)).onSuccess {
                _state.update { it.copy(isDispatchComplete = true) }
            }
        }
    }

    fun registerExpense(amount: Double, category: String) {
        val tripId = _state.value.currentTrip?.id ?: return
        viewModelScope.launch {
            val expense = Expense(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                tripId = tripId,
                amount = amount,
                category = ExpenseCategory.valueOf(category.uppercase()),
                timestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            repository.registerExpense(expense)
        }
    }

    fun finishTrip(odometer: Double, signature: String) {
        val tripId = _state.value.currentTrip?.id ?: return
        viewModelScope.launch {
            repository.finishTrip(tripId, OdometerValue(odometer), signature).onSuccess {
                _state.update { it.copy(isTripFinished = true) }
            }
        }
    }
}
