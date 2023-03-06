package io.twinkle.wearebadminton.ui

import android.os.Build
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.twinkle.wearebadminton.data.bean.*
import io.twinkle.wearebadminton.data.payload.MatchPreviousPayload
import io.twinkle.wearebadminton.data.payload.PlayerGalleryPayload
import io.twinkle.wearebadminton.data.payload.RankPayload
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayerProfileViewModel
import io.twinkle.wearebadminton.ui.widget.*
import io.twinkle.wearebadminton.utilities.BwfApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfileActivityUI(playerProfileViewModel: PlayerProfileViewModel = viewModel()) {
    val uiState by playerProfileViewModel.uiState.collectAsState()
    val previousResults = remember {
        mutableStateListOf<MatchPreviousResults>()
    }
    val breakingDownList = remember { mutableStateListOf<Breaks>() }
    val galleryList = remember { mutableStateListOf<String>() }
    val imageLoader = ImageLoader.Builder(
        LocalContext.current
    ).components {
        add(SvgDecoder.Factory())
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    val scrollState = rememberScrollState()
    // 定义 ToolBar 的高度
    val toolbarHeight = 225.dp
    // ToolBar 最大向上位移量
    // 56.dp 参考自 androidx.compose.material AppBar.kt 里面定义的 private val AppBarHeight = 56.dp
    val maxUpPx = with(LocalDensity.current) {
        toolbarHeight.roundToPx().toFloat() - 169.dp.roundToPx().toFloat()
    }
    // ToolBar 最小向上位移量
    val minUpPx = 0f
    // 偏移折叠工具栏上移高度
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

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

    val view = LocalView.current
    SideEffect {
        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = false

        fetchPreviousMatch(
            playerId = uiState.id,
            previousResults = previousResults,
            offset = 0,
            previousCount = 2
        )

        fetchPlayerGallery(playerId = uiState.id, galleryList = galleryList)

        fetchPlayerData(playerName = uiState.name, uiState.catId, breakingDownList)
    }

    // Player Banner
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight + toolbarOffsetHeightPx.value.roundToInt().dp)
        ) {
            // Player Image
            AsyncImage(
                model = uiState.bannerImgUrl,
                contentDescription = "background image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp),
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop
            )

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
                    AsyncImage(
                        model = uiState.avatarUrl,
                        contentDescription = "Player Avatar",
                        modifier = Modifier
                            .size(85.dp - (35.dp * (-(toolbarOffsetHeightPx.value / maxUpPx))))
                            .clip(CircleShape)
                            .border(2.dp, Color.White, shape = CircleShape),
                        imageLoader = imageLoader,
                        contentScale = ContentScale.Crop
                    )

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
                            style = if (-(toolbarOffsetHeightPx.value / maxUpPx) > 0.5f) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nation Icon
                            AsyncImage(
                                model = uiState.flagUrl,
                                contentDescription = "nation",
                                modifier = Modifier
                                    .size(32.dp * (1f - (-(toolbarOffsetHeightPx.value / maxUpPx))))
                                    .clip(CircleShape)
                                    .alpha(1f - (-(toolbarOffsetHeightPx.value / maxUpPx) * 2f))
                                    .border(1.dp, Color.White, shape = CircleShape),
                                imageLoader = imageLoader,
                                contentScale = ContentScale.Crop
                            )

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
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(scrollState)
        ) {

            TextTitle(title = "基本信息")

            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
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
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val coroutineScope = rememberCoroutineScope()
            val tooltipState = remember { PlainTooltipState() }

            TitleWithPageControl(
                title = "近期比赛",
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
                    PlainTooltipBox(
                        tooltip = {
                            Text(
                                text = "赛季积分需从世界排名页面获取，赛季积分是指该球员本赛季内所参加的比赛获取的世界排名积分，世界排名积分即赛季积分总和",
                                textAlign = TextAlign.Start,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        tooltipState = tooltipState,
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                tooltipState.show()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "tournaments",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
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
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                    items(breakingDownList.size) {
                        BreakingDownCard(breaks = breakingDownList[it])
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
                tint = MaterialTheme.colorScheme.onSurface
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
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
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
            BwfApi.WORLD_RANKING_AUTHORIZATION
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
            }, {
                Log.e("PlayerProfile", "failed to fetch the previous match")
            })
        }
}

private fun fetchPlayerGallery(playerId: String, galleryList: SnapshotStateList<String>) {
    Fuel.post(BwfApi.PLAYER_GALLERY).body(PlayerGalleryPayload(playerId = playerId).toJson())
        .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
        .header(
            Headers.AUTHORIZATION,
            BwfApi.WORLD_RANKING_AUTHORIZATION
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
            BwfApi.WORLD_RANKING_AUTHORIZATION
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
            }, {
                Log.e("WorldRanking", "failed to fetch the ranking table")
            })
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
            BwfApi.WORLD_RANKING_AUTHORIZATION
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

@Preview(showBackground = true)
@Composable
fun PlayerProfileActivityUIPreview() {
    BwfBadmintonTheme {
        PlayerProfileActivityUI()
    }
}