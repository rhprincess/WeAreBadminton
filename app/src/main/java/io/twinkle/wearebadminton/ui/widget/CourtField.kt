package io.twinkle.wearebadminton.ui.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.twinkle.wearebadminton.data.ServiceDirection
import io.twinkle.wearebadminton.ui.theme.BwfBadmintonTheme
import io.twinkle.wearebadminton.ui.viewmodel.LiveScoreViewModel

@Composable
fun CourtField(
    modifier: Modifier = Modifier,
    liveScoreViewModel: LiveScoreViewModel = viewModel(),
    courtFieldColor: Color = MaterialTheme.colorScheme.primary,
    courtLineColor: Color = Color.White,
    serveHighlightColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    val uiState by liveScoreViewModel.uiState.collectAsState()
    val singleOddScoreColorAlpha by animateFloatAsState(
        targetValue = if (uiState.singleOddScore) 1.0f else 0f,
        animationSpec = keyframes {
            durationMillis = if (uiState.singleOddScore) 600 else 300
            if (uiState.singleOddScore) {
                1f at 300
                0f at 400
                1f at 600
            }
        }
    )
    val singleEvenScoreColorAlpha by animateFloatAsState(
        targetValue = if (uiState.singleEvenScore) 1.0f else 0f,
        animationSpec = keyframes {
            durationMillis = if (uiState.singleEvenScore) 600 else 300
            if (uiState.singleEvenScore) {
                1f at 300
                0f at 400
                1f at 600
            }
        }
    )
    val doublesOddScoreColorAlpha by animateFloatAsState(
        targetValue = if (uiState.doublesOddScore) 1.0f else 0f,
        animationSpec = keyframes {
            durationMillis = if (uiState.doublesOddScore) 600 else 300
            if (uiState.doublesOddScore) {
                1f at 300
                0f at 400
                1f at 600
            }
        }
    )
    val doublesEvenScoreColorAlpha by animateFloatAsState(
        targetValue = if (uiState.doublesEvenScore) 1.0f else 0f,
        animationSpec = keyframes {
            durationMillis = if (uiState.doublesEvenScore) 600 else 300
            if (uiState.doublesEvenScore) {
                1f at 300
                0f at 400
                1f at 600
            }
        }
    )
    val rotation by animateFloatAsState(
        targetValue = when (uiState.serviceDirection) {
            ServiceDirection.NONE -> 0f
            ServiceDirection.TOP_LEFT_TO_BOTTOM_RIGHT -> -135f
            ServiceDirection.BOTTOM_RIGHT_TO_TOP_LEFT -> 45f
            ServiceDirection.BOTTOM_LEFT_TO_TOP_RIGHT -> 135f
            ServiceDirection.TOP_RIGHT_TO_BOTTOM_LEFT -> -45f
        },
        animationSpec = tween(durationMillis = 300)
    )
    Box(
        modifier = modifier
    ) {
        Canvas(
            Modifier
                .width(213.dp)
                .height(133.dp)
                .background(courtFieldColor, shape = RoundedCornerShape(8.dp))
                .padding(20.dp)
        ) {
            // ????????????
            drawRect(color = Color.White, style = Stroke(3.dp.toPx()))
            // ????????????????????????
            drawLine(
                color = courtLineColor,
                start = Offset(0f, 10.dp.toPx()),
                end = Offset(this.size.width, 10.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            // ????????????????????????
            drawLine(
                color = courtLineColor,
                start = Offset(0f, this.size.height - 10.dp.toPx()),
                end = Offset(this.size.width, this.size.height - 10.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            // ????????????????????????
            drawLine(
                color = courtLineColor,
                start = Offset((12.5).dp.toPx(), 0f),
                end = Offset((12.5).dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // ????????????????????????
            drawLine(
                color = courtLineColor,
                start = Offset(this.size.width - (12.5).dp.toPx(), 0f),
                end = Offset(this.size.width - (12.5).dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // ??????????????????
            drawLine(
                color = courtLineColor,
                start = Offset(61.dp.toPx(), 0f),
                end = Offset(61.dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // ??????????????????
            drawLine(
                color = courtLineColor,
                start = Offset(this.size.width - 61.dp.toPx(), 0f),
                end = Offset(this.size.width - 61.dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // ????????????
            drawLine(
                color = courtLineColor,
                start = Offset(0f, 46.dp.toPx()),
                end = Offset(61.dp.toPx(), 46.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            // ????????????
            drawLine(
                color = courtLineColor,
                start = Offset(this.size.width - 61.dp.toPx(), 46.dp.toPx()),
                end = Offset(this.size.width, 46.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )


            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = singleOddScoreColorAlpha),
                topLeft = Offset(0f, 10.dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )
            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = singleEvenScoreColorAlpha),
                topLeft = Offset(0f, (46.5).dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )
            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = singleEvenScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), 10.dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )
            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = singleOddScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), (46.5).dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )

            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesOddScoreColorAlpha),
                topLeft = Offset((12.5).dp.toPx(), 10.dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesEvenScoreColorAlpha),
                topLeft = Offset((12.5).dp.toPx(), (46.5).dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesEvenScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), 10.dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
            // ????????????????????????
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesOddScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), (46.5).dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
        }
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Service Direction",
            modifier = Modifier
                .align(Alignment.Center)
                .size(32.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                .rotate(rotation),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview
@Composable
fun LiveScoreActivityUiPreview() {
    BwfBadmintonTheme {
        CourtField()
    }
}