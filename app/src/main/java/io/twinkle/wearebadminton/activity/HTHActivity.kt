package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import io.twinkle.wearebadminton.ui.HTHActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.HTHViewModel
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HTHActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = mutableStateOf(0)
        val dynamicColor = mutableStateOf(true)
        val model by viewModels<HTHViewModel>()

        val p11 = intent.getStringExtra("p11")
        val p12 = intent.getStringExtra("p12")
        val p21 = intent.getStringExtra("p21")
        val p22 = intent.getStringExtra("p22")
        val tByName = intent.getBooleanExtra("transferByName", false)

        if (tByName) {
            model.update { currentState ->
                currentState.copy(
                    player11Name = p11 ?: "",
                    player12Name = p12 ?: "",
                    player21Name = p21 ?: "",
                    player22Name = p22 ?: ""
                )
            }
            lifecycleScope.launch {
                model.setLoading(true)
                model.updateTransfer()
                delay(3000)
                model.fetchHTHResult()
            }
        }

        lifecycleScope.launch {
            settings.data.collect {
                theme.value = it[Constants.KEY_THEME] ?: 0
                dynamicColor.value = it[Constants.KEY_DYNAMIC_COLOR] ?: true
            }
        }
        setContent {
            BwfBadmintonTheme(theme = theme.value, dynamicColor = dynamicColor.value) {
                HTHActivityUI(viewModel = model)
            }
        }
    }

}