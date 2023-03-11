package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import io.twinkle.wearebadminton.ui.LiveScoreActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveScoreViewModel
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.onBackPressed
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class LiveScoreActivity : ComponentActivity() {

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model by viewModels<LiveScoreViewModel>()
        var sec = 0
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
                val uiState by model.uiState.collectAsState()
                timer.schedule(1000, 1000) {
                    if (uiState.isGamePlaying) {
                        val min = sec / 60
                        if (min != uiState.duration) {
                            model.updateDuration(min)
                        }
                        sec++
                    }
                }
                LiveScoreActivityUI(this, model)
            }
        }

        onBackPressed(true) {
            timer.cancel()
            finish()
        }
    }

}