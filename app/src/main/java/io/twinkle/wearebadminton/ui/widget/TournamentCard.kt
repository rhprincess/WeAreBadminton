@file:OptIn(ExperimentalMaterial3Api::class)

package io.twinkle.wearebadminton.ui.widget

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.data.Tournaments
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun TournamentCard(tournament: Tournaments) {
    val imgLink =
        if (tournament.imgLink.contains("https")) tournament.imgLink else tournament.imgLink.replace(
            "http",
            "https"
        )
    Surface(Modifier.background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            AsyncImage(
                model = imgLink,
                contentDescription = tournament.name,
                modifier = Modifier
                    .width(110.dp)
                    .height(100.dp)
                    .padding(15.dp),
                imageLoader = ImageLoader.Builder(LocalContext.current).components {
                    add(SvgDecoder.Factory())
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }.build()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = tournament.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    val info = tournament.level.split("；")
                    // 赛事级别（类型）
                    if (info.isNotEmpty()) {
                        SuggestionChip(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(16.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(
                                    0.05f
                                )
                            ),
                            label = {
                                Text(text = info[0])
                            })
                    }
                    // 赛事奖金
                    if (info.size == 2 && info[1].isNotEmpty()) {
                        SuggestionChip(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(16.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(
                                    0.05f
                                )
                            ),
                            icon = {
                                if (info[1].contains("奖金")) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.dollar),
                                        contentDescription = "Dollar",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                if (info[1].contains("地点")) {
                                    Icon(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = "Location",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = info[1].replace("赛事地点：", "").replace("赛事奖金：USD", "")
                                        .trim()
                                )
                            })
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TournamentCardPreview() {
    BwfBadmintonTheme {
        TournamentCard(
            Tournaments(
                name = "马来西亚羽毛球公开赛",
                imgLink = "https://www.badmintoncn.com/cbo_img/gg/20221107191606T9pSCXEcmQ.png?imageslim",
                level = "超级1000；赛事奖金：USD 1,250,000"
            )
        )
    }
}