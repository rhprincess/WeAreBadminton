package io.twinkle.wearebadminton.data.payload

data class RankingEventsPayload(
    val activeTab: Int = 4,
    val isPara: Boolean = false,
    val playerId: String = ""
): Payload