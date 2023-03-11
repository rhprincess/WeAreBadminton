package io.twinkle.wearebadminton.ui.uistate

import io.twinkle.wearebadminton.data.bean.LiveMatchesResult

data class LiveMatchUiState(
    val lastMatches: List<LiveMatchesResult> = listOf(),
    val matches: List<LiveMatchesResult> = listOf(),
    val isRefreshing: Boolean = false
)
