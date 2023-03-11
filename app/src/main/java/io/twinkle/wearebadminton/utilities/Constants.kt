package io.twinkle.wearebadminton.utilities

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    const val SETTINGS = "settings"
    val KEY_THEME = intPreferencesKey("theme")
    val KEY_MATCH_PREVIOUS_SIZE = intPreferencesKey("match_previous_size")
    val WORLD_RANKING_COUNT_PER_PAGE = intPreferencesKey("world_ranking_count_per_page")
    val KEY_SHOW_BREAKING_DOWN = booleanPreferencesKey("show_breaking_down")
    val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    val KEY_LIVE_MATCH_REFRESHING_FREQUENCY = intPreferencesKey("live_match_refreshing_frequency")
}