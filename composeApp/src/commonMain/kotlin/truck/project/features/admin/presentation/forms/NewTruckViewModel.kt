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
import truck.project.features.fleet.domain.vo.PlateNumber
import truck.project.core.storage.ImageStorage
import kotlinx.datetime.Clock

data class NewTruckState(
    val plate: String = "",
    val model: String = "",
    val capacity: String = "",
    val photoData: ByteArray? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class NewTruckViewModel(
    private val repository: AdminRepository,
    private val imageStorage: ImageStorage
) : ViewModel() {

    private val _state = MutableStateFlow(NewTruckState())
    val state: StateFlow<NewTruckState> = _state.asStateFlow()

    fun onPlateChanged(value: String) = _state.update { it.copy(plate = value) }
    fun onModelChanged(value: String) = _state.update { it.copy(model = value) }
    fun onCapacityChanged(value: String) = _state.update { it.copy(capacity = value) }
    fun onPhotoSelected(data: ByteArray) = _state.update { it.copy(photoData = data) }

    fun saveTruck() {
        val currentState = _state.value
        if (currentState.plate.isBlank() || currentState.model.isBlank()) {
            _state.update { it.copy(error = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            var photoUrl: String? = null
            currentState.photoData?.let { data ->
                photoUrl = imageStorage.uploadToCloud(data, "trucks/${currentState.plate}.jpg")
            }

            val truck = Truck(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                plateNumber = PlateNumber(currentState.plate),
                model = currentState.model,
                capacity = currentState.capacity.toDoubleOrNull() ?: 0.0,
                imageUrl = photoUrl
            )
            repository.addTruck(truck).onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
