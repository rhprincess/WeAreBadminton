package io.twinkle.wearebadminton.ui.widget

import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.data.bean.LiveDetail
import io.twinkle.wearebadminton.data.bean.MatchDetail
import io.twinkle.wearebadminton.ui.theme.RankUpColor
import io.twinkle.wearebadminton.utilities.BwfApi

@Composable
fun LiveCourtCard(matchDetail: MatchDetail, liveDetail: LiveDetail, hasUpdate: Boolean) {
    val alpha by animateFloatAsState(targetValue = if (hasUpdate) 1f else 0f, keyframes {
        durationMillis = 750
        if (hasUpdate) {
            0.5f at 150
            1f at 300
            0.5f at 450
            1f at 750
        } else {
            1f at 150
            0.5f at 300
            1f at 450
            0f at 750
        }
    })

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

    val t1g1 = liveDetail.team1_g1_score ?: 0
    val t1g2 = liveDetail.team1_g2_score ?: 0
    val t1g3 = liveDetail.team1_g3_score ?: 0
    val t2g1 = liveDetail.team2_g1_score ?: 0
    val t2g2 = liveDetail.team2_g2_score ?: 0
    val t2g3 = liveDetail.team2_g3_score ?: 0

    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .border(
                width = if (hasUpdate) 3.dp else 1.dp,
                color = if (hasUpdate) MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha) else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.15f
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // 场地 和 时间信息
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp, 5.dp, 10.dp, 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        // 场地标题
                        Text(
                            text = "Court " + liveDetail.court_code,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    MatchDurationIndicator()

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = convertTime(liveDetail.duration),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(0.15f))

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
                        model = BwfApi.FLAG_URL + matchDetail.t1p1country_model?.flag_name_svg,
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
                            text = matchDetail.t1p1_player_model?.name_short1 ?: "",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Normal
                        )
                        if (matchDetail.t1p2_player_model != null) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = matchDetail.t1p2_player_model.name_short1,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    // 发球状态 -----------------------
                    if (liveDetail.service_player == 1 || liveDetail.service_player == 2) {
                        ServiceStat()
                    }

                    // 选手一分数
                    Row(
                        modifier = Modifier.defaultMinSize(minHeight = 45.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (t1g1 != 0 || t2g1 != 0) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 45.dp)
                                    .width(20.dp)
                            ) {
                                Text(
                                    text = t1g1.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }

                        if (t1g2 != 0 || t2g2 != 0) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 45.dp)
                                    .width(20.dp)
                            ) {
                                Text(
                                    text = t1g2.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }

                        if (t1g3 != 0 || t2g3 != 0) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 45.dp)
                                    .width(20.dp)
                            ) {
                                Text(
                                    text = t1g3.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
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
                        model = BwfApi.FLAG_URL + matchDetail.t2p1country_model?.flag_name_svg,
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
                            text = matchDetail.t2p1_player_model?.name_short1 ?: "",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Normal
                        )
                        if (matchDetail.t2p2_player_model != null) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = matchDetail.t2p2_player_model.name_short1,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    // 发球状态  ---------------------
                    if (liveDetail.service_player == 3 || liveDetail.service_player == 4) {
                        ServiceStat()
                    }

                    // 选手二分数
                    Row(
                        modifier = Modifier.defaultMinSize(minHeight = 45.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (t1g1 != 0 || t2g1 != 0) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 45.dp)
                                    .width(20.dp)
                            ) {
                                Text(
                                    text = t2g1.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }

                        if (t1g2 != 0 || t2g2 != 0) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 45.dp)
                                    .width(20.dp)
                            ) {
                                Text(
                                    text = t2g2.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }

                        if (t1g3 != 0 || t2g3 != 0) {
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 45.dp)
                                    .width(20.dp)
                            ) {
                                Text(
                                    text = t2g3.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    }
                }
            }

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(0.15f))

            // Round & MatchState
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(10.dp, 5.dp, 10.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Round
                Text(
                    text = liveDetail.event + " - " + liveDetail.round,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = liveDetail.match_state_name,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ServiceStat() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1500
                0f at 0
                1f at 600
                0f at 750
                1f at 1500
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .padding(10.dp, 0.dp, 0.dp, 0.dp)
            .size(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color = RankUpColor.copy(alpha1))
        )
    }
}