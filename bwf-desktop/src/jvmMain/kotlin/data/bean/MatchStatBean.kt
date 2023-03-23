package io.twinkle.wearebadminton.data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class MatchStatBean(
    val drawCount: Int,
    val results: MatchStatResults
) {
    class Deserializer : ResponseDeserializable<MatchStatBean> {
        override fun deserialize(content: String): MatchStatBean? =
            Gson().fromJson(content, MatchStatBean::class.java)
    }
}

data class MatchStatResults(
    val id: Int,
    val match_id: Int,
    val team1_challenge_lost: Int,
    val team1_challenge_nodecision: Int,
    val team1_challenge_used: Int,
    val team1_challenge_won: Int,
    val team1_consecutive_points: Int,
    val team1_game_points: Int,
    val team1_rallies_played: Int,
    val team1_rallies_won: Int,
    val team2_challenge_lost: Int,
    val team2_challenge_nodecision: Int,
    val team2_challenge_used: Int,
    val team2_challenge_won: Int,
    val team2_consecutive_points: Int,
    val team2_game_points: Int,
    val team2_rallies_played: Int,
    val team2_rallies_won: Int,
    val tournament_id: Int
)