package data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class PopularPlayersBean(
    val drawCount: Int,
    val pagination: Pagination,
    val results: List<PlayerResult>
) {
    class Deserializer : ResponseDeserializable<PopularPlayersBean> {
        override fun deserialize(content: String): PopularPlayersBean? =
            Gson().fromJson(content, PopularPlayersBean::class.java)
    }
}

data class Pagination(
    val current_page: Int,
    val `data`: List<PlayerResult>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class PlayerResult(
    val active: Int,
    val avatar: Avatar,
    val avatar_id: Int,
    val code: String,
    val country: String,
    val country_id: Any,
    val country_model: CountryModel,
    val created_at: String,
    val creator_id: Any,
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
    val name_display_break: String,
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

data class Avatar(
    val file_name: String,
    val name: String,
    val url_cloudinary: String,
    val url_large_image: String,
    val url_medium_image: String,
    val url_original: String,
    val url_thumbnail: String
)