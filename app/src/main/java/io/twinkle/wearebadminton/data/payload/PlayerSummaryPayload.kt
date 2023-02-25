package io.twinkle.wearebadminton.data.payload

data class PlayerSummaryPayload(
    val drawCount: Int = 1,
    val isPara: Boolean = false,
    val playerId: String = ""
)