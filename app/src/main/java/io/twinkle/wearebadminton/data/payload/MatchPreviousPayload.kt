package io.twinkle.wearebadminton.data.payload

data class MatchPreviousPayload(
    val activeTab: Int = 1,
    val drawCount: Int = 1,
    val isPara: Boolean = false,
    val playerId: String = "",
    val previousOffset: Int = 0
): Payload