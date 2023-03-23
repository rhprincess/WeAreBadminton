package data.payload

data class PlayerSearchPayload(
    val activeTab: Int = 1,
    val drawCount: Int = 1,
    val page: Int = 1,
    val searchKey: String = ""
): Payload