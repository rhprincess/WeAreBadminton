package data.payload

import com.google.gson.Gson

interface Payload {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}