package io.twinkle.wearebadminton.ui.widget

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.twinkle.wearebadminton.data.IndonesiaOpen
import io.twinkle.wearebadminton.data.MatchData
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScoreCard(data: MatchData, index: Int, size: MutableState<Int>) {
    val player1 = data.player1Name
    val player2 = data.player2Name
    val player1Scores = data.player1Scores
    val player2Scores = data.player2Scores
    val removed = remember { mutableStateOf(false) }

    val deleteAction = SwipeAction(
        icon = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Match",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        background = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        isUndo = true,
        onSwipe = {
//            IndonesiaOpen.removeAt(index)
//            removed.value = true
//            size.value = IndonesiaOpen.size
        },
    )

    SwipeableActionsBox(
        endActions = listOf(deleteAction),
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),
        swipeThreshold = 125.dp
    ) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)),
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
                        // 比赛轮次
                        Text(
                            text = data.round.round,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    ScoreBoardButton(
                        text = data.duration,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                    ScoreBoardButton(
                        text = "STATISTICS",
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    // Player 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(10.dp, 5.dp, 10.dp, 0.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 选手一国旗
                        Image(
                            painter = painterResource(id = data.player1Country.id),
                            contentDescription = "player1",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(25.dp),
                            contentScale = ContentScale.Fit
                        )
                        // 赢输状态
                        WinnerStatus(player1Scores = player1Scores, player2Scores = player2Scores)
                        // 选手一姓名
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(5.dp, 0.dp, 0.dp, 0.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = player1,
                                modifier = Modifier.align(Alignment.CenterStart),
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = if (isWinner(
                                        player1Scores,
                                        player2Scores
                                    )
                                ) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        // 选手一分数
                        LazyRow(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(count = player1Scores.size) {
                                if (player1Scores[it] != 0 && player2Scores[it] != 0) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
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
                            .height(40.dp)
                            .padding(10.dp, 5.dp, 10.dp, 0.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 选手二国旗
                        Image(
                            painter = painterResource(id = data.player2Country.id),
                            contentDescription = "player2",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(25.dp),
                            contentScale = ContentScale.Fit
                        )
                        // 赢输状态
                        WinnerStatus(player1Scores = player2Scores, player2Scores = player1Scores)
                        // 选手二姓名
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(5.dp, 0.dp, 0.dp, 0.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = player2,
                                modifier = Modifier.align(Alignment.CenterStart),
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = if (isWinner(
                                        player2Scores,
                                        player1Scores
                                    )
                                ) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        // 选手二分数
                        LazyRow(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(count = player2Scores.size) {
                                if (player1Scores[it] != 0 && player2Scores[it] != 0) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
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
            }
        }
    }
}

@Composable
fun ScoreBoardButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.Black,
    size: TextUnit = 12.sp,
    isOnFocus: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier
            .padding(2.dp, 2.dp, 0.dp, 0.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color = MaterialTheme.colorScheme.primary.copy(alpha = if (isOnFocus) alpha else 0.25f))
            .clickable {}
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = size,
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun WinnerStatus(player1Scores: List<Int>, player2Scores: List<Int>) {
    val won = isWinner(player1Scores, player2Scores)
    Box(modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)) {
        Box(
            modifier = Modifier
                .size(if (won) 15.dp else 0.dp)
                .padding(2.dp)
                .clip(CircleShape)
                .background(
                    color = if (won) MaterialTheme.colorScheme.tertiary else Color.Transparent
                )
        )
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
fun MatchScoreCardPreview() {
    BwfBadmintonTheme {
        val size = remember { mutableStateOf(IndonesiaOpen.size) }
        MatchScoreCard(data = IndonesiaOpen[0], 0, size)
    }
}