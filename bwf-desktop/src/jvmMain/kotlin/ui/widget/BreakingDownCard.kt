package ui.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.bean.Breaks
import ui.theme.BwfTheme
import kotlin.math.roundToInt

@Composable
fun BreakingDownCard(breaks: Breaks) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Divider(thickness = (0.5).dp, color = MaterialTheme.colors.onSurface.copy(0.15f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 45.dp)
        ) {
            // Year Week
            Box(
                modifier = Modifier
                    .width(85.dp)
                    .height(45.dp)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = breaks.date,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
            )
            // Name
            Box(
                modifier = Modifier
                    .defaultMinSize(minHeight = 45.dp)
                    .padding(start = 5.dp)
                    .weight(1f)
            ) {
                Text(
                    text = breaks.name,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            // result rank
            Box(
                modifier = Modifier.size(45.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = breaks.result.toString(),
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            // Points
            Box(
                modifier = Modifier
                    .height(45.dp)
                    .width(65.dp)
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = breaks.points.toDouble().roundToInt().toString(),
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun BreakingDownCardPlacement() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        // Year Week
        Box(
            modifier = Modifier
                .size(85.dp)
                .padding(start = 16.dp)
        ) {
            Text(
                text = "年 / 周",
                modifier = Modifier
                    .align(Alignment.Center),
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        Spacer(
            modifier = Modifier
                .width(16.dp)
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
                text = "赛事",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        // result rank
        Box(
            modifier = Modifier.size(45.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "排名",
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
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
fun BreakingDownCardPlacementPreview() {
    BwfTheme {
        BreakingDownCardPlacement()
    }
}