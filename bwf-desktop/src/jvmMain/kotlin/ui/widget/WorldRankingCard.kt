package ui.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.bean.PlayerData
import navcontroller.NavController
import screen.Screen
import ui.theme.BwfTheme
import ui.theme.RankDownColor
import ui.theme.RankUpColor
import ui.viewmodel.WorldRankingViewModel
import utilities.BwfApi
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun WorldRankingCard(
    index: Int,
    catId: Int,
    data: PlayerData,
    navController: NavController,
    viewModel: WorldRankingViewModel
) {
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Divider(thickness = (0.5).dp, color = MaterialTheme.colors.onSurface.copy(0.15f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 55.dp)
                .clickable {
                    viewModel.setRankIndex(index)
                    if (data.player2_model != null) {
                        viewModel.showPlayerChoices(true)
                    } else {
                        val bundle = NavController.ScreenBundle()
                        bundle.strings["playerId"] = data.player1_model.id.toString()
                        bundle.ints["catId"] = catId
                        navController.navigate(Screen.PlayerProfileScreen.name, bundle)
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
                    color = MaterialTheme.colors.onSurface
                )
            }
            // Rank Change
            Row(
                modifier = Modifier
                    .width(36.dp)
                    .height(55.dp)
            ) {
                if (data.rank_change == 0) {
                    Icon(
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource("icons/remove.svg"),
                        contentDescription = "rank change",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.25f)
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = when {
                            data.rank_change > 0 -> Icons.Filled.KeyboardArrowUp
                            else -> Icons.Filled.KeyboardArrowDown
                        }, contentDescription = "rank change",
                        tint = when {
                            data.rank_change > 0 -> RankUpColor
                            else -> RankDownColor
                        }
                    )
                }
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
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            // Nation
            val flag = data.player1_model.country_model.flag_name_svg
            val painter = handleNationIcon(flag)
            AsyncImage(
                modifier = Modifier
                    .size(32.dp)
                    .padding(3.dp)
                    .align(Alignment.CenterVertically),
                load = {
                    loadSvgPainter(
                        url = BwfApi.FLAG_URL + flag,
                        density = density
                    )
                },
                imageFor = { painter ?: it },
                contentDescription = "nation",
                imageTransformation = ImageTransformation.Circle,
                contentScale = ContentScale.Crop
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
                    color = MaterialTheme.colors.onSurface
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
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun WorldRankingCardPlacement() {
    val coroutineScope = rememberCoroutineScope()
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
                color = MaterialTheme.colors.onSurface
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
                color = MaterialTheme.colors.onSurface
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
                color = MaterialTheme.colors.onSurface
            )
        }
        // Tournaments
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(45.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "tournaments",
            )
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
                color = MaterialTheme.colors.onSurface
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Preview
@Composable
fun WorldRankingCardPlacementPreview() {
    BwfTheme {
        WorldRankingCardPlacement()
    }
}