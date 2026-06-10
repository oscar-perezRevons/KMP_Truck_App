package truck.project.features.auth.presentation.selection

data class SelectionState(
    val isLoading: Boolean = false
)

sealed interface SelectionIntent {
    data object SelectDriver : SelectionIntent
    data object SelectAdmin : SelectionIntent
}

sealed interface SelectionEffect {
    data object NavigateToDriverLogin : SelectionEffect
    data object NavigateToAdminLogin : SelectionEffect
}
