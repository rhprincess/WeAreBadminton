package io.twinkle.wearebadminton.utilities

object BwfApi {

    /**
     * 第一队选手id
     * t1p1memberid=
     * t1p2memberid=
     *
     * 第二队选手id
     * t2p1memberid=
     * t2p2memberid=
     */
    val HEAD_TO_HEAD: (t1p1: String, t1p2: String, t2p1: String, t2p2: String) -> String =
        { t1p1, t1p2, t2p1, t2p2 ->
            "https://bwf.tournamentsoftware.com/head-2-head/Head2HeadContent?OrganizationCode=209B123F-AA87-41A2-BC3E-CB57133E64CC&t1p1memberid=$t1p1&t1p2memberid=$t1p2&t2p1memberid=$t2p1&t2p2memberid=$t2p2&_="
        }


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
     * 世界排名 Api
     */
    const val WORLD_RANKING = "https://extranet-lv.bwfbadminton.com/api/vue-rankingtable"
    const val WORLD_RANKING_AUTHORIZATION =
        "Bearer 2|NaXRu9JnMpSdb8l86BkJxj6gzKJofnhmExwr8EWkQtHoattDAGimsSYhpM22a61e1crjTjfIGTKfhzxA"

    const val MATCHES_OF_2023 = "https://www.badmintoncn.com/view-25036-1.html"
}