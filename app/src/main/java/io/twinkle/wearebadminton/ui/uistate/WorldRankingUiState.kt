package io.twinkle.wearebadminton.ui.uistate

import io.twinkle.wearebadminton.data.payload.RankPayload
import io.twinkle.wearebadminton.data.bean.RankingBean

data class WorldRankingUiState(
    val apiBean: RankPayload = RankPayload(),
    val rankJson: String = "",
    val bean: RankingBean? = null,
    val isLoading: Boolean = true,
    val loadingAnimationFinished: Boolean = false
)