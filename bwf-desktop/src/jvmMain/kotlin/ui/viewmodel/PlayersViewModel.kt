package ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.bean.PopularPlayersBean
import data.payload.PlayerSearchPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.uistate.PlayersUiState

class PlayersViewModel {

    private val _uiState = MutableStateFlow(PlayersUiState())
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()

    companion object {
        @Composable
        fun viewModel(): PlayersViewModel = remember { PlayersViewModel() }
    }

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