package io.twinkle.wearebadminton.data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class LiveSingleBean(
    val drawCount: Int,
    val results: LiveSingleResults
) {
    class Deserializer : ResponseDeserializable<LiveSingleBean> {
        override fun deserialize(content: String): LiveSingleBean? =
            Gson().fromJson(content, LiveSingleBean::class.java)
    }
}

data class LiveSingleResults(
    val duration: Int,
    val event: String,
    val eventText: String,
    val id: Int,
    val matchStateText: String,
    val match_state: String,
    val receiver_player: Int,
    val service_player: Int,
    val t1g1Percent: Int,
    val t1g2Percent: Int,
    val t1g3Percent: Int,
    val t1p1Detail: T1p1Detail,
    val t1p2Detail: T1p2Detail,
    val t2g1Percent: Int,
    val t2g2Percent: Int,
    val t2g3Percent: Int,
    val t2p1Detail: T2p1Detail,
    val t2p2Detail: T2p2Detail,
    val team1_g1_score: Int,
    val team1_g2_score: Any,
    val team1_g3_score: Any,
    val team1_player1_id: Int,
    val team1_player2_id: Int,
    val team2_g1_score: Int,
    val team2_g2_score: Any,
    val team2_g3_score: Any,
    val team2_player1_id: Int,
    val team2_player2_id: Int,
    val winner: Int
)

data class T1p1Detail(
    val avatar: LiveSingleAvatar,
    val first_name: String,
    val fullName: String,
    val id: Int,
    val last_name: String,
    val nameShort: String,
    val name_display: String,
    val name_display_bold: String,
    val name_display_break: String,
    val name_short1: String,
    val name_type_id: Int,
    val nationality: String,
    val nationality_item: NationalityItem,
    val playerLink: String,
    val slug: String
)

data class T1p2Detail(
    val avatar: LiveSingleAvatar,
    val first_name: String,
    val fullName: String,
    val id: Int,
    val last_name: String,
    val nameShort: String,
    val name_display: String,
    val name_display_bold: String,
    val name_display_break: String,
    val name_short1: String,
    val name_type_id: Int,
    val nationality: String,
    val nationality_item: NationalityItem,
    val playerLink: String,
    val slug: String
)

data class T2p1Detail(
    val avatar: LiveSingleAvatar,
    val first_name: String,
    val fullName: String,
    val id: Int,
    val last_name: String,
    val nameShort: String,
    val name_display: String,
    val name_display_bold: String,
    val name_display_break: String,
    val name_short1: String,
    val name_type_id: Int,
    val nationality: String,
    val nationality_item: NationalityItem,
    val playerLink: String,
    val slug: String
)

data class T2p2Detail(
    val avatar: LiveSingleAvatar,
    val first_name: String,
    val fullName: String,
    val id: Int,
    val last_name: String,
    val nameShort: String,
    val name_display: String,
    val name_display_bold: String,
    val name_display_break: String,
    val name_short1: String,
    val name_type_id: Int,
    val nationality: String,
    val nationality_item: NationalityItem,
    val playerLink: String,
    val slug: String
)

data class LiveSingleAvatar(
    val url_cloudinary: String
)

data class NationalityItem(
    val code_iso3: String,
    val custom_code: String,
    val flag_name_svg: String,
    val id: Int
)