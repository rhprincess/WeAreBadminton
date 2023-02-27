package io.twinkle.wearebadminton.data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class PlayerProfileBean(
    val drawCount: Int,
    val results: ProfileResult
) {
    class Deserializer : ResponseDeserializable<PlayerProfileBean> {
        override fun deserialize(content: String): PlayerProfileBean? =
            Gson().fromJson(content, PlayerProfileBean::class.java)
    }
}

data class ProfileResult(
    val active: Int,
    val avatar: Avatar,
    val avatar_id: Int,
    val bio_model: BioModel,
    val code: String,
    val country: String,
    val country_id: Any,
    val country_model: CountryModel,
    val created_at: String,
    val creator_id: Any,
    val date_of_birth: String,
    val first_name: String,
    val gender_id: Int,
    val hero_image: HeroImage,
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

data class BioModel(
    val begin_sport: String,
    val bwf_bio: Any,
    val club: Any,
    val coach: Any,
    val current_residence: String,
    val dob: Any,
    val education_level: Any,
    val equipment_sponsor: String,
    val facebook: String,
    val family_information: Any,
    val famous_sporting_relatives: Any,
    val height: String,
    val hobbies: Any,
    val id: Int,
    val impairment_cause: Any,
    val impairment_type: Any,
    val instagram: String,
    val international_debut: String,
    val languages: String,
    val major_injuries: Any,
    val member_national_team_since: String,
    val memorable_achievements: String,
    val most_influential_person: String,
    val nickname: Any,
    val occupation: Any,
    val other_sports: Any,
    val player_id: Int,
    val plays: String,
    val pob: String,
    val previous_olympics: String,
    val sporting_ambitions: String,
    val sporting_awards: String,
    val sporting_hero: Any,
    val sporting_philosophy: Any,
    val start_playing_competitively: String,
    val style_of_play: Any,
    val superstitions_rituals: String,
    val training_regime: Any,
    val twitter: String,
    val website: String
)


data class HeroImage(
    val file_name: String,
    val name: String,
    val url: String,
    val url_cloudinary: String,
    val url_cloudinary_large: String,
    val url_cloudinary_mobile: String
)