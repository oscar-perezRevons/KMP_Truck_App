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
import truck.project.features.auth.domain.repository.AuthRepository

class AdminLoginViewModel(
    private val authRepository: AuthRepository
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
            AdminLoginIntent.BackClicked -> {
                viewModelScope.launch { _effect.send(AdminLoginEffect.NavigateBack) }
            }
        }
    }

    private fun login() {
        val emailValue = _state.value.email
        val passwordValue = _state.value.password

        var hasError = false
        
        if (!Email.isValid(emailValue)) {
            _state.update { it.copy(emailError = "Correo electrónico inválido") }
            hasError = true
        }

        if (passwordValue.length < 6) {
             _state.update { it.copy(passwordError = "Contraseña demasiado corta") }
             hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val result = authRepository.loginAdmin(
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
