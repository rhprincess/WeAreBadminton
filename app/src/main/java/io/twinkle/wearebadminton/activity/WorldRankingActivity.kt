package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.ui.WorldRanking
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.WorldRankingViewModel

class WorldRankingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BwfBadmintonTheme {
                val worldRankingViewModel: WorldRankingViewModel = viewModel()
                WorldRanking(worldRankingViewModel = worldRankingViewModel)
            }
        }
    }

}