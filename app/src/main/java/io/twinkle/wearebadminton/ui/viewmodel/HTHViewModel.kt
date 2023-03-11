package io.twinkle.wearebadminton.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import io.twinkle.wearebadminton.data.PlayerOrder
import io.twinkle.wearebadminton.data.bean.HeadToHeadBean
import io.twinkle.wearebadminton.data.bean.PopularPlayersBean
import io.twinkle.wearebadminton.data.payload.HeadToHeadPayload
import io.twinkle.wearebadminton.data.payload.PlayerSearchPayload
import io.twinkle.wearebadminton.ui.uistate.HTHUiState
import io.twinkle.wearebadminton.utilities.BwfApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HTHViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HTHUiState())
    val uiState: StateFlow<HTHUiState> = _uiState.asStateFlow()

    /**
     * 获取查找运动员的列表
     */
    fun updatePlayersList(name: String) {
        Fuel.post(BwfApi.SEARCH_PLAYERS)
            .body(PlayerSearchPayload(searchKey = name).toJson())
            .header(Headers.CONTENT_TYPE, "application/json")
            .header(
                Headers.AUTHORIZATION,
                BwfApi.BWFAPI_AUTHORIZATION
            )
            .responseObject(PopularPlayersBean.Deserializer()) { _, _, result ->
                result.fold({
                    _uiState.update { currentState ->
                        currentState.copy(playersBean = it)
                    }
                }, {})
            }
    }

    /**
     * 此方法用于更新来自其他页面的头对头选手信息
     */
    fun updateTransfer() {
        (0..3).forEach {
            val name = when (it) {
                0 -> uiState.value.player11Name
                1 -> uiState.value.player12Name
                2 -> uiState.value.player21Name
                3 -> uiState.value.player22Name
                else -> ""
            }
            Fuel.post(BwfApi.SEARCH_PLAYERS)
                .body(PlayerSearchPayload(searchKey = name).toJson())
                .header(Headers.CONTENT_TYPE, "application/json")
                .header(
                    Headers.AUTHORIZATION,
                    BwfApi.BWFAPI_AUTHORIZATION
                )
                .responseObject(PopularPlayersBean.Deserializer()) { _, _, result ->
                    result.fold({ bean ->
                        if (bean.results.isNotEmpty() && bean.results[0].name_display == name) {
                            _uiState.update { currentState ->
                                when (it) {
                                    0 -> currentState.copy(player11 = bean.results[0])
                                    1 -> currentState.copy(player12 = bean.results[0])
                                    2 -> currentState.copy(player21 = bean.results[0])
                                    3 -> currentState.copy(player22 = bean.results[0])
                                    else -> currentState
                                }
                            }
                        }
                    }, {})
                }
        }
    }

    fun updateSearchKey(key: String) {
        _uiState.update { currentState ->
            currentState.copy(searchKey = key)
        }
    }

    fun setLoading(loading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = loading)
        }
    }

    /**
     * 获取运动员头对头结果
     */
    fun fetchHTHResult() {
        Fuel.post(BwfApi.HEAD_TO_HEAD)
            .body(
                HeadToHeadPayload(
                    player11 = uiState.value.player11?.id ?: 0,
                    player12 = uiState.value.player12?.id,
                    player21 = uiState.value.player21?.id ?: 0,
                    player22 = uiState.value.player22?.id
                ).toJson()
            )
            .header(Headers.CONTENT_TYPE, "application/json")
            .header(
                Headers.AUTHORIZATION,
                BwfApi.BWFAPI_AUTHORIZATION
            )
            .responseObject(HeadToHeadBean.Deserializer()) { _, _, result ->
                result.fold({
                    _uiState.update { currentState ->
                        currentState.copy(team1 = it.results.team1, team2 = it.results.team2)
                    }
                    setLoading(false)
                }, {})
            }
    }

    fun showPlayersDialog(order: PlayerOrder, show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(currentOrder = order, showPlayersDialog = show)
        }
    }

    fun update(_update: (currentState: HTHUiState) -> HTHUiState) {
        _uiState.update(_update)
    }
}