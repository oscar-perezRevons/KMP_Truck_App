package truck.project.features.admin.presentation.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.admin.domain.repository.StorageMode
import truck.project.features.fleet.domain.vo.PlateNumber
import kotlinx.datetime.Clock

data class NewTruckState(
    val id: String? = null,
    val plate: String = "",
    val model: String = "",
    val capacity: String = "",
    val selectedPhoto: ByteArray? = null,
    val currentImageUrl: String? = null,
    val storageMode: StorageMode = StorageMode.CLOUD,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isEditMode: Boolean = false
)

class NewTruckViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewTruckState())
    val state: StateFlow<NewTruckState> = _state.asStateFlow()

    fun onPlateChanged(value: String) = _state.update { it.copy(plate = value) }
    fun onModelChanged(value: String) = _state.update { it.copy(model = value) }
    fun onCapacityChanged(value: String) = _state.update { it.copy(capacity = value) }
    
    fun onPhotoSelected(data: ByteArray) = _state.update { 
        it.copy(selectedPhoto = data) 
    }

    fun setEditTruck(truck: Truck) {
        _state.update { 
            it.copy(
                id = truck.id,
                plate = truck.plateNumber.value,
                model = truck.model,
                capacity = truck.capacity.toString(),
                currentImageUrl = truck.imageUrl,
                isEditMode = true
            )
        }
    }

    fun loadTruckById(truckId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getTruckById(truckId)?.let { truck ->
                setEditTruck(truck)
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun saveTruck() {
        val currentState = _state.value
        if (currentState.plate.isBlank() || currentState.model.isBlank()) {
            _state.update { it.copy(error = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            var finalImageUrl = currentState.currentImageUrl

            // If a new local photo is selected, upload it
            currentState.selectedPhoto?.let { data ->
                repository.addTruckPhoto(data, "truck_${currentState.plate}_${Clock.System.now().toEpochMilliseconds()}.jpg").onSuccess {
                    finalImageUrl = it
                }.onFailure {
                    _state.update { s -> s.copy(isLoading = false, error = "Error al subir imagen: ${it.message}") }
                    return@launch
                }
            }
            
            val truck = Truck(
                id = currentState.id ?: Clock.System.now().toEpochMilliseconds().toString(),
                plateNumber = PlateNumber(currentState.plate),
                model = currentState.model,
                capacity = currentState.capacity.toDoubleOrNull() ?: 0.0,
                imageUrl = finalImageUrl,
                imageUrls = if (finalImageUrl != null) listOf(finalImageUrl!!) else emptyList()
            )
            
            // Pass the finalImageUrl in the add/updateTruck calls
            val result = if (currentState.isEditMode) {
                repository.updateTruck(truck, null, currentState.storageMode)
            } else {
                repository.addTruck(truck, null, currentState.storageMode)
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
