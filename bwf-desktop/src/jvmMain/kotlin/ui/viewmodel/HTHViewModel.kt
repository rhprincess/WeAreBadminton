package ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import data.PlayerOrder
import data.bean.PopularPlayersBean
import data.payload.PlayerSearchPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.uistate.HTHUiState
import utilities.BwfApi

class HTHViewModel {
    private val _uiState = MutableStateFlow(HTHUiState())
    val uiState: StateFlow<HTHUiState> = _uiState.asStateFlow()

    companion object {
        @Composable
        fun viewModel(): HTHViewModel = remember { HTHViewModel() }
    }

    /**
     * 获取查找运动员的列表
     */
    fun updatePlayersList(name: String) {
        Fuel.post(BwfApi.SEARCH_PLAYERS)
            .body(PlayerSearchPayload(searchKey = name).toJson())
            .header(Headers.CONTENT_TYPE, "application/json, text/plain, */*n")
            .header(Headers.AUTHORIZATION, BwfApi.BWFAPI_AUTHORIZATION)
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
                .header(Headers.CONTENT_TYPE, "application/json, text/plain, */*")
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
        Fuel.get(
            BwfApi.HEAD_TO_HEAD(
                t1p1 = uiState.value.player11?.id.toString().replace("null", ""),
                t1p2 = uiState.value.player12?.id.toString().replace("null", ""),
                t2p1 = uiState.value.player21?.id.toString().replace("null", ""),
                t2p2 = uiState.value.player22?.id.toString().replace("null", "")
            )
        ).responseString { _, _, result ->
            result.fold({ source ->
                val regex =
                    "(?<=<td align=\"center\" style=\"font-size:56px; color:#fff; font-weight:bold;text-shadow: 0 2px 5px rgba\\(0, 0, 0, 0.3\\);\" width=\"10%\">).*?(?=</td>)".toRegex()
                val matchedResult = regex.findAll(source).toList()
                if (matchedResult.size == 2) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            team1 = matchedResult[0].value.toInt(),
                            team2 = matchedResult[1].value.toInt()
                        )
                    }
                    setLoading(false)
                }
            }, { it.printStackTrace() })
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