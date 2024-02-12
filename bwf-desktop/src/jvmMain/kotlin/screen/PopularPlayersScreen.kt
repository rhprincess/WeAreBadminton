package screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import data.bean.PopularPlayersBean
import navcontroller.NavController
import ui.viewmodel.PlayersViewModel
import ui.widget.PlayerAvatarCardVertical
import utilities.BwfApi

@Composable
fun PopularPlayersScreen(
    navController: NavController,
    playersViewModel: PlayersViewModel = PlayersViewModel.viewModel()
) {
    val uiState by playersViewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        RefreshPopularPlayers(playersViewModel = playersViewModel)
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "热门运动员",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                backgroundColor = Color.Transparent,
                elevation = Dp.Unspecified,
                actions = {
                    IconButton(onClick = {
                        playersViewModel.updateSearchApiBean(uiState.searchApiBean.copy(searchKey = uiState.searchKey))
                        playersViewModel.setLoading(true)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "refresh the table",
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        },
        content = { innerPadding ->
            PopularPlayersContent(
                navController = navController,
                innerPadding = innerPadding,
                playersViewModel = playersViewModel
            )
        }
    )
}

@Composable
fun PopularPlayersContent(
    navController: NavController,
    playersViewModel: PlayersViewModel = PlayersViewModel.viewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by playersViewModel.uiState.collectAsState()
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
                color = MaterialTheme.colors.primary
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
                            val bundle = NavController.ScreenBundle()
                            bundle.strings["playerId"] = list[it].id.toString()
//                            bundle.ints["catId"] = catId
                            navController.navigate(Screen.PlayerProfileScreen, bundle)
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
            .header(Headers.CONTENT_TYPE, "application/json, text/plain, */*")
            .header(
                Headers.AUTHORIZATION,
                BwfApi.BWFAPI_AUTHORIZATION
            )
            .timeout(15000)
            .responseString { _, _, result ->
                result.fold({
                    playersViewModel.setJson(it)
                    playersViewModel.setLoading(false)
                }, {
                    it.printStackTrace()
                })
            }
    }
}