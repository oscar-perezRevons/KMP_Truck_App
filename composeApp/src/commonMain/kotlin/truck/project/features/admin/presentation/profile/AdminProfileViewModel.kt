package truck.project.features.admin.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import truck.project.core.domain.vo.Password
import truck.project.features.admin.domain.model.Admin
import truck.project.features.admin.domain.repository.AdminRepository

data class AdminProfileState(
    val admin: Admin? = null,
    val name: String = "",
    val company: String = "",
    val password: String = "",
    val photoData: ByteArray? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val statusMessage: String? = null
)

class AdminProfileViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminProfileState())
    val state: StateFlow<AdminProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAdminProfile().collect { admin ->
                admin?.let {
                    _state.update { it.copy(admin = admin, name = admin.name, company = admin.company) }
                }
            }
        }
    }

    fun onNameChanged(value: String) = _state.update { it.copy(name = value) }
    fun onCompanyChanged(value: String) = _state.update { it.copy(company = value) }
    fun onPasswordChanged(value: String) = _state.update { it.copy(password = value) }
    fun onPhotoSelected(data: ByteArray) = _state.update { it.copy(photoData = data) }

    fun updateProfile() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, statusMessage = "Guardando cambios...") }
            val passwordVo = if (currentState.password.isNotBlank()) Password(currentState.password) else null
            repository.updateAdminProfile(currentState.name, currentState.company, passwordVo, currentState.photoData)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isSuccess = true, photoData = null, statusMessage = "Perfil actualizado exitosamente") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message, statusMessage = null) }
                }
        }
    }
}
