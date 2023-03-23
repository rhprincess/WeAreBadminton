package ui.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FunctionListCard(
    modifier: Modifier = Modifier,
    title: String = "功能标题"
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(8.dp)
                )
                .defaultMinSize(125.dp, 125.dp)
                .padding(12.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface,
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
    MaterialTheme {
        FunctionListCard()
    }
}