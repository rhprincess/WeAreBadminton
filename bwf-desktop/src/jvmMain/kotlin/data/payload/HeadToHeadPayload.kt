package data.payload

import kotlin.math.roundToInt

data class HeadToHeadPayload(
    val drawCount: Int = Math.random().roundToInt(),
    val player11: Int,
    val player12: Int?,
    val player21: Int,
    val player22: Int?
): Payload