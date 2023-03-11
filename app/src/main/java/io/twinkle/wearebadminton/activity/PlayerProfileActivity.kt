package io.twinkle.wearebadminton.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import io.twinkle.wearebadminton.data.bean.CurrentRankBean
import io.twinkle.wearebadminton.data.bean.PlayerProfileBean
import io.twinkle.wearebadminton.data.payload.CurrentRankPayload
import io.twinkle.wearebadminton.data.payload.PlayerSummaryPayload
import io.twinkle.wearebadminton.data.payload.RankingEventsPayload
import io.twinkle.wearebadminton.ui.PlayerProfileActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayerProfileViewModel
import io.twinkle.wearebadminton.utilities.BwfApi
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.launch

class PlayerProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerId = intent.getIntExtra("playerId", 57945)
        val catId = intent.getIntExtra("catId", 6)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val theme = mutableStateOf(0)
        val dynamicColor = mutableStateOf(true)
        lifecycleScope.launch {
            settings.data.collect {
                theme.value = it[Constants.KEY_THEME] ?: 0
                dynamicColor.value = it[Constants.KEY_DYNAMIC_COLOR] ?: true
            }
        }
        setContent {
            BwfBadmintonTheme(theme = theme.value, statusBarColor = Color.Transparent, dynamicColor = dynamicColor.value) {
                val playerProfileViewModel = viewModel<PlayerProfileViewModel>()
                val playerSummaryApiBean = PlayerSummaryPayload(playerId = playerId.toString())
                SideEffect {
                    // Summary Data
                    Fuel.post(BwfApi.PLAYER_SUMMARY)
                        .body(playerSummaryApiBean.toJson())
                        .header(Headers.CONTENT_TYPE, "application/json")
                        .header(
                            Headers.AUTHORIZATION,
                            BwfApi.BWFAPI_AUTHORIZATION
                        )
                        .responseObject(PlayerProfileBean.Deserializer()) { _, _, result ->
                            result.fold({
                                playerProfileViewModel.update { currentState ->
                                    currentState.copy(
                                        id = playerId.toString(),
                                        catId = catId,
                                        name = it.results.name_display,
                                        country = it.results.country_model.name,
                                        bannerImgUrl = it.results.hero_image.url_cloudinary,
                                        flagUrl = BwfApi.FLAG_URL + it.results.country_model.flag_name_svg,
                                        avatarUrl = it.results.avatar.url_cloudinary,
                                        lastName = it.results.last_name,
                                        bioModel = it.results.bio_model
                                    )
                                }
                            }, { it.printStackTrace() })

                        }

                    Fuel.post(BwfApi.RANKING_EVENTS)
                        .body(RankingEventsPayload(playerId = playerId.toString()).toJson())
                        .header(Headers.CONTENT_TYPE, "application/json")
                        .header(
                            Headers.AUTHORIZATION,
                            BwfApi.BWFAPI_AUTHORIZATION
                        ).responseString { _, _, _result ->
                            _result.fold({ s ->
                                val regex = "\"id\":\"(([\\s\\S])*?)\"".toRegex()
                                val rankingEvent =
                                    regex.find(s)?.value?.replace("\"", "")?.replace("id:", "")
                                // 获取当前排名
                                Fuel.post(BwfApi.CURRENT_RANKING)
                                    .body(
                                        CurrentRankPayload(
                                            playerId = playerId.toString(),
                                            rankingEvent = "$rankingEvent"
                                        ).toJson()
                                    )
                                    .header(Headers.CONTENT_TYPE, "application/json")
                                    .header(
                                        Headers.AUTHORIZATION,
                                        BwfApi.BWFAPI_AUTHORIZATION
                                    )
                                    .responseObject(CurrentRankBean.Deserializer()) { _, _, result ->
                                        result.fold({
                                            playerProfileViewModel.update { currentState ->
                                                currentState.copy(
                                                    worldRank = it.results.toString()
                                                )
                                            }
                                        }, { it.printStackTrace() })
                                    }

                            }, { it.printStackTrace() })
                        }

                }
                PlayerProfileActivityUI(playerProfileViewModel = playerProfileViewModel)
            }
        }
    }

}