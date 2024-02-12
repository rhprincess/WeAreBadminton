package ui.uistate

import data.bean.PopularPlayersBean
import data.payload.PlayerSearchPayload

data class PlayersUiState(
    val searchApiBean: PlayerSearchPayload = PlayerSearchPayload(),
    val json: String = "",
    val isLoading: Boolean = true,
    val playersBean: PopularPlayersBean? = null,
    val searchKey: String = ""
)