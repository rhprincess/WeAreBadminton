package io.twinkle.wearebadminton.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveScoreViewModel

@Composable
fun LiveScoreBoard(liveScoreViewModel: LiveScoreViewModel = viewModel()) {
    val uiState by liveScoreViewModel.uiState.collectAsState()
    Box(Modifier.padding(15.dp)) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),
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
                            fontColor = if (uiState.score1 > uiState.score2) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.65f
                            ) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(id = uiState.matchData.player1Country.id),
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
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                            )
                        }

                        Row(Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = {
                                    liveScoreViewModel.updateScore(
                                        score1 = uiState.score1 + 1,
                                        score2 = uiState.score2
                                    )
                                    liveScoreViewModel.applyScoreToCourt(score = uiState.score1)
                                },
                                modifier = Modifier.weight(1f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.25f
                                    )
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "increase score"
                                )
                            }
                            Spacer(Modifier.width(5.dp))
                            IconButton(
                                onClick = {
                                    liveScoreViewModel.updateScore(
                                        score1 = uiState.score1 - 1,
                                        score2 = uiState.score2
                                    )
                                    liveScoreViewModel.applyScoreToCourt(score = uiState.score1)
                                },
                                modifier = Modifier.weight(1f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.25f
                                    )
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.remove),
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
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                                    .align(Alignment.Center)
                            )
                        }
                        ScoreBoardButton(
                            text = "${uiState.duration}m",
                            textColor = MaterialTheme.colorScheme.primary,
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
                            fontColor = if (uiState.score2 > uiState.score1) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.65f
                            ) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(id = uiState.matchData.player2Country.id),
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
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                            )
                        }

                        Row(Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = {
                                    liveScoreViewModel.updateScore(
                                        score1 = uiState.score1,
                                        score2 = uiState.score2 + 1
                                    )
                                    liveScoreViewModel.applyScoreToCourt(score = uiState.score2)
                                },
                                modifier = Modifier.weight(1f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.25f
                                    )
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "increase score"
                                )
                            }
                            Spacer(Modifier.width(5.dp))
                            IconButton(
                                onClick = {
                                    liveScoreViewModel.updateScore(
                                        score1 = uiState.score1,
                                        score2 = uiState.score2 - 1
                                    )
                                    liveScoreViewModel.applyScoreToCourt(score = uiState.score2)
                                },
                                modifier = Modifier.weight(1f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.25f
                                    )
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.remove),
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

@Preview(showBackground = true)
@Composable
fun LiveScoreBoardPreview() {
    BwfBadmintonTheme {
        LiveScoreBoard()
    }
}