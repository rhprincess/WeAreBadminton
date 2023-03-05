package io.twinkle.wearebadminton.ui.widget

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import io.twinkle.wearebadminton.R
import io.twinkle.wearebadminton.activity.PlayerProfileActivity
import io.twinkle.wearebadminton.data.bean.PlayerData
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.theme.RankDownColor
import io.twinkle.wearebadminton.ui.theme.RankUpColor
import io.twinkle.wearebadminton.ui.viewmodel.WorldRankingViewModel
import io.twinkle.wearebadminton.utilities.BwfApi
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun WorldRankingCard(
    viewModel: WorldRankingViewModel = viewModel(),
    index: Int,
    catId: Int,
    data: PlayerData
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Divider(thickness = (0.5).dp, color = MaterialTheme.colorScheme.onSurface.copy(0.15f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 55.dp)
                .clickable {
                    viewModel.setRankIndex(index)
                    if (data.player2_model == null) {
                        val intent = Intent()
                        intent.putExtra("playerId", data.player1_id)
                        intent.putExtra("catId", catId)
                        intent.setClass(
                            context,
                            PlayerProfileActivity::class.java
                        )
                        context.startActivity(intent)
                    } else {
                        viewModel.showPlayerChoices(true)
                    }
                }
        ) {
            // Rank
            Box(modifier = Modifier.size(55.dp)) {
                Text(
                    text = data.rank.toString(),
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            // Rank Change
            Row(
                modifier = Modifier
                    .width(36.dp)
                    .height(55.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = when {
                        data.rank_change > 0 -> Icons.Filled.KeyboardArrowUp
                        data.rank_change < 0 -> Icons.Filled.KeyboardArrowDown
                        else -> ImageVector.vectorResource(id = R.drawable.remove)
                    }, contentDescription = "rank change",
                    tint = when {
                        data.rank_change > 0 -> RankUpColor
                        data.rank_change < 0 -> RankDownColor
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (data.rank_change != 0) data.rank_change.absoluteValue.toString() else "",
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            // Name
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 55.dp)
                    .padding(start = 5.dp)
                    .weight(1f)
            ) {
                Text(
                    text = if (data.player2_model == null) data.player1_model.name_display else data.player1_model.name_display + " &\n" + data.player2_model.name_display,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            // Nation
            AsyncImage(
                modifier = Modifier
                    .size(32.dp)
                    .padding(3.dp)
                    .align(Alignment.CenterVertically),
                model = BwfApi.FLAG_URL + data.player1_model.country_model.flag_name_svg,
                contentDescription = "nation",
                imageLoader = ImageLoader.Builder(
                    LocalContext.current
                ).components {
                    add(SvgDecoder.Factory())
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }.build()
            )
            // Tournaments
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .height(55.dp)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = data.tournaments.toString(),
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            // Points
            Box(
                modifier = Modifier
                    .height(55.dp)
                    .width(65.dp)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = data.points.toDouble().roundToInt().toString(),
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldRankingCardPlacement() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        // Rank
        Box(modifier = Modifier.size(45.dp)) {
            Text(
                text = "排名",
                modifier = Modifier
                    .align(Alignment.Center),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        // Rank Change
        Spacer(
            modifier = Modifier
                .width(36.dp)
                .fillMaxHeight()
        )
        // Name
        Box(
            modifier = Modifier
                .height(45.dp)
                .padding(start = 5.dp)
                .weight(1f)
        ) {
            Text(
                text = "名字",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        // Nation
        Box(
            modifier = Modifier.size(45.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "国家",
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        // Tournaments
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(45.dp),
            contentAlignment = Alignment.Center
        ) {
            PlainTooltipBox(tooltip = {
                Text(
                    text = "赛季参加赛事数量",
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }, containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "tournaments",
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        // Points
        Box(
            modifier = Modifier
                .height(45.dp)
                .width(65.dp)
                .padding(start = 5.dp)
        ) {
            Text(
                text = "积分",
                modifier = Modifier
                    .align(Alignment.Center),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun WorldRankingCardPlacementPreview() {
    BwfBadmintonTheme {
        WorldRankingCardPlacement()
    }
}