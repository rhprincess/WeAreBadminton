@file:OptIn(ExperimentalMaterial3Api::class)

package io.twinkle.wearebadminton.ui

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import io.twinkle.wearebadminton.activity.PlayerProfileActivity
import io.twinkle.wearebadminton.data.bean.RankingBean
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.WorldRankingViewModel
import io.twinkle.wearebadminton.ui.widget.TextTitle
import io.twinkle.wearebadminton.ui.widget.WorldRankingCard
import io.twinkle.wearebadminton.ui.widget.WorldRankingCardPlacement
import io.twinkle.wearebadminton.utilities.BwfApi

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
                    .clickable(
                        onClick = {},
                        enabled = uiState.isLoading,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() })
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
    val context = LocalContext.current
    SideEffect {
        worldRankingViewModel.updateRankingBean(
            Gson().fromJson(rankJson, RankingBean::class.java)
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
                    WorldRankingCard(
                        data = list[it],
                        index = it,
                        catId = tabIndex + 6,
                        viewModel = worldRankingViewModel
                    )
                }
            }
        }

        if (uiState.showPlayerChoices) {
            AlertDialog(
                onDismissRequest = { worldRankingViewModel.showPlayerChoices(false) },
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    val list = uiState.bean!!.results.data
                    TextTitle(title = "请选择一位球员", style = MaterialTheme.typography.bodyLarge)

                    SuggestionChip(
                        onClick = {
                            val intent = Intent()
                            intent.putExtra("playerId", list[uiState.rankIndex].player1_id)
                            intent.putExtra("catId", tabIndex + 6)
                            intent.setClass(
                                context,
                                PlayerProfileActivity::class.java
                            )
                            context.startActivity(intent)
                            worldRankingViewModel.showPlayerChoices(false)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(start = 16.dp),
                        label = {
                            Text(text = list[uiState.rankIndex].player1_model.name_display)
                        },
                        icon = {
                            AsyncImage(
                                model = BwfApi.FLAG_URL + list[uiState.rankIndex].player1_model.country_model.flag_name_svg,
                                contentDescription = "player1 nation flag",
                                modifier = Modifier.size(25.dp),
                                imageLoader = ImageLoader.Builder(
                                    LocalContext.current
                                ).components {
                                    add(SvgDecoder.Factory())
                                    if (Build.VERSION.SDK_INT >= 28) {
                                        add(ImageDecoderDecoder.Factory())
                                    } else {
                                        add(GifDecoder.Factory())
                                    }
                                }.build()
                            )
                        }
                    )

                    SuggestionChip(
                        onClick = {
                            val intent = Intent()
                            intent.putExtra("playerId", list[uiState.rankIndex].player2_id)
                            intent.putExtra("catId", tabIndex + 6)
                            intent.setClass(
                                context,
                                PlayerProfileActivity::class.java
                            )
                            context.startActivity(intent)
                            worldRankingViewModel.showPlayerChoices(false)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                        label = {
                            Text(text = list[uiState.rankIndex].player2_model!!.name_display)
                        },
                        icon = {
                            AsyncImage(
                                model = BwfApi.FLAG_URL + list[uiState.rankIndex].player2_model!!.country_model.flag_name_svg,
                                contentDescription = "player2 nation flag",
                                modifier = Modifier.size(25.dp),
                                imageLoader = ImageLoader.Builder(
                                    LocalContext.current
                                ).components {
                                    add(SvgDecoder.Factory())
                                    if (Build.VERSION.SDK_INT >= 28) {
                                        add(ImageDecoderDecoder.Factory())
                                    } else {
                                        add(GifDecoder.Factory())
                                    }
                                }.build()
                            )
                        }
                    )
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