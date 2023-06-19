package ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.bean.RankingBean
import data.payload.RankPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.uistate.WorldRankingUiState

class WorldRankingViewModel {

    private val _uiState = MutableStateFlow(WorldRankingUiState())
    val uiState: StateFlow<WorldRankingUiState> = _uiState.asStateFlow()

    companion object {
        @Composable
        fun viewModel(): WorldRankingViewModel = remember { WorldRankingViewModel() }
    }

    fun updateRankJson(json: String) {
        _uiState.update { currentState ->
            currentState.copy(rankJson = json)
        }
    }

    fun updateRankingBean(bean: RankingBean?) {
        _uiState.update { currentState ->
            currentState.copy(bean = bean)
        }
    }

    fun updateRankApiBean(bean: RankPayload) {
        _uiState.update { currentState ->
            currentState.copy(apiBean = bean)
        }
    }

    fun setLoading(loading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = loading)
        }
    }

    fun finishLoadingAnimation(finish: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(loadingAnimationFinished = finish)
        }
    }

    fun showPlayerChoices(show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(showPlayerChoices = show)
        }
    }

    fun setRankIndex(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(rankIndex = index)
        }
    }

}