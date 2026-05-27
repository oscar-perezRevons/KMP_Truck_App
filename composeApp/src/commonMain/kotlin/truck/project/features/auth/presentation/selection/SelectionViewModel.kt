package truck.project.features.auth.presentation.selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SelectionViewModel : ViewModel() {

    private val _state = MutableStateFlow(SelectionState())
    val state: StateFlow<SelectionState> = _state.asStateFlow()

    private val _effect = Channel<SelectionEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: SelectionIntent) {
        when (intent) {
            SelectionIntent.SelectAdmin -> {
                viewModelScope.launch { _effect.send(SelectionEffect.NavigateToAdminLogin) }
            }
            SelectionIntent.SelectDriver -> {
                viewModelScope.launch { _effect.send(SelectionEffect.NavigateToDriverLogin) }
            }
        }
    }
}
