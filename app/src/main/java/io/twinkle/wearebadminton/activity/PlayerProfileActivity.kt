package io.twinkle.wearebadminton.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import io.twinkle.wearebadminton.data.bean.CurrentRankBean
import io.twinkle.wearebadminton.data.bean.PlayerProfileBean
import io.twinkle.wearebadminton.data.payload.CurrentRankPayload
import io.twinkle.wearebadminton.data.payload.PlayerSummaryPayload
import io.twinkle.wearebadminton.data.payload.RankingEventsPayload
import io.twinkle.wearebadminton.ui.PlayerProfileActivityUI
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayerProfileViewModel
import io.twinkle.wearebadminton.utilities.BwfApi
import io.twinkle.wearebadminton.utilities.genericType

class PlayerProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playerId = intent.getIntExtra("playerId", 57945)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BwfBadmintonTheme(statusBarColor = Color.Transparent) {
                val playerProfileViewModel = viewModel<PlayerProfileViewModel>()
                val playerSummaryApiBean = PlayerSummaryPayload(playerId = playerId.toString())
                SideEffect {
                    // Summary Data
                    Fuel.post(BwfApi.PLAYER_SUMMARY)
                        .body(Gson().toJson(playerSummaryApiBean))
                        .header(Headers.CONTENT_TYPE, "application/json")
                        .header(
                            Headers.AUTHORIZATION,
                            BwfApi.WORLD_RANKING_AUTHORIZATION
                        )
                        .responseString { _, _, result ->
                            result.fold({
                                if (it.isNotEmpty()) {
                                    val bean = Gson().fromJson<PlayerProfileBean>(
                                        it,
                                        genericType<PlayerProfileBean>()
                                    )
                                    playerProfileViewModel.update { currentState ->
                                        currentState.copy(
                                            id = playerId.toString(),
                                            name = bean.results.name_display,
                                            country = bean.results.country,
                                            bannerImgUrl = bean.results.hero_image.url_cloudinary,
                                            flagUrl = BwfApi.FLAG_URL + bean.results.country_model.flag_name_svg,
                                            avatarUrl = bean.results.avatar.url_cloudinary,
                                            lastName = bean.results.last_name
                                        )
                                    }
                                }
                            }, {
                                Log.e(this::class.java.name, "failed to fetch the player summary")
                            })
                        }

                    Fuel.post(BwfApi.RANKING_EVENTS)
                        .body(Gson().toJson(RankingEventsPayload(playerId = playerId.toString())))
                        .header(Headers.CONTENT_TYPE, "application/json")
                        .header(
                            Headers.AUTHORIZATION,
                            BwfApi.WORLD_RANKING_AUTHORIZATION
                        ).responseString { _, _, _result ->
                            _result.fold({ s ->

                                val regex = "\"id\":\"(([\\s\\S])*?)\"".toRegex()
                                val rankingEvent =
                                    regex.find(s)?.value?.replace("\"", "")?.replace("id:", "")

                                Fuel.post(BwfApi.CURRENT_RANKING)
                                    .body(
                                        Gson().toJson(
                                            CurrentRankPayload(
                                                playerId = playerId.toString(),
                                                rankingEvent = "$rankingEvent"
                                            )
                                        )
                                    )
                                    .header(Headers.CONTENT_TYPE, "application/json")
                                    .header(
                                        Headers.AUTHORIZATION,
                                        BwfApi.WORLD_RANKING_AUTHORIZATION
                                    ).responseString { _, _, result ->
                                        result.fold({
                                            val bean = Gson().fromJson<CurrentRankBean>(
                                                it,
                                                genericType<CurrentRankBean>()
                                            )
                                            playerProfileViewModel.update { currentState ->
                                                currentState.copy(
                                                    worldRank = bean.results.toString()
                                                )
                                            }
                                        }, {
                                            Log.e(
                                                this::class.java.name,
                                                "failed to fetch current ranking"
                                            )
                                        })
                                    }

                            }, {
                                Log.e(this::class.java.name, "failed to fetch ranking events")
                            })
                        }

                }
                PlayerProfileActivityUI(playerProfileViewModel = playerProfileViewModel)
            }
        }
    }

}