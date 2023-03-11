package io.twinkle.wearebadminton.ui.viewmodel

import androidx.lifecycle.ViewModel
import io.twinkle.wearebadminton.data.bean.LiveMatchesResult
import io.twinkle.wearebadminton.ui.uistate.LiveMatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LiveMatchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LiveMatchUiState())
    val uiState: StateFlow<LiveMatchUiState> = _uiState.asStateFlow()

    fun updateLiveMatches(matches: List<LiveMatchesResult>) {
        val lastMatches = uiState.value.matches
        _uiState.update { currentState ->
            currentState.copy(matches = matches, isRefreshing = false, lastMatches = lastMatches)
        }
    }

    fun callRefreshingState() {
        _uiState.update { currentState ->
            currentState.copy(isRefreshing = true)
        }
    }
}