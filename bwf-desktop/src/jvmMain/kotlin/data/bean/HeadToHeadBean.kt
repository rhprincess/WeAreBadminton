package data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class HeadToHeadBean(
    val drawCount: Int,
    val results: HeadToHeadResults
) {
    class Deserializer : ResponseDeserializable<HeadToHeadBean> {
        override fun deserialize(content: String): HeadToHeadBean? =
            Gson().fromJson(content, HeadToHeadBean::class.java)
    }
}

data class HeadToHeadResults(
    val team1: Int,
    val team2: Int
)