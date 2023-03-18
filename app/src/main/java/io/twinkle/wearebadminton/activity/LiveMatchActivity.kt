package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import io.twinkle.wearebadminton.data.bean.LiveMatchesBean
import io.twinkle.wearebadminton.data.bean.MatchStatBean
import io.twinkle.wearebadminton.data.payload.LiveMatchesPayload
import io.twinkle.wearebadminton.data.payload.MatchStatPayload
import io.twinkle.wearebadminton.ui.LiveMatchActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveMatchViewModel
import io.twinkle.wearebadminton.utilities.BwfApi
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class LiveMatchActivity : ComponentActivity() {

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val theme = mutableStateOf(0)
        val dynamicColor = mutableStateOf(true)
        val refreshingFrequency = mutableStateOf(10)
        val tmtId = intent.getStringExtra("tmtId")
        val tmtType = intent.getIntExtra("tmtType", 0)
        val model by viewModels<LiveMatchViewModel>()
        var refreshTime = 0
        lifecycleScope.launch {
            settings.data.collect {
                theme.value = it[Constants.KEY_THEME] ?: 0
                dynamicColor.value = it[Constants.KEY_DYNAMIC_COLOR] ?: true
                refreshingFrequency.value = it[Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY] ?: 10
            }
        }
        setContent {
            BwfBadmintonTheme(theme = theme.value, dynamicColor = dynamicColor.value) {
                val uiState by model.uiState.collectAsState()

                SideEffect {
                    fetchLiveMatches(tmtId ?: "", tmtType, model)
                }

                timer.schedule(1000, 1000) {
                    if (refreshTime == refreshingFrequency.value) {
                        refreshTime = 0
                        fetchLiveMatches(tmtId ?: "", tmtType, model)
                        if (uiState.expandIndex != null) {
                            fetchMatchStat(
                                matchId = uiState.currentMatchId ?: 0,
                                tmtId = uiState.currentTmtId ?: 0,
                                viewModel = model
                            )
                        }
                    } else {
                        refreshTime++
                    }
                }

                LiveMatchActivityUI(viewModel = model)
            }
        }
    }

    private fun fetchLiveMatches(
        tmtId: String,
        tmtType: Int,
        viewModel: LiveMatchViewModel
    ) {
        viewModel.callRefreshingState()
        Fuel.post(BwfApi.LIVE_MATCHES)
            .body(LiveMatchesPayload(tmtId = tmtId, tmtType = tmtType).toJson())
            .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
            .header(Headers.AUTHORIZATION, BwfApi.BWFAPI_AUTHORIZATION)
            .responseObject(LiveMatchesBean.Deserializer()) { _, _, result ->
                result.fold({
                    viewModel.updateLiveMatches(it.results)
                }, {})
            }
    }

    private fun fetchMatchStat(
        matchId: Int,
        tmtId: Int,
        viewModel: LiveMatchViewModel
    ) {
        viewModel.callRefreshingState()
        Fuel.post(BwfApi.LIVE_MATCH_STAT)
            .body(MatchStatPayload(matchId = matchId.toString(), tmtId = tmtId.toString()).toJson())
            .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
            .header(Headers.AUTHORIZATION, BwfApi.BWFAPI_AUTHORIZATION)
            .responseObject(MatchStatBean.Deserializer()) { _, _, result ->
                result.fold({
                    viewModel.updateMatchStatResults(it.results)
                }, {})
            }
    }
}