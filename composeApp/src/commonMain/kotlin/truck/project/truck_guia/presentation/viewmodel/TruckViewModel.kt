package truck.project.truck_guia.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.usecase.*
import truck.project.truck_guia.domain.vo.Placa
import truck.project.truck_guia.presentation.state.TruckState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TruckViewModel(
    private val getTrucksUseCase: GetTrucksUseCase,
    private val saveTruckUseCase: SaveTruckUseCase,
    private val updateTruckUseCase: UpdateTruckUseCase,
    private val deleteTruckUseCase: DeleteTruckUseCase,
    private val syncTrucksUseCase: SyncTrucksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TruckState())
    val state: StateFlow<TruckState> = _state.asStateFlow()

    init {
        observeTrucks()
    }

    private fun observeTrucks() {
        viewModelScope.launch {
            getTrucksUseCase().collect { trucks ->
                _state.update { it.copy(trucks = trucks) }
            }
        }
    }

    fun onLicensePlateChange(value: String) {
        _state.update { it.copy(licensePlate = value) }
    }

    fun onModelChange(value: String) {
        _state.update { it.copy(model = value) }
    }

    fun onSaveTruck() {
        val currentState = _state.value
        if (currentState.licensePlate.isNotBlank() && currentState.model.isNotBlank()) {
            viewModelScope.launch {
                if (currentState.editingTruck == null) {
                    saveTruckUseCase(
                        Truck(
                            licensePlate = Placa.create(currentState.licensePlate),
                            model = currentState.model,
                            status = "Activo"
                        )
                    )
                } else {
                    updateTruckUseCase(
                        currentState.editingTruck.copy(
                            licensePlate = Placa.create(currentState.licensePlate),
                            model = currentState.model
                        )
                    )
                }
                _state.update { it.copy(licensePlate = "", model = "", editingTruck = null) }
            }
        }
    }

    fun onEditTruck(truck: Truck) {
        _state.update {
            it.copy(
                editingTruck = truck,
                licensePlate = truck.licensePlate.value,
                model = truck.model
            )
        }
    }

    fun onCancelEdit() {
        _state.update { it.copy(editingTruck = null, licensePlate = "", model = "") }
    }

    fun onDeleteTruck(truck: Truck) {
        viewModelScope.launch {
            deleteTruckUseCase(truck)
        }
    }

    fun onSync() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            syncTrucksUseCase()
            _state.update { it.copy(isLoading = false) }
        }
    }
}
