package io.twinkle.wearebadminton.data.payload

import kotlin.math.roundToInt

data class LiveMatchesPayload(
    val drawCount: Int = Math.random().roundToInt(),
    val tmtId: String,
    val tmtType: Int
) : Payload