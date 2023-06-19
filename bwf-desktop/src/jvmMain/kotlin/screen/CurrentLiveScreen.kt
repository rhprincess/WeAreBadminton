package screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import data.bean.CurrentLiveBean
import data.bean.CurrentLiveResult
import navcontroller.NavController
import navcontroller.rememberNavController
import ui.theme.BwfTheme
import ui.widget.CurrentLiveMatchCard
import utilities.BwfApi

@Composable
fun CurrentLiveScreen(navController: NavController) {
    val currentLiveList = remember { mutableStateListOf<CurrentLiveResult>() }
    SideEffect {
        fetchCurrentLive(currentLiveList)
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "赛事直播",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
                },
                backgroundColor = Color.Transparent,
                elevation = Dp.Unspecified)
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(verticalArrangement = Arrangement.Center) {
                    if (currentLiveList.isEmpty()) {
                        item {
                            Text(
                                text = "当前没有赛事",
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    items(currentLiveList.size) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            CurrentLiveMatchCard(navController,currentLiveList[it])
                        }
                    }
                }
            }
        }
    )
}

private fun fetchCurrentLive(currentLiveList: SnapshotStateList<CurrentLiveResult>) {
    Fuel.post(BwfApi.CURRENT_LIVE)
        .body("{drawCount: 0}")
        .header(Headers.CONTENT_TYPE, "application/json;charset=UTF-8")
        .header(Headers.AUTHORIZATION, BwfApi.BWFAPI_AUTHORIZATION)
        .responseObject(CurrentLiveBean.Deserializer()) { _, _, result ->
            result.fold({
                it.results.forEach { live ->
                    if (!currentLiveList.contains(live)) {
                        currentLiveList.add(live)
                    }
                }
            }, {})
        }
}

@Preview
@Composable
fun CurrentLiveScreenPreview() {
    BwfTheme {
        val navController by rememberNavController(startDestination = Screen.CurrentLiveScreen.name)
        CurrentLiveScreen(navController)
    }
}