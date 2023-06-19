package io.twinkle.wearebadminton.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.theme.Theme
import io.twinkle.wearebadminton.ui.widget.SettingsItem
import io.twinkle.wearebadminton.ui.widget.TextTitle
import io.twinkle.wearebadminton.utilities.Constants
import io.twinkle.wearebadminton.utilities.settings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun SettingsUI() {
    val dark = isSystemInDarkTheme()
    val context = LocalContext.current
    val theme = remember { mutableStateOf(0) }
    val previousSizeCount = remember { mutableStateOf(4) }
    val worldRankPerPage = remember { mutableStateOf(100) }
    val refreshingFrequency = remember { mutableStateOf(10) }
    var showBreakingDown by remember { mutableStateOf(true) }
    var dynamicColor by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        context.settings.data.collect {
            theme.value = it[Constants.KEY_THEME] ?: 0
            previousSizeCount.value = it[Constants.KEY_MATCH_PREVIOUS_SIZE] ?: 4
            worldRankPerPage.value = it[Constants.WORLD_RANKING_COUNT_PER_PAGE] ?: 100
            refreshingFrequency.value = it[Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY] ?: 10
            showBreakingDown = it[Constants.KEY_SHOW_BREAKING_DOWN] ?: true
            dynamicColor = it[Constants.KEY_DYNAMIC_COLOR] ?: true
        }
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.theme),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .size(56.dp)
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "主题颜色",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "当前主题: ${Theme.get(theme.value).name}",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.75f),
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
                                            Theme.get(it).DarkColors.primary
                                        } else {
                                            Theme.get(it).LightColors.primary
                                        },
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        if (dynamicColor) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "请先将动态壁纸主题颜色关闭后再使用主题色",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        } else {
                                            theme.value = it
                                            scope.launch {
                                                context.settings.edit { pref ->
                                                    pref[Constants.KEY_THEME] = it
                                                }
                                            }
                                        }
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

                SettingsItem(
                    title = "动态壁纸主题颜色",
                    subtitle = "当该选项处于开的状态的时候，应用的主题颜色将会动态地跟随壁纸而变化",
                    custom = {
                        Switch(
                            checked = dynamicColor,
                            onCheckedChange = {
                                dynamicColor = it
                                scope.launch {
                                    context.settings.edit { pref ->
                                        pref[Constants.KEY_DYNAMIC_COLOR] = dynamicColor
                                    }
                                }
                            }
                        )
                    },
                    onClick = {
                        dynamicColor = !dynamicColor
                        scope.launch {
                            context.settings.edit {
                                it[Constants.KEY_DYNAMIC_COLOR] = dynamicColor
                            }
                        }
                    }
                )

                /**                       近期比赛显示数量                       **/
                var previousCountDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "近期比赛显示数量",
                    subtitle = "在运动员主页中显示最近的比赛赛事比分情况",
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.scoreboard),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    custom = {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${previousSizeCount.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    onClick = { previousCountDialog = true }
                )

                val tags = arrayListOf(2, 3, 4, 5, 6, 7, 8)

                if (previousCountDialog) {
                    AlertDialog(
                        onDismissRequest = { previousCountDialog = false },
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            TextTitle(title = "近期比赛显示数量")

                            Column {
                                tags.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            previousSizeCount.value = it
                                            scope.launch {
                                                context.settings.edit { pref ->
                                                    pref[Constants.KEY_MATCH_PREVIOUS_SIZE] =
                                                        it
                                                }
                                            }
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
                                                    scope.launch {
                                                        context.settings.edit { pref ->
                                                            pref[Constants.KEY_MATCH_PREVIOUS_SIZE] =
                                                                it
                                                        }
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
                        Icon(
                            painter = painterResource(id = R.drawable.one_rect),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    custom = {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${worldRankPerPage.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    onClick = { worldRankingPageDialog = true }
                )

                val perPage = arrayListOf(10, 20, 25, 50, 100)

                if (worldRankingPageDialog) {
                    AlertDialog(
                        onDismissRequest = { worldRankingPageDialog = false },
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            TextTitle(title = "世界排名显示数量")

                            Column {
                                perPage.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            worldRankPerPage.value = it
                                            scope.launch {
                                                context.settings.edit { pref ->
                                                    pref[Constants.WORLD_RANKING_COUNT_PER_PAGE] =
                                                        it
                                                }
                                            }
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
                                                    scope.launch {
                                                        context.settings.edit { pref ->
                                                            pref[Constants.WORLD_RANKING_COUNT_PER_PAGE] =
                                                                it
                                                        }
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
                        Icon(
                            painter = painterResource(id = R.drawable.breaking_down),
                            contentDescription = "Theme",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    custom = {
                        Switch(
                            checked = showBreakingDown,
                            onCheckedChange = {
                                showBreakingDown = it
                                scope.launch {
                                    context.settings.edit { pref ->
                                        pref[Constants.KEY_SHOW_BREAKING_DOWN] = showBreakingDown
                                    }
                                }
                            }
                        )
                    },
                    onClick = {
                        showBreakingDown = !showBreakingDown
                        scope.launch {
                            context.settings.edit {
                                it[Constants.KEY_SHOW_BREAKING_DOWN] = showBreakingDown
                            }
                        }
                    }
                )

                var liveMatchRefreshFrequencyDialog by remember { mutableStateOf(false) }
                SettingsItem(
                    title = "实时比分刷新频率(秒)",
                    subtitle = "每${refreshingFrequency.value}秒后，刷新当前正在直播的所有比赛比分",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Score",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    custom = {
                        Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${refreshingFrequency.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    onClick = { liveMatchRefreshFrequencyDialog = true }
                )

                val frequency = arrayListOf(3, 5, 10, 15, 20)

                if (liveMatchRefreshFrequencyDialog) {
                    AlertDialog(
                        onDismissRequest = { liveMatchRefreshFrequencyDialog = false },
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            TextTitle(title = "实时比分刷新频率(秒)")

                            Column {
                                frequency.forEach {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            refreshingFrequency.value = it
                                            scope.launch {
                                                context.settings.edit { pref ->
                                                    pref[Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY] =
                                                        it
                                                }
                                            }
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
                                                    scope.launch {
                                                        context.settings.edit { pref ->
                                                            pref[Constants.KEY_LIVE_MATCH_REFRESHING_FREQUENCY] =
                                                                it
                                                        }
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

@Preview(showBackground = true)
@Composable
fun SettingsUIPreview() {
    BwfBadmintonTheme {
        SettingsUI()
    }
}