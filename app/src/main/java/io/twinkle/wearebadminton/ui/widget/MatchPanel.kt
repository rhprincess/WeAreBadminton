package io.twinkle.wearebadminton.ui.widget

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.data.IndonesiaOpen
import io.twinkle.wearebadminton.data.bean.MatchPreviousResults
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.theme.RankDownColor
import io.twinkle.wearebadminton.utilities.BwfApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchPanel(results: MatchPreviousResults? = null) {
    val imageLoader = ImageLoader.Builder(
        LocalContext.current
    ).components {
        add(SvgDecoder.Factory())
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    val player1Scores = remember { mutableStateListOf<Int>() }
    val player2Scores = remember { mutableStateListOf<Int>() }

    LaunchedEffect(key1 = Unit) {
        results?.match_set_model?.forEach {
            player1Scores.add(it.team1)
            player2Scores.add(it.team2)
        }
    }

    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // 分数牌信息
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .padding(10.dp, 5.dp, 10.dp, 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    // 赛事标题
                    Text(
                        text = if (results?.tournament_model?.name != null) results.tournament_model.name else "",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                // Player 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 45.dp)
                        .padding(10.dp, 5.dp, 10.dp, 0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 选手一国旗
                    AsyncImage(
                        model = BwfApi.FLAG_URL + results?.t1p1country_model?.flag_name_svg,
                        contentDescription = "player1",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(25.dp),
                        contentScale = ContentScale.Fit,
                        imageLoader = imageLoader
                    )
                    // 选手一姓名
                    Column(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 45.dp)
                            .padding(start = 10.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (results?.t1p1_player_model?.name_display != null) results.t1p1_player_model.name_display else "PLAYER1",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = if (isWinner(
                                    player1Scores,
                                    player2Scores
                                )
                            ) FontWeight.Bold else FontWeight.Normal
                        )
                        if (results?.t1p2_player_model != null) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = results.t1p2_player_model.name_display,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = if (isWinner(
                                        player1Scores,
                                        player2Scores
                                    )
                                ) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    // 赢输状态
                    WinnerStatus(player1Scores = player1Scores, player2Scores = player2Scores)
                    // 选手一分数
                    LazyRow(
                        modifier = Modifier.defaultMinSize(minHeight = 45.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(count = player1Scores.size) {
                            if (player1Scores[it] != 0 && player2Scores[it] != 0) {
                                Box(
                                    modifier = Modifier
                                        .defaultMinSize(minHeight = 45.dp)
                                        .width(20.dp)
                                ) {
                                    Text(
                                        text = player1Scores[it].toString(),
                                        fontSize = 14.sp,
                                        fontWeight = if (compareScores(
                                                player1Scores[it],
                                                player2Scores[it]
                                            )
                                        ) FontWeight.Bold else FontWeight.Normal,
                                        color = if (compareScores(
                                                player1Scores[it],
                                                player2Scores[it]
                                            )
                                        ) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                            alpha = 0.5f
                                        ),
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                    }
                }
                // Player 2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 45.dp)
                        .padding(10.dp, 5.dp, 10.dp, 0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 选手二国旗
                    AsyncImage(
                        model = BwfApi.FLAG_URL + results?.t2p1country_model?.flag_name_svg,
                        contentDescription = "player2",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(25.dp),
                        contentScale = ContentScale.Fit,
                        imageLoader = imageLoader
                    )
                    // 选手二姓名
                    Column(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 45.dp)
                            .padding(start = 10.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (results?.t2p1_player_model?.name_display != null) results.t2p1_player_model.name_display else "PLAYER2",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = if (isWinner(
                                    player2Scores,
                                    player1Scores
                                )
                            ) FontWeight.Bold else FontWeight.Normal
                        )
                        if (results?.t2p2_player_model != null) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = results.t2p2_player_model.name_display,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = if (isWinner(
                                        player2Scores,
                                        player1Scores
                                    )
                                ) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    // 赢输状态
                    WinnerStatus(player1Scores = player2Scores, player2Scores = player1Scores)
                    // 选手二分数
                    LazyRow(
                        modifier = Modifier.defaultMinSize(minHeight = 45.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(count = player2Scores.size) {
                            if (player1Scores[it] != 0 && player2Scores[it] != 0) {
                                Box(
                                    modifier = Modifier
                                        .defaultMinSize(minHeight = 45.dp)
                                        .width(20.dp)
                                ) {
                                    Text(
                                        text = player2Scores[it].toString(),
                                        fontSize = 14.sp,
                                        fontWeight = if (compareScores(
                                                player2Scores[it],
                                                player1Scores[it]
                                            )
                                        ) FontWeight.Bold else FontWeight.Normal,
                                        color = if (compareScores(
                                                player2Scores[it],
                                                player1Scores[it]
                                            )
                                        ) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                            alpha = 0.5f
                                        ),
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Round & Duration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .padding(10.dp, 5.dp, 10.dp, 0.dp)
            ) {
                // EVENT
                Text(
                    text = if (results?.tournament_model?.name != null) "ROUND - EVENT: " + results.draw_model.name else "",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

//                Text(
//                    text = if (results?.tournament_model?.name != null) results.draw_model.name else "",
//                    fontSize = 12.sp,
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd),
//                    color = MaterialTheme.colorScheme.onPrimaryContainer
//                )
            }
        }
    }
}

@Composable
private fun WinnerStatus(player1Scores: List<Int>, player2Scores: List<Int>) {
    val won = isWinner(player1Scores, player2Scores)
    if (won) {
        Box(modifier = Modifier.size(16.dp)) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Won",
                modifier = Modifier.fillMaxSize(),
                tint = RankDownColor
            )
        }
    }
}

/**
 * If score1 is larger than score2 and is less than 22, player1 won the game.
 * When score1 is larger than or equal to 22, the player1's score must two score larger than player2's.
 * Once the score1 is reach to 30, player1 won the game, and there is no limitation that the score1 must two score larger than score2.
 *
 * eg.
 * 21-17,25-23,30-29
 */
private fun compareScores(score1: Int, score2: Int): Boolean {
    return if (score1 > score2) {
        if (score1 < 22) {
            true
        } else if (score1 in 22..29) {
            score1 - score2 == 2
        } else score1 <= 30
    } else false
}

/**
 * Use the compareScores function to compare each game, and judge who is the winner.
 */
private fun isWinner(player1Scores: List<Int>, player2Scores: List<Int>): Boolean {
    var wons = 0
    player1Scores.withIndex().forEach {
        if (compareScores(player1Scores[it.index], player2Scores[it.index])) {
            wons++
        }
    }
    return wons >= 2
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MatchPanelPreview() {
    BwfBadmintonTheme {
        val size = remember { mutableStateOf(IndonesiaOpen.size) }
        MatchPanel()
    }
}