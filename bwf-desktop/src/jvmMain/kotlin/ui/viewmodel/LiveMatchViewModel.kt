package ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import data.bean.LiveMatchesResult
import data.bean.MatchStatResults
import ui.uistate.LiveMatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LiveMatchViewModel {
    private val _uiState = MutableStateFlow(LiveMatchUiState())
    val uiState: StateFlow<LiveMatchUiState> = _uiState.asStateFlow()

    companion object {
        @Composable
        fun viewModel(): LiveMatchViewModel = remember { LiveMatchViewModel() }
    }

    fun updateLiveMatches(matches: List<LiveMatchesResult>) {
        val lastMatches = uiState.value.matches
        _uiState.update { currentState ->
            currentState.copy(matches = matches, isRefreshing = false, lastMatches = lastMatches)
        }
        try {
            if (uiState.value.topIndex != null) {
                matches[uiState.value.topIndex!!]
                _uiState.update { currentState ->
                    currentState.copy(isSafeToStickOnTop = true)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            _uiState.update { currentState ->
                currentState.copy(topIndex = null, isSafeToStickOnTop = false)
            }
        }
    }

    fun callRefreshingState() {
        _uiState.update { currentState ->
            currentState.copy(isRefreshing = true)
        }
    }

    fun stickToTop(index: Int?) {
        _uiState.update { currentState ->
            currentState.copy(topIndex = index)
        }
    }

    fun setExpandedIndex(index: Int?) {
        _uiState.update { currentState ->
            currentState.copy(expandIndex = index)
        }
    }

    fun updateMatchStatId(matchId: Int?, tmtId: Int?) {
        _uiState.update { currentState ->
            currentState.copy(currentMatchId = matchId, currentTmtId = tmtId)
        }
    }

    fun updateMatchStatResults(matchStatResults: MatchStatResults?) {
        _uiState.update { currentState ->
            currentState.copy(matchStatResults = matchStatResults)
        }
    }
}