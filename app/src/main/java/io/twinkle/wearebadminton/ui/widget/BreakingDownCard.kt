package io.twinkle.wearebadminton.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.twinkle.wearebadminton.data.bean.Breaks
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreakingDownCard(breaks: Breaks) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Divider(thickness = (0.5).dp, color = MaterialTheme.colorScheme.onSurface.copy(0.15f))
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
                    text = breaks.date,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
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
                    text = breaks.name,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
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
                    color = MaterialTheme.colorScheme.onSurface
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
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                color = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BreakingDownCardPlacementPreview() {
    BwfBadmintonTheme {
        BreakingDownCardPlacement()
    }
}