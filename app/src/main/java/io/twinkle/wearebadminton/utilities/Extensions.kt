package io.twinkle.wearebadminton.utilities

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.twinkle.wearebadminton.data.SerializableMatchData

fun ComponentActivity.onBackPressed(isEnabled: Boolean, callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(this,
        object : OnBackPressedCallback(isEnabled) {
            override fun handleOnBackPressed() {
                callback()
            }
        })
}

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!

fun getMatchesHistory(json: String): ArrayList<SerializableMatchData> {
    return if (json.isEmpty()) {
        arrayListOf()
    } else {
        Gson().fromJson(json, genericType<ArrayList<SerializableMatchData>>())
    }
}

val Context.settings: DataStore<Preferences> by preferencesDataStore(name = Constants.SETTINGS)