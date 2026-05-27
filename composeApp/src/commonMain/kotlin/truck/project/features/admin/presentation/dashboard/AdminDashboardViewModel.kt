package truck.project.features.admin.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.trips.data.local.TripDao

class AdminDashboardViewModel(
    private val repository: AdminRepository,
    private val tripDao: TripDao
) : ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state: StateFlow<AdminDashboardState> = _state.asStateFlow()

    private val _effect = Channel<AdminDashboardEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadStats()
    }

    fun onIntent(intent: AdminDashboardIntent) {
        when (intent) {
            AdminDashboardIntent.LoadStats -> loadStats()
            AdminDashboardIntent.NavigateToFleet -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToFleet) }
            }
            AdminDashboardIntent.NavigateToDrivers -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToDrivers) }
            }
            AdminDashboardIntent.NavigateToTrips -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToTrips) }
            }
            AdminDashboardIntent.Logout -> {
                viewModelScope.launch { _effect.send(AdminDashboardEffect.NavigateToLogin) }
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            combine(
                repository.getTruckCount(),
                repository.getDriverCount(),
                flowOf(0) // active trips count flow - could be added to tripDao
            ) { trucks, drivers, trips ->
                AdminDashboardState(
                    truckCount = trucks,
                    driverCount = drivers,
                    activeTripsCount = trips
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
}
