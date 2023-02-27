package io.twinkle.wearebadminton.data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class RankingBean(
    val drawCount: Int,
    val results: Results
) {
    class Deserializer : ResponseDeserializable<RankingBean> {
        override fun deserialize(content: String): RankingBean? =
            Gson().fromJson(content, RankingBean::class.java)
    }
}

data class Results(
    val current_page: Int,
    val `data`: List<Data>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: String,
    val to: Int,
    val total: Int
)

data class Data(
    val close: Any,
    val confederation_id: Int,
    val id: Int,
    val lose: Int,
    val match_party_id: Int,
    val p1_country: String,
    val p2_country: String,
    val player1_id: Int,
    val player1_model: PlayerModel,
    val player2_id: Int,
    val player2_model: PlayerModel?,
    val points: String,
    val prize_money: String,
    val qual: Any,
    val rank: Int,
    val rank_change: Int,
    val rank_previous: Int,
    val ranking_category_id: Int,
    val ranking_publication_id: Int,
    val team_id: Int,
    val team_md: Any,
    val team_ms: Any,
    val team_sc: Any,
    val team_tc: Any,
    val team_total_points: String,
    val team_uc: Any,
    val team_wd: Any,
    val team_ws: Any,
    val team_xd: Any,
    val tournaments: Int,
    val win: Int
)

data class PlayerModel(
    val active: Int,
    val avatar_id: Int,
    val code: String,
    val country: String,
    val country_id: Any,
    val country_model: CountryModel,
    val created_at: String,
    val creator_id: Int,
    val date_of_birth: String,
    val first_name: String,
    val gender_id: Int,
    val id: Int,
    val image_hero_id: Int,
    val image_profile_id: Int,
    val is_deleted: Int,
    val language: Int,
    val last_cache_updated_at: String,
    val last_crawled_at: String,
    val last_name: String,
    val name_display: String,
    val name_display_bold: String,
    val name_initials: String,
    val name_locked: Int,
    val name_short1: String,
    val name_short2: String,
    val name_type_id: Int,
    val nationality: String,
    val old_member_id: Int,
    val ordering: Any,
    val para: Int,
    val preferred_name: Int,
    val profile_type: Int,
    val slug: String,
    val status: Int,
    val updated_at: String
)

data class CountryModel(
    val code_iso3: String,
    val confed_id: Int,
    val created_at: String,
    val created_by: Any,
    val currency_code: String,
    val currency_name: String,
    val currency_symbol: String,
    val custom_code: String,
    val flag_name: String,
    val flag_name_svg: String,
    val flag_url: String,
    val id: Int,
    val is_deleted: Int,
    val language_name: String,
    val ma_id: Int,
    val name: String,
    val nationality: String,
    val status: Int,
    val updated_at: String,
    val updated_by: Int
)