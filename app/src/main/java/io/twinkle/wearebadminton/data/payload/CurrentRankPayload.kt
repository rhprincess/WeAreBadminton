package io.twinkle.wearebadminton.data.payload

data class CurrentRankPayload(
    val isPara: Boolean = false,
    val playerId: String = "",
    val rankingEvent: String = "6-0"
)