package io.twinkle.wearebadminton.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.twinkle.wearebadminton.data.payload.PlayerSearchPayload
import io.twinkle.wearebadminton.data.bean.PopularPlayersBean
import io.twinkle.wearebadminton.ui.uistate.PlayersUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlayersUiState())
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()

    fun setLoading(loading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = loading)
        }
    }

    fun updateSearchApiBean(bean: PlayerSearchPayload) {
        _uiState.update { currentState ->
            currentState.copy(searchApiBean = bean)
        }
    }

    fun setJson(json: String) {
        _uiState.update { currentState ->
            currentState.copy(json = json)
        }
    }

    fun updatePlayersBean(bean: PopularPlayersBean) {
        _uiState.update { currentState ->
            currentState.copy(playersBean = bean)
        }
    }

    fun updateSearchKey(key: String) {
        _uiState.update { currentState ->
            currentState.copy(searchKey = key)
        }
    }

}