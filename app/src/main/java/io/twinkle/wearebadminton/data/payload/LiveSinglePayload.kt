package io.twinkle.wearebadminton.data.payload

import kotlin.math.roundToInt

data class LiveSinglePayload(
    val drawCount: Int = Math.random().roundToInt(),
    val matchId: String,
    val tmtId: String
): Payload