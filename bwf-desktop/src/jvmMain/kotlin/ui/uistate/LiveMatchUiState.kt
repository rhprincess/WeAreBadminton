package ui.uistate

import data.bean.LiveMatchesResult
import data.bean.MatchStatResults

data class LiveMatchUiState(
    val lastMatches: List<LiveMatchesResult> = listOf(),
    val matches: List<LiveMatchesResult> = listOf(),
    val matchStatResults: MatchStatResults? = null,
    val isRefreshing: Boolean = false,
    val topIndex: Int? = null,
    val expandIndex: Int? = null,
    val isSafeToStickOnTop: Boolean = true,
    val currentMatchId: Int? = null,
    val currentTmtId: Int? = null
)
