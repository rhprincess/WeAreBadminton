package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import data.bean.*
import data.payload.*
import io.twinkle.wearebadminton.ui.widget.TextTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import navcontroller.NavController
import navcontroller.rememberNavController
import ui.theme.BwfTheme
import ui.viewmodel.PlayerProfileViewModel
import ui.widget.*
import utilities.BwfApi
import utilities.Constants
import utilities.DataStoreUtils
import kotlin.math.roundToInt

@Composable
fun PlayerProfileScreen(
    navController: NavController,
    playerProfileViewModel: PlayerProfileViewModel = PlayerProfileViewModel.viewModel()
) {
    val bundle = navController.screenBundle
    val playerId: String = bundle.strings["playerId"] ?: ""
    val catId: Int = bundle.ints["catId"] ?: 6

    val uiState by playerProfileViewModel.uiState.collectAsState()
    val previousResults = remember {
        mutableStateListOf<MatchPreviousResults>()
    }
    val density = LocalDensity.current
    val breakingDownList = remember { mutableStateListOf<Breaks>() }
    val galleryList = remember { mutableStateListOf<String>() }

    val previousSizeCount = DataStoreUtils.readIntData(Constants.KEY_MATCH_PREVIOUS_SIZE)
    val showBreakingDown = DataStoreUtils.readBooleanData(Constants.KEY_SHOW_BREAKING_DOWN)

    // 定义 ToolBar 的高度
    val toolbarHeight = 225.dp
    val statusBarHeight = 42.dp
    // ToolBar 最大向上位移量
    // 56.dp 参考自 androidx.compose.material AppBar.kt 里面定义的 private val AppBarHeight = 56.dp
    val maxUpPx = with(LocalDensity.current) {
        56.dp.value + statusBarHeight.value
    }
    // ToolBar 最小向上位移量
    val minUpPx = 0f
    // 偏移折叠工具栏上移高度
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val scrollState = rememberScrollState()
    val scrollbarAdapter = rememberScrollbarAdapter(scrollState)

    var isExpanded by remember { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                // 设置 ToolBar 的位移范围
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-maxUpPx, -minUpPx)
                return Offset.Zero
            }
        }
    }

    SideEffect {
        fetchImageAndRanking(playerId, catId, playerProfileViewModel)

        fetchPlayerData(playerName = uiState.name, uiState.catId, breakingDownList)

        fetchPreviousMatch(
            playerId = uiState.id,
            previousResults = previousResults,
            offset = 0,
            previousCount = when (previousSizeCount) {
                2 -> 0
                3 -> 1
                4 -> 2
                5 -> 3
                6 -> 4
                7 -> 5
                8 -> 6
                else -> 2
            }
        )

        fetchPlayerGallery(playerId = uiState.id, galleryList = galleryList)
    }

    // Player Banner
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight + toolbarOffsetHeightPx.value.roundToInt().dp)
        ) {
            // Player Image
            if (uiState.bannerImgUrl.isNotEmpty()) {
                AsyncImage(
                    load = {
                        loadImageBitmap(uiState.bannerImgUrl)
                    },
                    contentDescription = "background image",
                    imageFor = { it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp),
                    contentScale = ContentScale.Crop
                )
            } else Box(Modifier.fillMaxWidth().height(225.dp))

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(85.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.worldRank,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 48.sp,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp, top = 42.dp)
            ) {
                // Player Information
                Row {
                    if (uiState.avatarUrl.isNotEmpty()) {
                        AsyncImage(
                            load = {
                                loadImageBitmap(uiState.avatarUrl)
                            },
                            imageFor = { it },
                            contentDescription = "Player Avatar",
                            modifier = Modifier
                                .size(85.dp - (35.dp * (-(toolbarOffsetHeightPx.value / maxUpPx))))
                                .clip(CircleShape)
                                .border(2.dp, Color.White, shape = CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else Box(Modifier.size(85.dp))

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        // Name
                        Text(
                            text = buildAnnotatedString {
                                uiState.name.split(" ").forEach {
                                    if (it == uiState.lastName) {
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("$it ")
                                        }
                                    } else {
                                        append("$it ")
                                    }
                                }
                            },
                            color = Color.White,
                            modifier = Modifier.width(132.dp + (93.dp * (-(toolbarOffsetHeightPx.value / maxUpPx)))),
                            style = if (-(toolbarOffsetHeightPx.value / maxUpPx) > 0.5f) MaterialTheme.typography.h6 else MaterialTheme.typography.h5
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nation Icon
                            if (uiState.flagUrl.isNotEmpty()) {
                                val flag = uiState.flagUrl
                                val painter = handleNationIcon(flag)
                                AsyncImage(
                                    load = {
                                        loadSvgPainter(
                                            uiState.flagUrl,
                                            density
                                        )
                                    },
                                    imageFor = { painter ?: it },
                                    contentDescription = "nation",
                                    modifier = Modifier
                                        .size(32.dp * (1f - (-(toolbarOffsetHeightPx.value / maxUpPx))))
                                        .clip(CircleShape)
                                        .alpha(1f - (-(toolbarOffsetHeightPx.value / maxUpPx) * 2f))
                                        .border(1.dp, Color.White, shape = CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else Box(Modifier.size(32.dp))

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = uiState.country,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "WORLD TOUR RANK: ${uiState.worldTourRank}",
//                    fontSize = 13.sp,
//                    color = Color.White
//                )
            }

            Box(Modifier.padding(8.dp)) {
                IconButton(onClick = { navController.navigateBack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back",
                        tint = Color.White
                    )
                }
            }

            Box(Modifier.padding(8.dp).align(Alignment.BottomEnd)) {
                IconButton(onClick = {
                    if (isExpanded) {
                        isExpanded = false
                        toolbarOffsetHeightPx.value = -maxUpPx
                    } else {
                        isExpanded = true
                        toolbarOffsetHeightPx.value = -minUpPx
                    }
                }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "collapse",
                        tint = Color.White
                    )
                }
            }
        }

        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colors.surface)
                    .padding(bottom = 16.dp)
                    .verticalScroll(scrollState)
            ) {

                TextTitle(title = "基本信息")

                Box(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .background(
                            MaterialTheme.colors.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (uiState.bioModel != null) {
                            PlayerInfoItem(key = "姓名", value = uiState.name)
                            PlayerInfoItem(key = "身高", value = uiState.bioModel!!.height ?: "N/A")
                            PlayerInfoItem(
                                key = "惯用手", value = when (uiState.bioModel!!.plays) {
                                    "1" -> "右手"
                                    "2" -> "左手"
                                    else -> "N/A"
                                }
                            )
                            PlayerInfoItem(
                                key = "现居地",
                                value = uiState.bioModel!!.current_residence ?: "N/A"
                            )
                            PlayerInfoItem(key = "语言", value = uiState.bioModel!!.languages ?: "N/A")
                            PlayerInfoItem(key = "选手ID", value = uiState.id)
                        } else {
                            PlayerInfoItem(key = "N/A", value = "N/A")
                        }
                    }
                }

                // Previous Matches

                val lazyListState1 = rememberLazyListState()
                val lazyListState2 = rememberLazyListState()
                val screenWidth = 375.dp - 16.dp
                val coroutineScope = rememberCoroutineScope()

                TitleWithPageControl(
                    title = "近期比赛 (${previousResults.size})",
                    modifier = Modifier.fillMaxWidth(),
                    lazyListState = lazyListState1,
                    coroutineScope = coroutineScope,
                    size = previousResults.size
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    state = lazyListState1
                ) {
                    items(previousResults.size) {
                        Box(
                            modifier = Modifier
                                .width(screenWidth)
                                .padding(horizontal = 16.dp)
//                            .shadow(3.dp, shape = RoundedCornerShape(8.dp))
                        ) {
                            MatchPanel(results = previousResults[it])
                        }
                    }
                }

                if (showBreakingDown) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextTitle(title = "赛季积分")
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(45.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "tournaments",
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    }

                    Column(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .background(
                                MaterialTheme.colors.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        BreakingDownCardPlacement()
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    when (breakingDownList.size) {
                                        0 -> 32.dp
                                        1, 2, 3 -> screenWidth / 2
                                        else -> screenWidth
                                    }
                                )
                        ) {
                            if (breakingDownList.size == 0) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "无赛事积分数据",
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                                            style = MaterialTheme.typography.body2
                                        )
                                    }
                                }
                            }
                            items(breakingDownList.size) {
                                BreakingDownCard(breaks = breakingDownList[it])
                            }
                        }
                    }

                }

                TitleWithPageControl(
                    title = "选手图库 (${galleryList.size})",
                    modifier = Modifier.fillMaxWidth(),
                    lazyListState = lazyListState2,
                    coroutineScope = coroutineScope,
                    size = galleryList.size
                )

                // player's gallery
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    state = lazyListState2
                ) {
                    items(galleryList.size) {
                        GalleryItem(imgUrl = galleryList[it])
                    }
                }
            }

            VerticalScrollbar(
                adapter = scrollbarAdapter,
                modifier = Modifier.fillMaxHeight(),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = MaterialTheme.colors.primary,
                    unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.15f)
                )
            )
        }
    }

}

@Composable
private fun TitleWithPageControl(
    modifier: Modifier = Modifier,
    title: String,
    lazyListState: LazyListState,
    coroutineScope: CoroutineScope,
    size: Int
) {
    var current by remember { mutableStateOf(0) }
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        TextTitle(title = title, modifier = Modifier.weight(1f))
        IconButton(onClick = {
            coroutineScope.launch {
                if (size > 0) {
                    lazyListState.animateScrollToItem(
                        if (current - 1 >= 0) {
                            current -= 1
                            current
                        } else 0
                    )
                }
            }
        }, modifier = Modifier.padding(vertical = 8.dp)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Localized description",
                tint = MaterialTheme.colors.onSurface
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = {
            coroutineScope.launch {
                if (size > 0) {
                    lazyListState.animateScrollToItem(
                        if (current + 1 <= size - 1) {
                            current += 1
                            current
                        } else size - 1
                    )
                }
            }
        }, modifier = Modifier.padding(vertical = 8.dp)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Localized description",
                tint = MaterialTheme.colors.onSurface
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

private fun fetchImageAndRanking(playerId: String, catId: Int, playerProfileViewModel: PlayerProfileViewModel) {
    val playerSummaryApiBean = PlayerSummaryPayload(playerId = playerId)
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
                        id = playerId,
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
        .body(RankingEventsPayload(playerId = playerId).toJson())
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
                            playerId = playerId,
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

private fun fetchPreviousMatch(
    playerId: String,
    previousResults: SnapshotStateList<MatchPreviousResults>,
    offset: Int,
    previousCount: Int
) {
    Fuel.post(BwfApi.MATCH_PREVIOUS).body(
        MatchPreviousPayload(
            playerId = playerId,
            previousOffset = offset,
            isPara = false,
            drawCount = 1 + offset
        ).toJson()
    )
        .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
        .header("dnt", 1)
        .header(
            Headers.AUTHORIZATION,
            BwfApi.BWFAPI_AUTHORIZATION
        ).responseObject(MatchPreviousBean.Deserializer()) { _, _, result ->
            result.fold({
                if (!previousResults.contains(it.results) &&
                    it.results.t1p1_player_model != null &&
                    (it.results.t1p1_player_model.id.toString() == playerId ||
                            it.results.t2p1_player_model?.id.toString() == playerId ||
                            it.results.t1p2_player_model?.id.toString() == playerId ||
                            it.results.t2p2_player_model?.id.toString() == playerId)
                ) {
                    previousResults.add(it.results)
                    if (offset <= previousCount) {
                        fetchPreviousMatch(playerId, previousResults, offset + 1, previousCount)
                    }
                }
            }, {})
        }
}

private fun fetchPlayerGallery(playerId: String, galleryList: SnapshotStateList<String>) {
    Fuel.post(BwfApi.PLAYER_GALLERY).body(PlayerGalleryPayload(playerId = playerId).toJson())
        .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
        .header(
            Headers.AUTHORIZATION,
            BwfApi.BWFAPI_AUTHORIZATION
        ).responseObject(PlayerGalleryBean.Deserializer()) { _, _, result ->
            result.fold({ bean ->
                bean.results.forEach {
                    if (!galleryList.contains(it.src))
                        galleryList.add(it.src)
                }
            }, {})
        }
}

private fun fetchPlayerData(
    playerName: String,
    catId: Int,
    breakingDownList: SnapshotStateList<Breaks>
) {
    var isResponse = false
    val head = "{\"results\":{\"current_page\":1,\"data\":["
    val tail =
        "],\"first_page_url\":\"https:\\/\\/extranet-lv.bwfbadminton.com\\/api\\/vue-rankingtable?page=1\",\"from\":1,\"last_page\":1,\"last_page_url\":\"https:\\/\\/extranet-lv.bwfbadminton.com\\/api\\/vue-rankingtable?page=1\",\"next_page_url\":null,\"path\":\"https:\\/\\/extranet-lv.bwfbadminton.com\\/api\\/vue-rankingtable\",\"per_page\":1,\"prev_page_url\":null,\"to\":1,\"total\":1},\"drawCount\":1}"

    Fuel.post(BwfApi.WORLD_RANKING)
        .body(RankPayload(searchKey = playerName, pageKey = "1", catId = catId).toJson())
        .header(Headers.CONTENT_TYPE, "application/json")
        .header(
            Headers.AUTHORIZATION,
            BwfApi.BWFAPI_AUTHORIZATION
        )
        .responseString { _, _, result ->
            result.fold({
                if (catId > 7) {
                    val gson = GsonBuilder().serializeNulls().create()
                    val bean = gson.fromJson(it, RankingBean::class.java)
                    bean.results.data[0].ranking_category_id
                    if (bean.results.data.isNotEmpty() && !isResponse) {
                        val playerData = gson.toJson(bean.results.data)
                        isResponse = true
                        fetchBreakingDown(
                            playerData.substring(1, playerData.length - 1),
                            catId = catId,
                            breakingDownList = breakingDownList
                        )
                    }
                } else {
                    if (it.contains(head) and it.contains(tail)) {
                        if (!isResponse) {
                            val playerData = it.replace(head, "").replace(tail, "")
                            isResponse = true
                            fetchBreakingDown(
                                playerData = playerData,
                                catId = catId,
                                breakingDownList = breakingDownList
                            )
                        }
                    }
                }
            }, {})
        }
}

private fun fetchBreakingDown(
    playerData: String,
    catId: Int,
    breakingDownList: SnapshotStateList<Breaks>
) {
    val json = "{\"rankId\":2,\"catId\":$catId,\"playerData\":$playerData}"
    Fuel.post(BwfApi.BREAKING_DOWN)
        .body(json)
        .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
        .header(
            Headers.AUTHORIZATION,
            BwfApi.BWFAPI_AUTHORIZATION
        ).responseString { _, _, result ->
            result.fold({ s ->
                val bean = Gson().fromJson(s, Array<Breaks>::class.java)
                bean.forEach {
                    if (!breakingDownList.contains(it))
                        breakingDownList.add(it)
                }
            }, {})
        }
}

@Preview
@Composable
fun PlayerProfileActivityUIPreview() {
    BwfTheme {
        val navController by rememberNavController(startDestination = Screen.PlayerProfileScreen.name)
        PlayerProfileScreen(navController)
    }
}