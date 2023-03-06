package io.twinkle.wearebadminton.utilities

import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    const val SETTINGS = "settings"
    val KEY_THEME = intPreferencesKey("theme")
    val KEY_MATCH_PREVIOUS_SIZE = intPreferencesKey("match_previous_size")
}