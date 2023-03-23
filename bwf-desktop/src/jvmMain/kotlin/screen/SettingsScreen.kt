package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import io.twinkle.wearebadminton.ui.widget.TextTitle
import navcontroller.NavController
import navcontroller.rememberNavController
import ui.theme.BwfTheme
import ui.theme.Theme
import ui.widget.SettingsItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val theme = remember { mutableStateOf(0) }
    val previousSizeCount = remember { mutableStateOf(4) }
    val worldRankPerPage = remember { mutableStateOf(100) }
    val refreshingFrequency = remember { mutableStateOf(10) }
    var showBreakingDown by remember { mutableStateOf(true) }
    var dark by remember { mutableStateOf(true) }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
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
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Icon(
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
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {

                        }
                        items(Theme.THEME_SIZE) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = if (dark) {
                                            Theme.get(it).darkColors.primary
                                        } else {
                                            Theme.get(it).lightColors.primary
                                        },
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        theme.value = it
                                    }
                            ) {
                                Icon(
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
                }

                /**                       近期比赛显示数量                       **/
                var previousCountDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "近期比赛显示数量",
                    subtitle = "在运动员主页中显示最近的比赛赛事比分情况",
                    icon = {
//                        Icon(
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
                    Dialog(
                        title = "近期比赛显示数量",
                        state = DialogState(width = 325.dp, height = 435.dp),
                        onCloseRequest = { previousCountDialog = false },
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            TextTitle(title = "近期比赛显示数量")

                            Column(Modifier.verticalScroll(rememberScrollState())) {
                                tags.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            previousSizeCount.value = it
                                            previousCountDialog = false
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
//                        Icon(
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
                    Dialog(
                        title = "世界排名显示数量",
                        state = DialogState(width = 325.dp, height = 415.dp),
                        onCloseRequest = { worldRankingPageDialog = false },
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background,
                                    shape = RoundedCornerShape(8.dp)
                                ).verticalScroll(rememberScrollState())
                        ) {
                            TextTitle(title = "世界排名显示数量")

                            Column {
                                perPage.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            worldRankPerPage.value = it
                                            worldRankingPageDialog = false
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
//                        Icon(
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
                            }
                        )
                    },
                    onClick = {
                        showBreakingDown = !showBreakingDown
                    }
                )

                var liveMatchRefreshFrequencyDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "实时比分刷新频率(秒)",
                    subtitle = "每${refreshingFrequency.value}秒后，刷新当前正在直播的所有比赛比分",
                    icon = {
//                        Icon(
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
                    Dialog(
                        title = "实时比分刷新频率(秒)",
                        state = DialogState(width = 325.dp, height = 415.dp),
                        onCloseRequest = { liveMatchRefreshFrequencyDialog = false },
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            TextTitle(title = "实时比分刷新频率(秒)")

                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                frequency.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            refreshingFrequency.value = it
                                            liveMatchRefreshFrequencyDialog = false
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

@Preview
@Composable
fun SettingsUIPreview() {
    BwfTheme {
        val navController by rememberNavController(Screen.MainScreen.name)
        SettingsScreen(navController)
    }
}