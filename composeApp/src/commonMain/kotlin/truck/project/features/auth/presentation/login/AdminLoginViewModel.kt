package truck.project.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import truck.project.core.domain.vo.Email
import truck.project.core.domain.vo.Password
import truck.project.features.admin.domain.repository.AdminRepository

class AdminLoginViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminLoginState())
    val state: StateFlow<AdminLoginState> = _state.asStateFlow()

    private val _effect = Channel<AdminLoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: AdminLoginIntent) {
        when (intent) {
            is AdminLoginIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.value, emailError = null) }
            }
            is AdminLoginIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.value, passwordError = null) }
            }
            AdminLoginIntent.LoginClicked -> login()
            AdminLoginIntent.RegisterClicked -> {
                viewModelScope.launch { _effect.send(AdminLoginEffect.NavigateToRegister) }
            }
            AdminLoginIntent.BackClicked -> {
                viewModelScope.launch { _effect.send(AdminLoginEffect.NavigateBack) }
            }
        }
    }

    private fun login() {
        val emailValue = _state.value.email
        val passwordValue = _state.value.password

        var hasError = false
        
        if (emailValue.isBlank() || !Email.isValid(emailValue)) {
            _state.update { it.copy(emailError = "Ingrese un correo válido") }
            hasError = true
        }

        if (passwordValue.isBlank() || passwordValue.length < 4) {
             _state.update { it.copy(passwordError = "Contraseña demasiado corta (mínimo 4 caracteres)") }
             hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val result = adminRepository.login(
                Email(emailValue),
                Password(passwordValue)
            )

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
                _effect.send(AdminLoginEffect.NavigateToDashboard)
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }
}
