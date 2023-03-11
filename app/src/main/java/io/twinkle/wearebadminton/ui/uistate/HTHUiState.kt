package io.twinkle.wearebadminton.ui.uistate

import io.twinkle.wearebadminton.data.PlayerOrder
import io.twinkle.wearebadminton.data.bean.PlayerResult
import io.twinkle.wearebadminton.data.bean.PopularPlayersBean

data class HTHUiState(
    val player11: PlayerResult? = null,
    val player12: PlayerResult? = null,
    val player21: PlayerResult? = null,
    val player22: PlayerResult? = null,
    val playersBean: PopularPlayersBean? = null,
    val team1: Int = 0,
    val team2: Int = 0,
    val showPlayersDialog: Boolean = false,
    val currentOrder: PlayerOrder = PlayerOrder.TEAM_ONE_FIRST,
    val searchKey: String = "",
    val isLoading: Boolean = false,
    val player11Name: String = "",
    val player12Name: String = "",
    val player21Name: String = "",
    val player22Name: String = ""
)
