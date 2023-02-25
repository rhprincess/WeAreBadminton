package io.twinkle.wearebadminton.ui

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import io.twinkle.wearebadminton.data.bean.RankingBean
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.WorldRankingViewModel
import io.twinkle.wearebadminton.ui.widget.WorldRankingCard
import io.twinkle.wearebadminton.ui.widget.WorldRankingCardPlacement
import io.twinkle.wearebadminton.utilities.BwfApi
import io.twinkle.wearebadminton.utilities.genericType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldRanking(worldRankingViewModel: WorldRankingViewModel = viewModel()) {
    val uiState by worldRankingViewModel.uiState.collectAsState()
    val alpha by animateFloatAsState(
        targetValue = if (uiState.isLoading) 1f else 0f,
        tween(durationMillis = 300),
        finishedListener = {
            worldRankingViewModel.setLoading(false)
            worldRankingViewModel.finishLoadingAnimation(true)
        })
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    // Side-load the ranking table
    if (uiState.isLoading) {
        RefreshRanking(worldRankingViewModel = worldRankingViewModel)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    title = {
                        Text(
                            "世界羽联排名",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {},
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            content = { innerPadding ->
                WorldRankingContent(
                    innerPadding = innerPadding,
                    worldRankingViewModel = worldRankingViewModel
                )
            }
        )

        if (!uiState.loadingAnimationFinished) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
                    .alpha(alpha)
                    .clickable(enabled = uiState.isLoading) {}
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                        .alpha(alpha),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorldRankingContent(
    worldRankingViewModel: WorldRankingViewModel = viewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by worldRankingViewModel.uiState.collectAsState()
    val rankJson = uiState.rankJson
    SideEffect {
        worldRankingViewModel.updateRankingBean(
            Gson().fromJson<RankingBean>(
                rankJson,
                genericType<RankingBean>()
            )
        )
    }

    Column(modifier = Modifier.padding(innerPadding)) {

        val types = listOf("男单", "女单", "男双", "女双", "混双")
        var tabIndex by remember { mutableStateOf(0) }

        // 比赛项目
        TabRow(selectedTabIndex = tabIndex) {
            types.forEachIndexed { index, s ->
                Tab(selected = index == tabIndex, onClick = {
                    tabIndex = index
                    worldRankingViewModel.updateRankApiBean(uiState.apiBean.copy(catId = index + 6))
                    worldRankingViewModel.setLoading(true)
                    worldRankingViewModel.finishLoadingAnimation(false)
                }, text = {
                    Text(text = s)
                })
            }
        }

        // 项目介绍
        WorldRankingCardPlacement()

        // 排名列表
        LazyColumn(
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.bean != null) {
                val list = uiState.bean!!.results.data
                items(list.size) {
                    WorldRankingCard(data = list[it])
                }
            }
        }

    }
}

@Composable
private fun RefreshRanking(worldRankingViewModel: WorldRankingViewModel) {
    val uiState by worldRankingViewModel.uiState.collectAsState()
    SideEffect {
        Fuel.post(BwfApi.WORLD_RANKING)
            .body(Gson().toJson(uiState.apiBean))
            .header(Headers.CONTENT_TYPE, "application/json")
            .header(
                Headers.AUTHORIZATION,
                BwfApi.WORLD_RANKING_AUTHORIZATION
            )
            .responseString { _, _, result ->
                result.fold({
                    worldRankingViewModel.updateRankJson(it)
                    worldRankingViewModel.setLoading(false)
                }, {
                    Log.e("WorldRanking", "failed to fetch the ranking table")
                })
            }
    }
}

@Preview(showBackground = true)
@Composable
fun WorldRankingContentPreview() {
    BwfBadmintonTheme {
        WorldRanking()
    }
}