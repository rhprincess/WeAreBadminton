package io.twinkle.wearebadminton.data.payload

import com.google.gson.Gson

interface Payload {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}