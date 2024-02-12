package screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import data.bean.RankingBean
import kotlinx.coroutines.launch
import navcontroller.NavController
import ui.viewmodel.WorldRankingViewModel
import ui.widget.*
import utilities.BwfApi
import utilities.Constants
import utilities.DataStoreUtils

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
    val perPageKey = DataStoreUtils.readIntData(Constants.KEY_WORLD_RANKING_COUNT_PER_PAGE)
    // Side-load the ranking table
    if (uiState.isLoading) {
        RefreshRanking(perPageKey = perPageKey, worldRankingViewModel = worldRankingViewModel)
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
                    worldRankingViewModel = worldRankingViewModel,
                    navController = navController
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
    navController: NavController,
    worldRankingViewModel: WorldRankingViewModel = WorldRankingViewModel.viewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by worldRankingViewModel.uiState.collectAsState()
    val rankJson = uiState.rankJson
    val scope = rememberCoroutineScope()
    SideEffect {
        worldRankingViewModel.updateRankingBean(
            Gson().fromJson(rankJson, RankingBean::class.java)
        )
    }

    Column(modifier = Modifier.padding(innerPadding)) {

        val types = listOf("男单", "女单", "男双", "女双", "混双")
        var tabIndex by remember { mutableStateOf(DataStoreUtils.readIntData(Constants.KEY_WORLD_RANKING_TAB_INDEX)) }

        // 比赛项目
        TabRow(selectedTabIndex = tabIndex) {
            types.forEachIndexed { index, s ->
                Tab(selected = index == tabIndex, onClick = {
                    tabIndex = index
                    scope.launch {
                        DataStoreUtils.putData(Constants.KEY_WORLD_RANKING_TAB_INDEX, index)
                    }
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

        Row {
            val lazyListState = rememberLazyListState()
            val adapter = rememberScrollbarAdapter(lazyListState)
            // 排名列表
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.weight(1f),
                state = lazyListState
            ) {
                if (uiState.bean != null) {
                    val list = uiState.bean!!.results.data
                    items(list.size) {
                        WorldRankingCard(
                            data = list[it],
                            index = it,
                            catId = tabIndex + 6,
                            viewModel = worldRankingViewModel,
                            navController = navController
                        )
                    }
                }
            }
            VerticalScrollbar(
                adapter = adapter,
                modifier = Modifier.fillMaxHeight(),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = MaterialTheme.colors.primary,
                    unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.15f)
                )
            )
        }

        if (uiState.showPlayerChoices) {
            Dialog(
                onDismissRequest = { worldRankingViewModel.showPlayerChoices(false) },
                properties = DialogProperties()
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.background,
                            shape = RoundedCornerShape(8.dp)
                        ).clip(RoundedCornerShape(8.dp))
                ) {
                    val list = uiState.bean!!.results.data
                    DialogWindowTitleBar("请选择一位球员") { worldRankingViewModel.showPlayerChoices(false) }

                    OutlinedButton(
                        onClick = {
                            val bundle = NavController.ScreenBundle()
                            bundle.strings["playerId"] = list[uiState.rankIndex].player1_id.toString()
                            bundle.ints["catId"] = tabIndex + 6
                            worldRankingViewModel.showPlayerChoices(false)
                            navController.navigate(Screen.PlayerProfileScreen, bundle)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(start = 16.dp),
                    ) {
                        val flag = list[uiState.rankIndex].player1_model.country_model.flag_name_svg
                        AsyncImage(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(3.dp),
                            load = {
                                loadImageBitmap(BwfApi.FLAG_URL + flag)
                            },
                            imageFor = { it },
                            contentDescription = "nation",
                            imageTransformation = ImageTransformation.Circle,
                            contentScale = ContentScale.Crop
                        )
                        Text(text = list[uiState.rankIndex].player1_model.name_display)
                    }

                    Spacer(Modifier.height(5.dp))

                    OutlinedButton(
                        onClick = {
                            val bundle = NavController.ScreenBundle()
                            bundle.strings["playerId"] = list[uiState.rankIndex].player2_id.toString()
                            bundle.ints["catId"] = tabIndex + 6
                            worldRankingViewModel.showPlayerChoices(false)
                            navController.navigate(Screen.PlayerProfileScreen, bundle)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                    ) {
                        val flag = list[uiState.rankIndex].player2_model!!.country_model.flag_name_svg
                        AsyncImage(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(3.dp),
                            load = {
                                loadImageBitmap(url = BwfApi.FLAG_URL + flag)
                            },
                            imageFor = { it },
                            contentDescription = "nation",
                            imageTransformation = ImageTransformation.Circle,
                            contentScale = ContentScale.Crop
                        )
                        Text(text = list[uiState.rankIndex].player2_model!!.name_display)
                    }
                }
            }
        }

    }
}

@Composable
private fun RefreshRanking(perPageKey: Int, worldRankingViewModel: WorldRankingViewModel) {
    val uiState by worldRankingViewModel.uiState.collectAsState()

    SideEffect {
        val index = DataStoreUtils.readIntData(Constants.KEY_WORLD_RANKING_TAB_INDEX) + 6
        Fuel.post(BwfApi.WORLD_RANKING)
            .body(uiState.apiBean.copy(pageKey = "$perPageKey", catId = index).toJson())
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