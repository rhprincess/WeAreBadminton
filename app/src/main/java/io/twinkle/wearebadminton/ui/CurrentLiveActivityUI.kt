package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import io.twinkle.wearebadminton.data.bean.CurrentLiveBean
import io.twinkle.wearebadminton.data.bean.CurrentLiveResult
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.widget.CurrentLiveMatchCard
import io.twinkle.wearebadminton.utilities.BwfApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentLiveActivityUI() {
    val currentLiveList = remember { mutableStateListOf<CurrentLiveResult>() }
    SideEffect {
        fetchCurrentLive(currentLiveList)
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "赛事直播",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
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
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    items(currentLiveList.size) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            CurrentLiveMatchCard(currentLiveList[it])
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

@Preview(showBackground = true)
@Composable
fun CurrentLiveActivityUIPreview() {
    BwfBadmintonTheme {
        CurrentLiveActivityUI()
    }
}