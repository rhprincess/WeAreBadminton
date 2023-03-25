package ui.widget

import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.BwfTheme
import ui.viewmodel.LocalMatchViewModel

@Composable
fun LiveScoreBoard(localMatchViewModel: LocalMatchViewModel = LocalMatchViewModel.viewModel()) {
    val uiState by localMatchViewModel.uiState.collectAsState()
    Box(Modifier.padding(15.dp)) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.secondary.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(15.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AnimatedCounter(
                            count = uiState.score1,
                            fontSize = 56.sp,
                            modifier = Modifier
                                .size(85.dp)
                                .align(Alignment.CenterHorizontally),
                            fontColor = if (uiState.score1 > uiState.score2) MaterialTheme.colors.primary.copy(
                                alpha = 0.65f
                            ) else MaterialTheme.colors.onSurface.copy(alpha = 0.65f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(uiState.matchData.player1Country.path),
                                contentDescription = "player1",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(20.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = uiState.matchData.player1Name,
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontSize = 13.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                            )
                        }

                        Row(Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = {
                                    localMatchViewModel.updateScore(
                                        score1 = uiState.score1 + 1,
                                        score2 = uiState.score2
                                    )
                                    localMatchViewModel.applyScoreToCourt(score = uiState.score1)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "increase score"
                                )
                            }
                            Spacer(Modifier.width(5.dp))
                            IconButton(
                                onClick = {
                                    localMatchViewModel.updateScore(
                                        score1 = uiState.score1 - 1,
                                        score2 = uiState.score2
                                    )
                                    localMatchViewModel.applyScoreToCourt(score = uiState.score1)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    painter = painterResource("icons/remove.svg"),
                                    contentDescription = "decrease score"
                                )
                            }
                        }
                    }
                    // 分数分割线
                    Column(
                        Modifier
                            .weight(0.5f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            Modifier
                                .height(85.dp)
                                .fillMaxWidth()
                        ) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.15f))
                                    .align(Alignment.Center)
                            )
                        }
                        ScoreBoardButton(
                            text = "${uiState.duration}m",
                            textColor = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.dp),
                            isOnFocus = uiState.isGamePlaying
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AnimatedCounter(
                            count = uiState.score2,
                            fontSize = 56.sp,
                            modifier = Modifier
                                .size(85.dp)
                                .align(Alignment.CenterHorizontally),
                            fontColor = if (uiState.score2 > uiState.score1) MaterialTheme.colors.primary.copy(
                                alpha = 0.65f
                            ) else MaterialTheme.colors.onSurface.copy(alpha = 0.65f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(uiState.matchData.player2Country.path),
                                contentDescription = "player1",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(20.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = uiState.matchData.player2Name,
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontSize = 13.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                            )
                        }

                        Row(Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = {
                                    localMatchViewModel.updateScore(
                                        score1 = uiState.score1,
                                        score2 = uiState.score2 + 1
                                    )
                                    localMatchViewModel.applyScoreToCourt(score = uiState.score2)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "increase score"
                                )
                            }
                            Spacer(Modifier.width(5.dp))
                            IconButton(
                                onClick = {
                                    localMatchViewModel.updateScore(
                                        score1 = uiState.score1,
                                        score2 = uiState.score2 - 1
                                    )
                                    localMatchViewModel.applyScoreToCourt(score = uiState.score2)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    painter = painterResource("icons/remove.svg"),
                                    contentDescription = "decrease score"
                                )
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
            .background(color = MaterialTheme.colors.primary.copy(alpha = if (isOnFocus) alpha else 0.25f))
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

@Preview
@Composable
fun LiveScoreBoardPreview() {
    BwfTheme {
        LiveScoreBoard()
    }
}