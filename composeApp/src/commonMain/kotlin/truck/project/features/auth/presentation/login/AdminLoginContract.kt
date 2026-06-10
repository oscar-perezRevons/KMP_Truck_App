package truck.project.features.auth.presentation.login

data class AdminLoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed interface AdminLoginIntent {
    data class EmailChanged(val value: String) : AdminLoginIntent
    data class PasswordChanged(val value: String) : AdminLoginIntent
    data object LoginClicked : AdminLoginIntent
    data object RegisterClicked : AdminLoginIntent
    data object BackClicked : AdminLoginIntent
}

sealed interface AdminLoginEffect {
    data object NavigateToDashboard : AdminLoginEffect
    data object NavigateToRegister : AdminLoginEffect
    data object NavigateBack : AdminLoginEffect
}
