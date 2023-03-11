package io.twinkle.wearebadminton.ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import kotlin.math.roundToInt

@Composable
fun MatchDurationIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .width(42.dp)
            .height(8.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.25f),
                shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 34.dp * alpha)

        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchDurationIndicatorPreview() {
    BwfBadmintonTheme {
        MatchDurationIndicator()
    }
}