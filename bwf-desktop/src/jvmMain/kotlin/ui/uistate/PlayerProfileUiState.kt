package ui.uistate

import io.twinkle.wearebadminton.data.bean.BioModel

data class PlayerProfileUiState(
    val id: String = "",
    val name: String = "",
    val country: String = "",
    val worldRank: String = "...",
//    val worldTourRank: String = "...",
    val previousLoading: Boolean = true,
    val bannerImgUrl: String = "",
    val avatarUrl: String = "",
    val flagUrl: String = "",
    val lastName: String = "",
    val bioModel: BioModel? = null,
    val catId: Int = 6
)
