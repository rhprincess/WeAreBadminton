package io.twinkle.wearebadminton.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import io.twinkle.wearebadminton.activity.AllYearMatchActivity
import io.twinkle.wearebadminton.data.Tournaments
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.widget.TournamentCard
import io.twinkle.wearebadminton.utilities.BwfApi

@Composable
fun AllYearMatch(activity: AllYearMatchActivity) {
    // 需要传递的数据
    val sourceCode = remember { mutableStateOf("") }
    val isLoaded = remember { mutableStateOf(false) }
    val tournaments = remember { mutableStateListOf<Tournaments>() }
    // 侧加载网络请求
    SideEffect {
        val matchNameRegex = ">.*[\\u4E00-\\u9FA5][^事](100|)赛+.*</strong".toRegex()
        val matchImgRegex =
            "<img src='(https?://www.badmintoncn.com/cbo_img/gg/|https://extranet.bwfbadminton.com/)+.*/>".toRegex()
        val matchLevelRegex = "/>.*[\\u4E00-\\u9FA5]+.*</td".toRegex()
        // 获取赛事页面源代码
        Fuel.get(BwfApi.MATCHES_OF_2023)
            .header(Headers.CONTENT_TYPE, "text/plain,charset=utf-8")
            .header(Headers.ACCEPT, "text/plain,charset=utf-8")
            .responseString { _, response, _ ->
                if (response.responseMessage == "OK") {
                    sourceCode.value = response.toString()
                    val names = matchNameRegex.findAll(response.toString())
                    val imgs = matchImgRegex.findAll(response.toString())
                    val levels = matchLevelRegex.findAll(response.toString())
                    for ((index, t) in names.withIndex()) {
                        // 将匹配到的字符中冗余部分替换掉
                        val name = t.value.replace("</strong", "").replace(">", "")
                        val img = imgs.elementAt(index).value.replace("<img src='", "")
                            .replace("' width=\"35\" align=\"left\"/>", "")
                        val level =
                            levels.elementAt(index).value.replace(
                                "/><span class=\"smalltext graytext2\">",
                                ""
                            ).replace("</span></td", "")
                        // 加进列表
                        tournaments.add(Tournaments(name = name, imgLink = img, level = level))
                    }
                    isLoaded.value = true
                }
            }
    }
    AllYearMatchUI(sourceCode, isLoaded, tournaments)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllYearMatchUI(
    code: MutableState<String>,
    isLoaded: MutableState<Boolean>,
    tournaments: SnapshotStateList<Tournaments>
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "2023赛事",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        },
        content = { innerPadding ->
            Box(Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = !isLoaded.value, modifier = Modifier
                        .align(Alignment.Center)
                        .size(56.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                LazyColumn(contentPadding = innerPadding) {
                    items(count = tournaments.size) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 15.dp, vertical = 5.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { }) {
                            TournamentCard(tournament = tournaments[it])
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun AllYearMatchPreview() {
    BwfBadmintonTheme {
    }
}