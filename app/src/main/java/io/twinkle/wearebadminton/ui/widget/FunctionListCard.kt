package io.twinkle.wearebadminton.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.twinkle.wearebadminton.data.FunctionListItem
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme

@Composable
fun FunctionListCard(
    modifier: Modifier = Modifier,
    item: FunctionListItem
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .background(
                    item.color.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(8.dp)
                )
                .defaultMinSize(125.dp, 125.dp)
                .padding(12.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .align(
                        Alignment.Center
                    )
                    .width(75.dp)
            )
        }
    }
}

@Preview
@Composable
fun FunctionListCardPreview() {
    BwfBadmintonTheme(darkTheme = true) {
        FunctionListCard(item = FunctionListItem.WorldRanking)
    }
}