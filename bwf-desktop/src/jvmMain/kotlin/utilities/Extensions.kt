package utilities

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.SerializableMatchData

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!

fun getMatchesHistory(json: String): ArrayList<SerializableMatchData> {
    return if (json.isEmpty()) {
        arrayListOf()
    } else {
        Gson().fromJson(json, genericType<ArrayList<SerializableMatchData>>())
    }
}