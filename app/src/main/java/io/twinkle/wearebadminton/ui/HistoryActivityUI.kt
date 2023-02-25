package io.twinkle.wearebadminton.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.twinkle.wearebadminton.data.CountryIcon
import io.twinkle.wearebadminton.data.MatchData
import io.twinkle.wearebadminton.data.Round
import io.twinkle.wearebadminton.data.SerializableMatchData
import io.twinkle.wearebadminton.ui.widget.MatchScoreCard
import io.twinkle.wearebadminton.utilities.getMatchesHistory
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryActivityUI() {
    val context = LocalContext.current
    var historyMatchesFile: File
    val list: SnapshotStateList<ArrayList<SerializableMatchData>> = remember {
        mutableStateListOf()
    }
    var dataList: ArrayList<SerializableMatchData> = arrayListOf()
    val matchSize = remember {
        mutableStateOf(0)
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "本地比赛历史",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            var loaded by remember { mutableStateOf(false) }
            SideEffect {
                historyMatchesFile = File(context.filesDir, "matches_history.json")
                val json = historyMatchesFile.readText()
                list.add(getMatchesHistory(json))
                matchSize.value = list[0].size
                dataList = list[0]
                loaded = true
            }
            if (loaded) {
                LazyColumn(
                    contentPadding = innerPadding,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                ) {
                    items(count = matchSize.value) {
                        val data = dataList[it]
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp, 0.dp, 10.dp, 10.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .animateItemPlacement()
                        ) {
                            MatchScoreCard(
                                MatchData(
                                    player1Name = data.player1Name,
                                    player2Name = data.player2Name,
                                    player1Country = CountryIcon.valueOf(data.player1Country),
                                    player2Country = CountryIcon.valueOf(data.player2Country),
                                    player1Scores = data.player1Scores,
                                    player2Scores = data.player2Scores,
                                    round = Round.valueOf(data.round),
                                    duration = data.duration
                                ), it, matchSize
                            )
                        }
                    }
                }
            }
        }
    )
}