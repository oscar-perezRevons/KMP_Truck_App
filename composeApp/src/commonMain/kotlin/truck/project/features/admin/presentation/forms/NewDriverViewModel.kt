package truck.project.features.admin.presentation.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.fleet.domain.vo.DriverPin
import truck.project.core.storage.ImageStorage
import kotlinx.datetime.Clock

data class NewDriverState(
    val name: String = "",
    val dni: String = "",
    val license: String = "",
    val pin: String = "",
    val photoData: ByteArray? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class NewDriverViewModel(
    private val repository: AdminRepository,
    private val imageStorage: ImageStorage
) : ViewModel() {

    private val _state = MutableStateFlow(NewDriverState())
    val state: StateFlow<NewDriverState> = _state.asStateFlow()

    fun onNameChanged(value: String) = _state.update { it.copy(name = value) }
    fun onDniChanged(value: String) = _state.update { it.copy(dni = value) }
    fun onLicenseChanged(value: String) = _state.update { it.copy(license = value) }
    fun onPhotoSelected(data: ByteArray) = _state.update { it.copy(photoData = data) }
    fun onPinChanged(value: String) {
        if (value.length <= 4 && value.all { it.isDigit() }) {
            _state.update { it.copy(pin = value) }
        }
    }

    fun saveDriver() {
        val currentState = _state.value
        if (currentState.name.isBlank() || currentState.pin.length < 4) {
            _state.update { it.copy(error = "Completa los campos y usa un PIN de 4 dígitos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            var photoUrl: String? = null
            currentState.photoData?.let { data ->
                photoUrl = imageStorage.uploadToCloud(data, "drivers/${currentState.dni}.jpg")
            }

            val driver = Driver(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                name = currentState.name,
                dni = currentState.dni,
                licenseNumber = currentState.license,
                pin = DriverPin(currentState.pin),
                photoUrl = photoUrl
            )
            repository.addDriver(driver).onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
