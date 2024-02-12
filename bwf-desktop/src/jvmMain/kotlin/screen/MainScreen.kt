package screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import data.FunctionListItem
import navcontroller.NavController
import ui.widget.FunctionListCard
import utilities.AutoThemedIcon
import utilities.LocalScreenSize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val windowSize = LocalScreenSize.current
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "世界羽联信息平台",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.SettingsScreen)
                    }) {
                        AutoThemedIcon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colors.onSurface
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
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(5.dp, 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(FunctionListItem.values()) {
                        Box(Modifier.padding(5.dp)) {
                            FunctionListCard(
                                title = it.title,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .align(Alignment.Center)
                                    .clickable {
                                        when (it) {
                                            FunctionListItem.AllYearMatches -> {
                                                navController.navigate(Screen.AllYearMatchesScreen)
                                            }

                                            FunctionListItem.WorldRanking -> {
                                                navController.navigate(Screen.WorldRankingScreen)
                                            }

                                            FunctionListItem.LiveMatch -> {
                                                navController.navigate(Screen.CurrentLiveScreen)
                                            }

                                            FunctionListItem.HeadToHead -> {
                                                navController.navigate(Screen.HeadToHeadScreen)
                                            }

                                            FunctionListItem.PopularPlayers -> {
                                                navController.navigate(Screen.PopularPlayersScreen)
                                            }
                                        }
                                    })
                        }
                    }
                }
            }
        }
    )
}