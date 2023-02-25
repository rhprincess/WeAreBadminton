package io.twinkle.wearebadminton.ui.uistate

import io.twinkle.wearebadminton.data.payload.PlayerSearchPayload
import io.twinkle.wearebadminton.data.bean.PopularPlayersBean

data class PlayersUiState(
    val searchApiBean: PlayerSearchPayload = PlayerSearchPayload(),
    val json: String = "",
    val isLoading: Boolean = true,
    val playersBean: PopularPlayersBean? = null,
    val searchKey: String = ""
)
