package screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import kotlinx.coroutines.launch
import navcontroller.NavController
import ui.theme.Theme
import ui.widget.DialogWindowTitleBar
import ui.widget.SettingsItem
import utilities.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(navController: NavController, theme: MutableState<Int>, darkTheme: MutableState<Boolean>) {
    val previousSizeCount = remember { mutableStateOf(4) }
    val worldRankPerPage = remember { mutableStateOf(100) }
    val refreshingFrequency = remember { mutableStateOf(10) }
    var showBreakingDown by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        theme.value = DataStoreUtils.readIntData(Constants.KEY_THEME)
        previousSizeCount.value = DataStoreUtils.readIntData(Constants.KEY_MATCH_PREVIOUS_SIZE, 4)
        worldRankPerPage.value = DataStoreUtils.readIntData(Constants.KEY_WORLD_RANKING_COUNT_PER_PAGE, 100)
        refreshingFrequency.value = DataStoreUtils.readIntData(Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY, 5)
        showBreakingDown = DataStoreUtils.readBooleanData(Constants.KEY_SHOW_BREAKING_DOWN, true)
        darkTheme.value = DataStoreUtils.readBooleanData(Constants.KEY_DARK_MODE, false)
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "设置",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                backgroundColor = Color.Transparent,
                elevation = Dp.Unspecified,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateBack()
                    }) {
                        AutoThemedIcon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        AutoThemedIcon(
//                            painter = painterResource(""),
//                            contentDescription = "Theme",
//                            modifier = Modifier
//                                .size(56.dp)
//                                .padding(12.dp),
//                            tint = MaterialTheme.colors.primary
//                        )
                        Box(Modifier.size(56.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "主题颜色",
                            fontSize = 15.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "当前主题: ${Theme.get(theme.value).name}",
                            fontSize = 15.sp,
                            color = MaterialTheme.colors.onSurface.copy(0.75f),
                        )
                    }
                    Column(
                        Modifier.height(65.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val lazyListState = rememberLazyListState()
                        val adapter = rememberScrollbarAdapter(lazyListState)
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            state = lazyListState
                        ) {
                            items(Theme.THEME_SIZE) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(
                                            color = if (darkTheme.value) {
                                                Theme.get(it).darkColors.primary
                                            } else {
                                                Theme.get(it).lightColors.primary
                                            },
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            theme.value = it
                                            scope.launch {
                                                DataStoreUtils.putData(Constants.KEY_THEME, it)
                                            }
                                        }
                                ) {
                                    AutoThemedIcon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .alpha(if (theme.value == it) 1f else 0f)
                                            .padding(8.dp)
                                            .fillMaxSize()
                                    )
                                }
                            }
                        }
                        HorizontalScrollbar(
                            adapter = adapter,
                            modifier = Modifier.fillMaxWidth(),
                            style = LocalScrollbarStyle.current.copy(
                                hoverColor = MaterialTheme.colors.primary,
                                unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }

                SettingsItem(
                    title = "夜间模式",
                    subtitle = "切换夜间模式",
                    icon = {
                        AutoThemedIcon(
                            painter = useIcon("nightlight", SvgIcon.OUTLINE),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colors.onSurface
                        )
                        Box(Modifier.size(56.dp))
                    },
                    custom = {
                        Switch(
                            checked = darkTheme.value,
                            onCheckedChange = {
                                darkTheme.value = it
                                scope.launch {
                                    DataStoreUtils.putData(Constants.KEY_DARK_MODE, it)
                                }
                                darkTheme.value = it
                            }
                        )
                    },
                    onClick = {
                        darkTheme.value = !darkTheme.value
                        scope.launch {
                            DataStoreUtils.putData(Constants.KEY_DARK_MODE, darkTheme.value)
                        }
                    }
                )

                /**                       近期比赛显示数量                       **/
                var previousCountDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "近期比赛显示数量",
                    subtitle = "在运动员主页中显示最近的比赛赛事比分情况",
                    icon = {
                        AutoThemedIcon(
                            painter = useIcon("numbers", SvgIcon.OUTLINE),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colors.onSurface
                        )
                        Box(Modifier.size(56.dp))
                    },
                    custom = {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${previousSizeCount.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    onClick = { previousCountDialog = true }
                )

                val tags = arrayListOf(2, 3, 4, 5, 6, 7, 8)

                if (previousCountDialog) {
                    Dialog(onDismissRequest = { previousCountDialog = false }, properties = DialogProperties()) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background,
                                    shape = RoundedCornerShape(8.dp)
                                ).clip(RoundedCornerShape(8.dp))
                        ) {
                            DialogWindowTitleBar(title = "近期比赛显示数量") { previousCountDialog = false }

                            Column(Modifier.verticalScroll(rememberScrollState())) {
                                tags.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            previousSizeCount.value = it
                                            previousCountDialog = false
                                            scope.launch {
                                                DataStoreUtils.putData(Constants.KEY_MATCH_PREVIOUS_SIZE, it)
                                            }
                                        }) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            RadioButton(
                                                selected = it == previousSizeCount.value,
                                                onClick = {
                                                    previousSizeCount.value = it
                                                    scope.launch {
                                                        DataStoreUtils.putData(Constants.KEY_MATCH_PREVIOUS_SIZE, it)
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(text = "$it")
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))
                                    }
                                }
                            }

                        }
                    }
                }
                /**                       近期比赛显示数量                       **/

                /**                       世界排名显示数量                       **/
                var worldRankingPageDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "世界排名显示数量",
                    subtitle = "世界排名页面最多显示前多少名的排名情况",
                    icon = {
//                        AutoThemedIcon(
//                            painter = painterResource(""),
//                            contentDescription = "Theme",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(12.dp),
//                            tint = MaterialTheme.colors.onSurface
//                        )
                        Box(Modifier.size(56.dp))
                    },
                    custom = {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${worldRankPerPage.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    onClick = { worldRankingPageDialog = true }
                )

                val perPage = arrayListOf(10, 20, 25, 50, 100)

                if (worldRankingPageDialog) {
                    Dialog(onDismissRequest = { previousCountDialog = false }, properties = DialogProperties()) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background,
                                    shape = RoundedCornerShape(8.dp)
                                ).verticalScroll(rememberScrollState())
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            DialogWindowTitleBar(title = "世界排名显示数量") { worldRankingPageDialog = false }

                            Column {
                                perPage.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            worldRankPerPage.value = it
                                            worldRankingPageDialog = false
                                            scope.launch {
                                                DataStoreUtils.putData(Constants.KEY_WORLD_RANKING_COUNT_PER_PAGE, it)
                                            }
                                        }) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            RadioButton(
                                                selected = it == worldRankPerPage.value,
                                                onClick = {
                                                    worldRankPerPage.value = it
                                                    scope.launch {
                                                        DataStoreUtils.putData(
                                                            Constants.KEY_WORLD_RANKING_COUNT_PER_PAGE,
                                                            it
                                                        )
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(text = "$it")
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))
                                    }
                                }
                            }

                        }
                    }
                }

                SettingsItem(
                    title = "显示赛季积分",
                    subtitle = "显示运动员页面的赛事积分栏目",
                    icon = {
//                        AutoThemedIcon(
//                            painter = painterResource(""),
//                            contentDescription = "Theme",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(12.dp),
//                            tint = MaterialTheme.colors.onSurface
//                        )
                        Box(Modifier.size(56.dp))
                    },
                    custom = {
                        Switch(
                            checked = showBreakingDown,
                            onCheckedChange = {
                                showBreakingDown = it
                                scope.launch {
                                    DataStoreUtils.putData(Constants.KEY_SHOW_BREAKING_DOWN, it)
                                }
                            }
                        )
                    },
                    onClick = {
                        showBreakingDown = !showBreakingDown
                        scope.launch {
                            DataStoreUtils.putData(Constants.KEY_SHOW_BREAKING_DOWN, showBreakingDown)
                        }
                    }
                )

                var liveMatchRefreshFrequencyDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "实时比分刷新频率(秒)",
                    subtitle = "每${refreshingFrequency.value}秒后，刷新当前正在直播的所有比赛比分",
                    icon = {
//                        AutoThemedIcon(
//                            imageVector = Icons.Filled.Refresh,
//                            contentDescription = "Score",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(12.dp),
//                            tint = MaterialTheme.colors.onSurface
//                        )
                        Box(Modifier.size(56.dp))
                    },
                    custom = {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${refreshingFrequency.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    onClick = { liveMatchRefreshFrequencyDialog = true }
                )

                val frequency = arrayListOf(3, 5, 10, 15, 20)

                if (liveMatchRefreshFrequencyDialog) {
                    Dialog(onDismissRequest = { previousCountDialog = false }, properties = DialogProperties()) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background,
                                    shape = RoundedCornerShape(8.dp)
                                ).clip(RoundedCornerShape(8.dp))
                        ) {
                            DialogWindowTitleBar(title = "实时比分刷新频率(秒)") { liveMatchRefreshFrequencyDialog = false }

                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                frequency.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            refreshingFrequency.value = it
                                            liveMatchRefreshFrequencyDialog = false
                                            scope.launch {
                                                DataStoreUtils.putData(
                                                    Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY,
                                                    it
                                                )
                                            }
                                        }) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            RadioButton(
                                                selected = it == refreshingFrequency.value,
                                                onClick = {
                                                    refreshingFrequency.value = it
                                                    scope.launch {
                                                        DataStoreUtils.putData(
                                                            Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY,
                                                            it
                                                        )
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(text = "$it")
                                        }

                                        Spacer(modifier = Modifier.width(20.dp))
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
    )
}