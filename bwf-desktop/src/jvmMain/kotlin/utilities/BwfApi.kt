package utilities

object BwfApi {

    /**
     * 国旗API
     */
    const val FLAG_URL = "https://extranet.bwfbadminton.com/docs/flags-svg/"

    /**
     * 查找运动员 / 热门运动员
     */
    const val SEARCH_PLAYERS = "https://extranet-lv.bwfbadminton.com/api/vue-popular-players"

    /**
     * 运动员信息
     */
    const val PLAYER_SUMMARY = "https://extranet-lv.bwfbadminton.com/api/vue-player-summary"

    /**
     * 运动员详情页
     */
    const val PLAYER_PAGE = "https://bwfbadminton.com/player/"

    /**
     * 上次比赛
     */
    const val MATCH_PREVIOUS = "https://extranet-lv.bwfbadminton.com/api/vue-player-match-previous"

    /**
     * 当前排名
     */
    const val CURRENT_RANKING =
        "https://extranet-lv.bwfbadminton.com/api/vue-player-ranking-current"

    /**
     * RANKING EVENTS
     */
    const val RANKING_EVENTS = "https://extranet-lv.bwfbadminton.com/api/vue-player-ranking-events"

    /**
     * PLAYER's GALLERY
     */
    const val PLAYER_GALLERY = "https://extranet-lv.bwfbadminton.com/api/vue-player-gallery"

    /**
     * BREAKING DOWN
     */
    const val BREAKING_DOWN = "https://extranet-lv.bwfbadminton.com/api/vue-rankingbreakdown"

    /**
     * Current Live
     */
    const val CURRENT_LIVE = "https://extranet-lv.bwfbadminton.com/api/vue-current-live"

    /**
     * LIVE MATCHES
     */
    const val LIVE_MATCHES = "https://extranet-lv.bwfbadminton.com/api/vue-live-matches"

    /**
     * LIVE MATCH STAT
     */
    const val LIVE_MATCH_STAT = "https://extranet-lv.bwfbadminton.com/api/vue-match-stat"

    /**
     * LIVE_GAME_GRAPH
     */
    const val LIVE_GAME_GRAPH = "https://extranet-lv.bwfbadminton.com/api/vue-game-graph"

    /**
     * Head 2 Head
     */
    fun HEAD_TO_HEAD(t1p1: String = "", t1p2: String = "", t2p1: String = "", t2p2: String = ""): String =
        "https://www.badmintoncn.com/cbo_star/star_h2h.php?t1p1=$t1p1&t1p2=$t1p2&t2p1=$t2p1&t2p2=$t2p2"

    /**
     *  All Year matches
     */
    const val ALL_YEAR_MATCHES = "https://extranet-lv.bwfbadminton.com/api/vue-cal-event-tournaments"

    /**
     * 世界排名 Api
     */
    const val WORLD_RANKING = "https://extranet-lv.bwfbadminton.com/api/vue-rankingtable"
    const val BWFAPI_AUTHORIZATION =
        "Bearer 2|NaXRu9JnMpSdb8l86BkJxj6gzKJofnhmExwr8EWkQtHoattDAGimsSYhpM22a61e1crjTjfIGTKfhzxA"
}