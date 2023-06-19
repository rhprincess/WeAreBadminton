package data.bean

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class CurrentRankBean(val results: Int) {
    class Deserializer : ResponseDeserializable<CurrentRankBean> {
        override fun deserialize(content: String): CurrentRankBean? =
            Gson().fromJson(content, CurrentRankBean::class.java)
    }
}
