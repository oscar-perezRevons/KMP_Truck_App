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
import truck.project.features.admin.domain.repository.StorageMode
import truck.project.core.storage.ImageStorage
import kotlinx.datetime.Clock

data class NewDriverState(
    val id: String? = null,
    val name: String = "",
    val dni: String = "",
    val license: String = "",
    val email: String = "",
    val password: String = "",
    val photoDataList: List<ByteArray> = emptyList(),
    val photoUrls: List<String> = emptyList(),
    val currentUrlInput: String = "",
    val storageMode: StorageMode = StorageMode.CLOUD,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isEditMode: Boolean = false
)

class NewDriverViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewDriverState())
    val state: StateFlow<NewDriverState> = _state.asStateFlow()

    fun onNameChanged(value: String) = _state.update { it.copy(name = value) }
    fun onDniChanged(value: String) = _state.update { it.copy(dni = value) }
    fun onLicenseChanged(value: String) = _state.update { it.copy(license = value) }
    fun onEmailChanged(value: String) = _state.update { it.copy(email = value) }
    fun onPasswordChanged(value: String) = _state.update { it.copy(password = value) }
    fun onPhotoSelected(data: ByteArray) = _state.update { it.copy(photoDataList = it.photoDataList + data) }
    fun onUrlInputChanged(value: String) = _state.update { it.copy(currentUrlInput = value) }
    fun addUrl() = _state.update { 
        if (it.currentUrlInput.isNotBlank()) {
            it.copy(photoUrls = it.photoUrls + it.currentUrlInput, currentUrlInput = "")
        } else it
    }
    fun onStorageModeChanged(mode: StorageMode) = _state.update { it.copy(storageMode = mode) }

    fun setEditDriver(driver: Driver) {
        _state.update { 
            it.copy(
                id = driver.id,
                name = driver.name,
                dni = driver.dni,
                license = driver.licenseNumber,
                email = driver.email ?: "",
                photoUrls = driver.photoUrls + listOfNotNull(driver.photoUrl),
                isEditMode = true
            )
        }
    }

    fun loadDriverById(driverId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getDriverById(driverId)?.let { driver ->
                setEditDriver(driver)
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun saveDriver() {
        val currentState = _state.value
        if (currentState.name.isBlank() || currentState.email.isBlank()) {
            _state.update { it.copy(error = "Por favor completa los campos básicos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val driver = Driver(
                id = currentState.id ?: Clock.System.now().toEpochMilliseconds().toString(),
                name = currentState.name,
                dni = currentState.dni,
                licenseNumber = currentState.license,
                email = currentState.email,
                password = currentState.password.ifBlank { null },
                photoUrl = currentState.photoUrls.firstOrNull(),
                photoUrls = currentState.photoUrls
            )
            
            val result = if (currentState.isEditMode) {
                repository.updateDriver(driver, currentState.photoDataList.firstOrNull(), currentState.storageMode)
            } else {
                repository.addDriver(driver, currentState.photoDataList.firstOrNull(), currentState.storageMode)
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
