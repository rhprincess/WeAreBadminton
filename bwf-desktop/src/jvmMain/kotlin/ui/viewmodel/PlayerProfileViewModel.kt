package ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.uistate.PlayerProfileUiState

class PlayerProfileViewModel {

    private val _uiState = MutableStateFlow(PlayerProfileUiState())
    val uiState: StateFlow<PlayerProfileUiState> = _uiState.asStateFlow()

    companion object {
        @Composable
        fun viewModel(): PlayerProfileViewModel = remember { PlayerProfileViewModel() }
    }

    fun update(_update: (currentState: PlayerProfileUiState) -> PlayerProfileUiState) {
        _uiState.update { state ->
            _update(state)
        }
    }

}