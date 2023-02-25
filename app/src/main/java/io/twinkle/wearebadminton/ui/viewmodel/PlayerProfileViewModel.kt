package io.twinkle.wearebadminton.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.twinkle.wearebadminton.ui.uistate.PlayerProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerProfileUiState())
    val uiState: StateFlow<PlayerProfileUiState> = _uiState.asStateFlow()

    fun update(_update: (currentState: PlayerProfileUiState) -> PlayerProfileUiState) {
        _uiState.update { state ->
            _update(state)
        }
    }

}