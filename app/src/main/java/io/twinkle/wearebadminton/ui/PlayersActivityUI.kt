package io.twinkle.wearebadminton.ui

import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.google.gson.Gson
import io.twinkle.wearebadminton.activity.PlayerProfileActivity
import io.twinkle.wearebadminton.data.bean.PopularPlayersBean
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.PlayersViewModel
import io.twinkle.wearebadminton.ui.widget.PlayerAvatarCardVertical
import io.twinkle.wearebadminton.utilities.BwfApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersActivityUI(playersViewModel: PlayersViewModel = viewModel()) {
    val uiState by playersViewModel.uiState.collectAsState()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (uiState.isLoading) {
        RefreshPopularPlayers(playersViewModel = playersViewModel)
    }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "热门运动员",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        playersViewModel.updateSearchApiBean(uiState.searchApiBean.copy(searchKey = uiState.searchKey))
                        playersViewModel.setLoading(true)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "refresh the table",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = { innerPadding ->
            PlayersActivityUIContent(
                innerPadding = innerPadding,
                playersViewModel = playersViewModel
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayersActivityUIContent(
    playersViewModel: PlayersViewModel = viewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by playersViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val json = uiState.json
    val alpha by animateFloatAsState(targetValue = if (uiState.isLoading) 1f else 0f)
    SideEffect {
        if (json.isNotEmpty())
            playersViewModel.updatePlayersBean(
                bean = Gson().fromJson(json, PopularPlayersBean::class.java)
            )
    }

    Column(modifier = Modifier.padding(innerPadding)) {

        // 搜索球员
        TextField(
            value = uiState.searchKey,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            },
            singleLine = true,
            onValueChange = {
                playersViewModel.updateSearchKey(it)
                playersViewModel.updateSearchApiBean(uiState.searchApiBean.copy(searchKey = uiState.searchKey))
                playersViewModel.setLoading(true)
            }, modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxSize()) {

            CircularProgressIndicator(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.Center)
                    .alpha(alpha),
                color = MaterialTheme.colorScheme.primary
            )

            // 选手列表
            LazyVerticalGrid(
                contentPadding = PaddingValues(bottom = 10.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center
            ) {
                if (uiState.playersBean != null) {
                    val list = uiState.playersBean!!.results
                    items(list.size) {
                        Box(modifier = Modifier.clickable {
                            val intent = Intent()
                            intent.putExtra("playerId", list[it].id)
                            intent.setClass(
                                context,
                                PlayerProfileActivity::class.java
                            )
                            context.startActivity(intent)
                        }, contentAlignment = Alignment.Center) {
                            PlayerAvatarCardVertical(result = list[it])
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun RefreshPopularPlayers(playersViewModel: PlayersViewModel) {
    val uiState by playersViewModel.uiState.collectAsState()
    SideEffect {
        Fuel.post(BwfApi.SEARCH_PLAYERS)
            .body(uiState.searchApiBean.toJson())
            .header(Headers.CONTENT_TYPE, "application/json")
            .header(
                Headers.AUTHORIZATION,
                BwfApi.BWFAPI_AUTHORIZATION
            )
            .responseString { _, _, result ->
                result.fold({
                    println(it)
                    playersViewModel.setJson(it)
                    playersViewModel.setLoading(false)
                }, {
                    Log.e("WorldRanking", "failed to fetch the ranking table")
                })
            }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayersActivityUIPreview() {
    BwfBadmintonTheme {
        PlayersActivityUI()
    }
}