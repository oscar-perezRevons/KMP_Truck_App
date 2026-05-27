package truck.project.features.fleet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.repository.TruckRepository
import truck.project.features.fleet.domain.vo.PlateNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TruckListState(
    val trucks: List<Truck> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TruckViewModel(
    private val repository: TruckRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TruckListState())
    val state: StateFlow<TruckListState> = _state.asStateFlow()

    init {
        observeTrucks()
    }

    private fun observeTrucks() {
        viewModelScope.launch {
            repository.getAllTrucks().collect { trucks ->
                _state.update { it.copy(trucks = trucks) }
            }
        }
    }

    fun onDeleteTruck(truck: Truck) {
        viewModelScope.launch {
            repository.deleteTruck(truck)
        }
    }

    fun onSync() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.sync()
            _state.update { it.copy(isLoading = false) }
        }
    }
}
