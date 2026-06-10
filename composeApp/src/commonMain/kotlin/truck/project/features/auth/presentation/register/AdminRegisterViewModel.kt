package truck.project.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password
import truck.project.features.admin.domain.repository.AdminRepository

data class AdminRegisterState(
    val name: String = "",
    val company: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AdminRegisterViewModel(
    private val repository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminRegisterState())
    val state: StateFlow<AdminRegisterState> = _state.asStateFlow()

    fun onNameChanged(value: String) = _state.update { it.copy(name = value) }
    fun onCompanyChanged(value: String) = _state.update { it.copy(company = value) }
    fun onEmailChanged(value: String) = _state.update { it.copy(email = value) }
    fun onPasswordChanged(value: String) = _state.update { it.copy(password = value) }

    fun register() {
        val currentState = _state.value
        if (currentState.name.isBlank() || currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(error = "Por favor completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.register(
                currentState.name,
                currentState.company,
                Email(currentState.email),
                Password(currentState.password)
            ).onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
