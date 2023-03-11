package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import io.twinkle.wearebadminton.ui.CurrentLiveActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.launch

class CurrentLiveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = mutableStateOf(0)
        val dynamicColor = mutableStateOf(true)
        lifecycleScope.launch {
            settings.data.collect {
                theme.value = it[Constants.KEY_THEME] ?: 0
                dynamicColor.value = it[Constants.KEY_DYNAMIC_COLOR] ?: true
            }
        }
        setContent {
            BwfBadmintonTheme(theme = theme.value, dynamicColor = dynamicColor.value) {
                CurrentLiveActivityUI()
            }
        }
    }
}