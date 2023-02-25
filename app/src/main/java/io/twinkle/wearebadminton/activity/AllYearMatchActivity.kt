package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.twinkle.wearebadminton.ui.AllYearMatch
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

class AllYearMatchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BwfBadmintonTheme {
                AllYearMatch(this)
            }
        }
    }
}