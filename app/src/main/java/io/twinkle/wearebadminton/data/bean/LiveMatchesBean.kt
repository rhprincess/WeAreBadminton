package io.twinkle.wearebadminton.data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class LiveMatchesBean(
    val drawCount: Int,
    val results: List<LiveMatchesResult>
) {
    class Deserializer : ResponseDeserializable<LiveMatchesBean> {
        override fun deserialize(content: String): LiveMatchesBean? =
            Gson().fromJson(content, LiveMatchesBean::class.java)
    }
}

data class LiveMatchesResult(
    val live_count: Int,
    val live_detail: LiveDetail,
    val match_count: Int,
    val match_detail: MatchDetail
)

data class LiveDetail(
    val court_code: String,
    val duration: Int,
    val event: String,
    val id: Int,
    val match_id: Int,
    val match_state: String,
    val match_state_name: String,
    val round: String,
    val service_player: Int,
    val team1_g1_score: Int?,
    val team1_g2_score: Int?,
    val team1_g3_score: Int?,
    val team2_g1_score: Int?,
    val team2_g2_score: Int?,
    val team2_g3_score: Int?
)

data class MatchDetail(
    val code: String,
    val id: Int,
    val t1p1_country: String,
    val t1p1_player_model: LiveMatchesPlayerModel?,
    val t1p1country_model: LiveMatchesCountryModel?,
    val t1p2_country: String,
    val t1p2_player_model: LiveMatchesPlayerModel?,
    val t1p2country_model: LiveMatchesCountryModel?,
    val t2p1_country: String,
    val t2p1_player_model: LiveMatchesPlayerModel?,
    val t2p1country_model: LiveMatchesCountryModel?,
    val t2p2_country: String,
    val t2p2_player_model: LiveMatchesPlayerModel?,
    val t2p2country_model: LiveMatchesCountryModel?,
    val team1_player1_id: String,
    val team1_player2_id: String,
    val team2_player1_id: String,
    val team2_player2_id: String,
    val tournament_id: Int
)

data class LiveMatchesPlayerModel(
    val id: Int,
    val name_display_bold: String,
    val name_short1: String
)

data class LiveMatchesCountryModel(
    val custom_code: String,
    val flag_name_svg: String,
    val id: Int
)