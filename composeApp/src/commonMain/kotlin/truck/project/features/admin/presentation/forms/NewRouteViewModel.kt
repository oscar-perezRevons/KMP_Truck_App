package truck.project.features.admin.presentation.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.admin.domain.repository.StorageMode

data class NewRouteState(
    val id: String? = null,
    val origin: String = "",
    val destination: String = "",
    val startLat: Double? = null,
    val startLng: Double? = null,
    val endLat: Double? = null,
    val endLng: Double? = null,
    val driverId: String? = null,
    val truckId: String? = null,
    val images: List<ByteArray> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val currentUrlInput: String = "",
    val storageMode: StorageMode = StorageMode.CLOUD,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false
)

class NewRouteViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewRouteState())
    val state: StateFlow<NewRouteState> = _state.asStateFlow()

    fun onOriginChanged(value: String) = _state.update { it.copy(origin = value) }
    fun onDestinationChanged(value: String) = _state.update { it.copy(destination = value) }
    fun onStartPointSelected(lat: Double, lng: Double, address: String) = _state.update { 
        it.copy(startLat = lat, startLng = lng, origin = address) 
    }
    fun onEndPointSelected(lat: Double, lng: Double, address: String) = _state.update { 
        it.copy(endLat = lat, endLng = lng, destination = address) 
    }
    fun onDriverChanged(value: String?) = _state.update { it.copy(driverId = value) }
    fun onTruckChanged(value: String?) = _state.update { it.copy(truckId = value) }
    fun onAddImage(data: ByteArray) = _state.update { it.copy(images = it.images + data) }
    fun onUrlInputChanged(value: String) = _state.update { it.copy(currentUrlInput = value) }
    fun addUrl() = _state.update { 
        if (it.currentUrlInput.isNotBlank()) {
            it.copy(imageUrls = it.imageUrls + it.currentUrlInput, currentUrlInput = "")
        } else it
    }
    fun onStorageModeChanged(mode: StorageMode) = _state.update { it.copy(storageMode = mode) }

    fun setEditTrip(tripId: String, origin: String, destination: String) {
        _state.update { 
            it.copy(
                id = tripId,
                origin = origin,
                destination = destination,
                isEditMode = true
            )
        }
    }

    fun assignTrip() {
        val currentState = _state.value
        if (currentState.origin.isBlank() || currentState.destination.isBlank()) {
            _state.update { it.copy(error = "Por favor ingresa origen y destino") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val result = if (currentState.isEditMode) {
                repository.updateTrip(
                    tripId = currentState.id ?: "",
                    origin = currentState.origin,
                    destination = currentState.destination,
                    startLat = currentState.startLat,
                    startLng = currentState.startLng,
                    endLat = currentState.endLat,
                    endLng = currentState.endLng,
                    driverId = currentState.driverId,
                    truckId = currentState.truckId
                )
            } else {
                repository.createTrip(
                    origin = currentState.origin,
                    destination = currentState.destination,
                    startLat = currentState.startLat,
                    startLng = currentState.startLng,
                    endLat = currentState.endLat,
                    endLng = currentState.endLng,
                    driverId = currentState.driverId,
                    truckId = currentState.truckId,
                    images = currentState.images,
                    storageMode = currentState.storageMode
                )
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
