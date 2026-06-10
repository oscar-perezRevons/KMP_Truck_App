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
    val origin: String = "",
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

    private val _state = MutableStateFlow(NewTruckState(
        currentImageUrl = "imagen1"
    ))
    val state: StateFlow<NewTruckState> = _state.asStateFlow()

    val availableOrigins = listOf(
        "LA PAZ", "COCHABAMBA", "CHUQUISACA", "SANTA CRUZ", 
        "ORURO", "POTOSI", "TARIJA", "BENI", "PANDO"
    )

    val availableModels = listOf(
        "Volvo FH", "Volvo FH16", "Volvo FM", 
        "Volvo FMX", "Volvo VM", "Volvo VNL"
    )

    fun onPlateChanged(value: String) {
        val sanitized = value.uppercase().replace(Regex("[^A-Z0-9]"), "")
        if (sanitized.length <= 7) {
            _state.update { it.copy(plate = sanitized, error = null) }
        }
    }

    fun onModelChanged(value: String) = _state.update { it.copy(model = value) }
    fun onOriginChanged(value: String) = _state.update { it.copy(origin = value) }
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
                origin = truck.origin,
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

    private fun validatePlate(plate: String): String? {
        if (plate.length != 7) return "La placa debe tener exactamente 7 caracteres."
        
        val numbers = plate.take(4)
        val letters = plate.takeLast(3)
        
        if (!numbers.all { it.isDigit() }) return "Los primeros 4 caracteres deben ser números (0-9)."
        if (!letters.all { it.isLetter() }) return "Los últimos 3 caracteres deben ser letras (A-Z)."
        
        val bannedLetters = listOf('Ñ', 'O', 'Q', 'I')
        if (letters.any { it in bannedLetters }) {
            return "No se permiten las letras Ñ, O, Q ni I en la placa."
        }
        
        return null
    }

    fun saveTruck() {
        val currentState = _state.value
        
        val plateError = validatePlate(currentState.plate)
        if (plateError != null) {
            _state.update { it.copy(error = plateError) }
            return
        }

        if (currentState.model.isBlank() || currentState.origin.isBlank() || currentState.capacity.isBlank()) {
            _state.update { it.copy(error = "Por favor completa todos los campos.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val finalImageUrl = if (currentState.isEditMode) {
                currentState.currentImageUrl
            } else {
                "imagen1"
            }
            
            val truck = Truck(
                id = currentState.id ?: Clock.System.now().toEpochMilliseconds().toString(),
                plateNumber = PlateNumber(currentState.plate),
                model = currentState.model,
                origin = currentState.origin,
                capacity = currentState.capacity.toDoubleOrNull() ?: 0.0,
                imageUrl = finalImageUrl,
                imageUrls = if (finalImageUrl != null) listOf(finalImageUrl) else emptyList()
            )
            
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
