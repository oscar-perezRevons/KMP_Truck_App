package truck.project.features.admin.presentation.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.TripDao

data class AssignTripState(
    val selectedTrip: TripEntity? = null,
    val drivers: List<Driver> = emptyList(),
    val trucks: List<Truck> = emptyList(),
    val selectedDriverId: String? = null,
    val selectedTruckId: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AssignTripViewModel(
    private val repository: AdminRepository,
    private val tripDao: TripDao
) : ViewModel() {

    private val _state = MutableStateFlow(AssignTripState())
    val state: StateFlow<AssignTripState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.getDrivers(),
                repository.getTrucks()
            ) { drivers, trucks ->
                _state.update { it.copy(drivers = drivers, trucks = trucks) }
            }.collect()
        }
    }

    fun loadTrip(tripId: String) {
        viewModelScope.launch {
            val trip = tripDao.getTripById(tripId)
            _state.update { it.copy(selectedTrip = trip, selectedDriverId = trip?.driverId, selectedTruckId = trip?.truckId) }
        }
    }

    fun onDriverSelected(driverId: String) {
        _state.update { it.copy(selectedDriverId = driverId) }
    }

    fun onTruckSelected(truckId: String) {
        _state.update { it.copy(selectedTruckId = truckId) }
    }

    fun confirmAssignment() {
        val currentState = _state.value
        val trip = currentState.selectedTrip ?: return
        
        if (currentState.selectedDriverId == null || currentState.selectedTruckId == null) {
            _state.update { it.copy(error = "Debes asignar un conductor y un camión") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.updateTrip(
                tripId = trip.id,
                origin = trip.origin,
                destination = trip.destination,
                driverId = currentState.selectedDriverId,
                truckId = currentState.selectedTruckId,
                status = "PROGRAMMED"
            ).onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
