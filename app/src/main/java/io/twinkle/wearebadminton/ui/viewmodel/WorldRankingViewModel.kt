package io.twinkle.wearebadminton.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.twinkle.wearebadminton.data.payload.RankPayload
import io.twinkle.wearebadminton.data.bean.RankingBean
import io.twinkle.wearebadminton.ui.uistate.WorldRankingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WorldRankingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WorldRankingUiState())
    val uiState: StateFlow<WorldRankingUiState> = _uiState.asStateFlow()

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

}