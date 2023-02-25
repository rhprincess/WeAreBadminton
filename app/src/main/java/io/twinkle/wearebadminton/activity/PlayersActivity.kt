package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.ui.PlayersActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayersViewModel

class PlayersActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BwfBadmintonTheme {
                val playersViewModel = viewModel<PlayersViewModel>()
                PlayersActivityUI(playersViewModel = playersViewModel)
            }
        }
    }

}