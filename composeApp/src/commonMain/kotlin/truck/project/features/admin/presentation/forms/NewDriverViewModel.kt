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
    val dniBase: String = "",
    val dniComplement: String = "",
    val dniExtension: String = "LP",
    val licenseType: String = "Profesional B",
    val emailPrefix: String = "",
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

    val availableExtensions = listOf("LP", "SC", "CB", "OR", "PT", "CH", "TJ", "BE", "PD")
    val availableLicenses = listOf("Profesional B", "Profesional C")

    val extensionNames = mapOf(
        "LP" to "La Paz",
        "SC" to "Santa Cruz",
        "CB" to "Cochabamba",
        "OR" to "Oruro",
        "PT" to "Potosí",
        "CH" to "Chuquisaca",
        "TJ" to "Tarija",
        "BE" to "Beni",
        "PD" to "Pando"
    )

    fun onNameChanged(value: String) = _state.update { it.copy(name = value) }
    
    fun onDniBaseChanged(value: String) {
        if (value.length <= 8 && value.all { it.isDigit() }) {
            _state.update { it.copy(dniBase = value) }
        }
    }

    fun onDniComplementChanged(value: String) {
        if (value.length <= 2) {
            _state.update { it.copy(dniComplement = value.uppercase()) }
        }
    }

    fun onDniExtensionChanged(value: String) = _state.update { it.copy(dniExtension = value) }
    fun onLicenseTypeChanged(value: String) = _state.update { it.copy(licenseType = value) }
    fun onEmailPrefixChanged(value: String) = _state.update { it.copy(emailPrefix = value) }
    
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
        // Try to parse DNI back: "base[-complement] extension"
        // Example: "6755210-1A LP" or "6755210 LP"
        val dniParts = driver.dni.split(" ")
        val mainPart = dniParts.getOrNull(0) ?: ""
        val extension = dniParts.getOrNull(1) ?: "LP"
        
        val mainSplit = mainPart.split("-")
        val base = mainSplit.getOrNull(0) ?: ""
        val complement = mainSplit.getOrNull(1) ?: ""

        val emailPrefix = driver.email?.substringBefore("@") ?: ""

        _state.update { 
            it.copy(
                id = driver.id,
                name = driver.name,
                dniBase = base,
                dniComplement = complement,
                dniExtension = if (availableExtensions.contains(extension)) extension else "LP",
                licenseType = if (availableLicenses.contains(driver.licenseNumber)) driver.licenseNumber else "Profesional B",
                emailPrefix = emailPrefix,
                password = driver.password ?: "",
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
        
        // Validations
        if (currentState.name.isBlank()) {
            _state.update { it.copy(error = "El nombre es obligatorio") }
            return
        }
        
        if (currentState.dniBase.length < 4) {
            _state.update { it.copy(error = "El número base del C.I. debe tener al menos 4 dígitos") }
            return
        }

        if (currentState.emailPrefix.isBlank()) {
            _state.update { it.copy(error = "El correo electrónico es obligatorio") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val fullDni = buildString {
                append(currentState.dniBase)
                if (currentState.dniComplement.isNotBlank()) {
                    append("-")
                    append(currentState.dniComplement)
                }
                append(" ")
                append(currentState.dniExtension)
            }

            val fullEmail = "${currentState.emailPrefix}@gmail.com"
            
            val driver = Driver(
                id = currentState.id ?: Clock.System.now().toEpochMilliseconds().toString(),
                name = currentState.name,
                dni = fullDni,
                licenseNumber = currentState.licenseType,
                email = fullEmail,
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
