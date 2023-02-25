package io.twinkle.wearebadminton.data.payload

/**
 * {"rankId":2,"catId":6,"publicationId":0,"doubles":false,"searchKey":"","pageKey":"100","page":1,"drawCount":1}
 */
data class RankPayload(
    val rankId: Int = 2,
    val catId: Int = 6,
    val publicationId: Int = 0,
    val doubles: Boolean = false,
    val searchKey: String = "",
    val pageKey: String = "100",
    val page: Int = 1,
    val drawCount: Int = 1
)
