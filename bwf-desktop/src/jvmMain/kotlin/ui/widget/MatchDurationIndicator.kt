package ui.widget

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MatchDurationIndicator(dark: Boolean = true) {
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
                color = if (dark) MaterialTheme.colors.onSurface.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.25f),
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
                        color = if (dark) MaterialTheme.colors.onSurface.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
            )
        }
    }
}