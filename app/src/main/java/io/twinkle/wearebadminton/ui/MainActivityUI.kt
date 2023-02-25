package io.twinkle.wearebadminton.ui

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.twinkle.wearebadminton.activity.*
import io.twinkle.wearebadminton.data.FunctionListItem
import io.twinkle.wearebadminton.ui.widget.FunctionListCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainActivityUI(activity: MainActivity) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "BWF Badminton",
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
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
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
                                            FunctionListItem.NewMatch -> {
                                                val intent = Intent()
                                                intent.setClass(
                                                    activity,
                                                    LiveScoreActivity::class.java
                                                )
                                                activity.startActivity(intent)
                                            }
                                            FunctionListItem.AllYearMatches -> {
                                                val intent = Intent()
                                                intent.setClass(
                                                    activity,
                                                    AllYearMatchActivity::class.java
                                                )
                                                activity.startActivity(intent)
                                            }
                                            FunctionListItem.MatchHistory -> {
                                                val intent = Intent()
                                                intent.setClass(
                                                    activity,
                                                    HistoryActivity::class.java
                                                )
                                                activity.startActivity(intent)
                                            }
                                            FunctionListItem.WorldRanking -> {
                                                val intent = Intent()
                                                intent.setClass(
                                                    activity,
                                                    WorldRankingActivity::class.java
                                                )
                                                activity.startActivity(intent)
                                            }
                                            FunctionListItem.HeadToHead -> {
                                                val intent = Intent()
                                                intent.setClass(
                                                    activity,
                                                    HeadToHeadActivity::class.java
                                                )
                                                activity.startActivity(intent)
                                            }
                                            FunctionListItem.PopularPlayers -> {
                                                val intent = Intent()
                                                intent.setClass(
                                                    activity,
                                                    PlayersActivity::class.java
                                                )
                                                activity.startActivity(intent)
                                            }
                                            else -> {}
                                        }
                                    })
                        }
                    }
                }
            }
        }
    )
}