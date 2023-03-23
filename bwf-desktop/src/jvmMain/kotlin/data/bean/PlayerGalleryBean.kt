package io.twinkle.wearebadminton.data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class PlayerGalleryBean(
    val drawCount: Int,
    val results: List<PlayerGalleryResult>
) {
    class Deserializer : ResponseDeserializable<PlayerGalleryBean> {
        override fun deserialize(content: String): PlayerGalleryBean? =
            Gson().fromJson(content, PlayerGalleryBean::class.java)
    }
}

data class PlayerGalleryResult(
    val caption: String,
    val src: String,
    val thumb: String,
    val title: String
)