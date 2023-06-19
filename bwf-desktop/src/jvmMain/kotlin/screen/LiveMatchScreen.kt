package screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import data.bean.LiveMatchesBean
import data.bean.MatchStatBean
import data.payload.LiveMatchesPayload
import data.payload.MatchStatPayload
import navcontroller.NavController
import ui.viewmodel.LiveMatchViewModel
import ui.widget.LiveCourtCard
import utilities.BwfApi
import utilities.Constants
import utilities.DataStoreUtils
import utilities.LocalScreenSize
import java.util.*
import kotlin.concurrent.schedule

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiveMatchScreen(navController: NavController, viewModel: LiveMatchViewModel = LiveMatchViewModel.viewModel()) {
    val timer = Timer()
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val refreshingFrequency = DataStoreUtils.readIntData(Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY, 10)
    val tmtId = navController.screenBundle.strings["tmtId"]
    val tmtType = navController.screenBundle.ints["tmtType"] ?: 0
    var refreshTime = 0
    val windowSize = LocalScreenSize.current

    SideEffect {
        fetchLiveMatches(tmtId ?: "", tmtType, viewModel)
    }

    timer.schedule(1000, 1000) {
        if (refreshTime == refreshingFrequency) {
            refreshTime = 0
            fetchLiveMatches(tmtId ?: "", tmtType, viewModel)
            if (uiState.expandIndex != null) {
                fetchMatchStat(
                    matchId = uiState.currentMatchId ?: 0,
                    tmtId = uiState.currentTmtId ?: 0,
                    viewModel = viewModel
                )
            }
        } else {
            refreshTime++
        }
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "场地比分直播",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = Dp.Unspecified
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (windowSize.width >= 375.dp * 2) {
                    LazyVerticalGrid(
                        GridCells.Fixed((windowSize.width / 375.dp).toInt()),
                        verticalArrangement = Arrangement.Center,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        item {
                            if (uiState.topIndex != null && uiState.isSafeToStickOnTop) {
                                var hasUpdate = false
                                if (uiState.lastMatches.size == uiState.matches.size) {
                                    hasUpdate =
                                        uiState.matches[uiState.topIndex!!] != uiState.lastMatches[uiState.topIndex!!]
                                }
                                Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                                    Box(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    listOf(
                                                        MaterialTheme.colors.background,
                                                        MaterialTheme.colors.background.copy(0.75f),
                                                        MaterialTheme.colors.background.copy(0.5f),
                                                        MaterialTheme.colors.background.copy(0.25f),
                                                        Color.Transparent,
                                                    )
                                                )
                                            )
                                            .shadow(
                                                elevation = 8.dp,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        LiveCourtCard(
                                            index = uiState.topIndex!!,
                                            matchDetail = uiState.matches[uiState.topIndex!!].match_detail,
                                            liveDetail = uiState.matches[uiState.topIndex!!].live_detail,
                                            matchStatResults = uiState.matchStatResults,
                                            hasUpdate = hasUpdate,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.matches.isEmpty() && !uiState.isRefreshing) {
                            item {
                                Text(
                                    text = "无正在进行的比赛",
                                    style = MaterialTheme.typography.h5,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        items(uiState.matches.size) {
                            if (it != uiState.topIndex) {
                                var hasUpdate = false
                                if (uiState.lastMatches.size == uiState.matches.size) {
                                    hasUpdate = uiState.matches[it] != uiState.lastMatches[it]
                                }
                                Box(modifier = Modifier.padding(16.dp)) {
                                    LiveCourtCard(
                                        index = it,
                                        matchDetail = uiState.matches[it].match_detail,
                                        liveDetail = uiState.matches[it].live_detail,
                                        matchStatResults = uiState.matchStatResults,
                                        hasUpdate = hasUpdate,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.Center, state = listState) {

                        // 标题头
                        stickyHeader {
                            if (uiState.topIndex != null && uiState.isSafeToStickOnTop) {
                                var hasUpdate = false
                                if (uiState.lastMatches.size == uiState.matches.size) {
                                    hasUpdate =
                                        uiState.matches[uiState.topIndex!!] != uiState.lastMatches[uiState.topIndex!!]
                                }
                                Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                                    Box(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    listOf(
                                                        MaterialTheme.colors.background,
                                                        MaterialTheme.colors.background.copy(0.75f),
                                                        MaterialTheme.colors.background.copy(0.5f),
                                                        MaterialTheme.colors.background.copy(0.25f),
                                                        Color.Transparent,
                                                    )
                                                )
                                            )
                                            .shadow(
                                                elevation = 8.dp,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        LiveCourtCard(
                                            index = uiState.topIndex!!,
                                            matchDetail = uiState.matches[uiState.topIndex!!].match_detail,
                                            liveDetail = uiState.matches[uiState.topIndex!!].live_detail,
                                            matchStatResults = uiState.matchStatResults,
                                            hasUpdate = hasUpdate,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }
                        }

                        if (uiState.matches.isEmpty() && !uiState.isRefreshing) {
                            item {
                                Text(
                                    text = "无正在进行的比赛",
                                    style = MaterialTheme.typography.h5,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        items(uiState.matches.size) {
                            if (it != uiState.topIndex) {
                                var hasUpdate = false
                                if (uiState.lastMatches.size == uiState.matches.size) {
                                    hasUpdate = uiState.matches[it] != uiState.lastMatches[it]
                                }
                                Box(modifier = Modifier.padding(16.dp)) {
                                    LiveCourtCard(
                                        index = it,
                                        matchDetail = uiState.matches[it].match_detail,
                                        liveDetail = uiState.matches[it].live_detail,
                                        matchStatResults = uiState.matchStatResults,
                                        hasUpdate = hasUpdate,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    )
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