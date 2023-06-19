package ui.uistate

import data.bean.RankingBean
import data.payload.RankPayload

data class WorldRankingUiState(
    val apiBean: RankPayload = RankPayload(),
    val rankJson: String = "",
    val bean: RankingBean? = null,
    val isLoading: Boolean = true,
    val loadingAnimationFinished: Boolean = false,
    val showPlayerChoices: Boolean = false,
    val rankIndex: Int = 0
)