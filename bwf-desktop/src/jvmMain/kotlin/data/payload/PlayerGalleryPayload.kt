package data.payload

data class PlayerGalleryPayload(
    val activeTab: Int = 1,
    val drawCount: Int = 1,
    val extranetUrl: String = "https://extranet.bwfbadminton.com",
    val locale: String = "en",
    val playerId: String
) : Payload