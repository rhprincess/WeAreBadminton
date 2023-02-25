package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.twinkle.wearebadminton.ui.HistoryActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

class HistoryActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BwfBadmintonTheme {
                HistoryActivityUI()
            }
        }
    }

}