package screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import io.twinkle.wearebadminton.data.bean.RankingBean
import utilities.BwfApi
import navcontroller.NavController
import navcontroller.rememberNavController
import ui.theme.BwfTheme
import ui.viewmodel.WorldRankingViewModel
import ui.widget.WorldRankingCard
import ui.widget.WorldRankingCardPlacement

@Composable
fun WorldRankingScreen(
    navController: NavController,
    worldRankingViewModel: WorldRankingViewModel = WorldRankingViewModel.viewModel()
) {
    val uiState by worldRankingViewModel.uiState.collectAsState()
    val alpha by animateFloatAsState(
        targetValue = if (uiState.isLoading) 1f else 0f,
        tween(durationMillis = 300),
        finishedListener = {
            worldRankingViewModel.setLoading(false)
            worldRankingViewModel.finishLoadingAnimation(true)
        })
    // Side-load the ranking table
    if (uiState.isLoading) {
        RefreshRanking(perPageKey = 100, worldRankingViewModel = worldRankingViewModel)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colors.surface),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "世界羽联排名",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {},
                    backgroundColor = Color.Transparent,
                    elevation = Dp.Unspecified,
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigateBack()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
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
                    .background(MaterialTheme.colors.surface.copy(alpha = 0.75f))
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
                    color = MaterialTheme.colors.primary
                )
            }
        }

    }
}

@Composable
fun WorldRankingContent(
    worldRankingViewModel: WorldRankingViewModel = WorldRankingViewModel.viewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by worldRankingViewModel.uiState.collectAsState()
    val rankJson = uiState.rankJson
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
                        catId = tabIndex + 6
                    )
                }
            }
        }

//        if (uiState.showPlayerChoices) {
//            Dialog(
//                onCloseRequest = { worldRankingViewModel.showPlayerChoices(false) },
//            ) {
//                Column(
//                    modifier = Modifier
//                        .background(
//                            MaterialTheme.colors.background,
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                ) {
//                    val list = uiState.bean!!.results.data
//                    TextTitle(title = "请选择一位球员", style = MaterialTheme.typography.body1)
//
//                    SuggestionChip(
//                        onClick = {
//                            val intent = Intent()
//                            intent.putExtra("playerId", list[uiState.rankIndex].player1_id)
//                            intent.putExtra("catId", tabIndex + 6)
//                            intent.setClass(
//                                context,
//                                PlayerProfileActivity::class.java
//                            )
//                            context.startActivity(intent)
//                            worldRankingViewModel.showPlayerChoices(false)
//                        },
//                        shape = RoundedCornerShape(16.dp),
//                        modifier = Modifier.padding(start = 16.dp),
//                        label = {
//                            Text(text = list[uiState.rankIndex].player1_model.name_display)
//                        },
//                        icon = {
//                            AsyncImage(
//                                model = BwfApi.FLAG_URL + list[uiState.rankIndex].player1_model.country_model.flag_name_svg,
//                                contentDescription = "player1 nation flag",
//                                modifier = Modifier.size(25.dp),
//                                imageLoader = ImageLoader.Builder(
//                                    LocalContext.current
//                                ).components {
//                                    add(SvgDecoder.Factory())
//                                    if (Build.VERSION.SDK_INT >= 28) {
//                                        add(ImageDecoderDecoder.Factory())
//                                    } else {
//                                        add(GifDecoder.Factory())
//                                    }
//                                }.build()
//                            )
//                        }
//                    )
//
//                    SuggestionChip(
//                        onClick = {
//                            val intent = Intent()
//                            intent.putExtra("playerId", list[uiState.rankIndex].player2_id)
//                            intent.putExtra("catId", tabIndex + 6)
//                            intent.setClass(
//                                context,
//                                PlayerProfileActivity::class.java
//                            )
//                            context.startActivity(intent)
//                            worldRankingViewModel.showPlayerChoices(false)
//                        },
//                        shape = RoundedCornerShape(16.dp),
//                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
//                        label = {
//                            Text(text = list[uiState.rankIndex].player2_model!!.name_display)
//                        },
//                        icon = {
//                            AsyncImage(
//                                model = BwfApi.FLAG_URL + list[uiState.rankIndex].player2_model!!.country_model.flag_name_svg,
//                                contentDescription = "player2 nation flag",
//                                modifier = Modifier.size(25.dp),
//                                imageLoader = ImageLoader.Builder(
//                                    LocalContext.current
//                                ).components {
//                                    add(SvgDecoder.Factory())
//                                    if (Build.VERSION.SDK_INT >= 28) {
//                                        add(ImageDecoderDecoder.Factory())
//                                    } else {
//                                        add(GifDecoder.Factory())
//                                    }
//                                }.build()
//                            )
//                        }
//                    )
//                }
//            }
//        }

    }
}

@Composable
private fun RefreshRanking(perPageKey: Int, worldRankingViewModel: WorldRankingViewModel) {
    val uiState by worldRankingViewModel.uiState.collectAsState()

    SideEffect {
        Fuel.post(BwfApi.WORLD_RANKING)
            .body(uiState.apiBean.copy(pageKey = "$perPageKey").toJson())
            .header(Headers.CONTENT_TYPE, "application/json")
            .header(
                Headers.AUTHORIZATION,
                BwfApi.BWFAPI_AUTHORIZATION
            )
            .responseString { _, _, result ->
                result.fold({
                    worldRankingViewModel.updateRankJson(it)
                    worldRankingViewModel.setLoading(false)
                }, {})
            }
    }
}

@Preview
@Composable
fun WorldRankingContentPreview() {
    BwfTheme {
        val navController by rememberNavController(Screen.WorldRankingScreen.name)
        WorldRankingScreen(navController)
    }
}