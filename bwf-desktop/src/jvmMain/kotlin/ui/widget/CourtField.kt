package ui.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import data.ServiceDirection
import ui.theme.BwfTheme
import ui.viewmodel.LocalMatchViewModel

@Composable
fun CourtField(
    modifier: Modifier = Modifier,
    localMatchViewModel: LocalMatchViewModel = LocalMatchViewModel.viewModel(),
    courtFieldColor: Color = MaterialTheme.colors.primary,
    courtLineColor: Color = Color.White,
    serveHighlightColor: Color = MaterialTheme.colors.primaryVariant
) {
    val uiState by localMatchViewModel.uiState.collectAsState()
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
            // 底线四方
            drawRect(color = Color.White, style = Stroke(3.dp.toPx()))
            // 左侧边线（上侧）
            drawLine(
                color = courtLineColor,
                start = Offset(0f, 10.dp.toPx()),
                end = Offset(this.size.width, 10.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            // 右侧边线（下侧）
            drawLine(
                color = courtLineColor,
                start = Offset(0f, this.size.height - 10.dp.toPx()),
                end = Offset(this.size.width, this.size.height - 10.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            // 左区双打发球底线
            drawLine(
                color = courtLineColor,
                start = Offset((12.5).dp.toPx(), 0f),
                end = Offset((12.5).dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // 右区双打发球底线
            drawLine(
                color = courtLineColor,
                start = Offset(this.size.width - (12.5).dp.toPx(), 0f),
                end = Offset(this.size.width - (12.5).dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // 左区发球内线
            drawLine(
                color = courtLineColor,
                start = Offset(61.dp.toPx(), 0f),
                end = Offset(61.dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // 右区发球内线
            drawLine(
                color = courtLineColor,
                start = Offset(this.size.width - 61.dp.toPx(), 0f),
                end = Offset(this.size.width - 61.dp.toPx(), this.size.height),
                strokeWidth = 3.dp.toPx()
            )
            // 左区中线
            drawLine(
                color = courtLineColor,
                start = Offset(0f, 46.dp.toPx()),
                end = Offset(61.dp.toPx(), 46.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            // 右区中线
            drawLine(
                color = courtLineColor,
                start = Offset(this.size.width - 61.dp.toPx(), 46.dp.toPx()),
                end = Offset(this.size.width, 46.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )


            // 左上单打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = singleOddScoreColorAlpha),
                topLeft = Offset(0f, 10.dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )
            // 左下单打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = singleEvenScoreColorAlpha),
                topLeft = Offset(0f, (46.5).dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )
            // 右上单打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = singleEvenScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), 10.dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )
            // 右下单打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = singleOddScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), (46.5).dp.toPx()),
                size = Size(61.dp.toPx(), 36.dp.toPx())
            )

            // 左上双打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesOddScoreColorAlpha),
                topLeft = Offset((12.5).dp.toPx(), 10.dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
            // 左下双打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesEvenScoreColorAlpha),
                topLeft = Offset((12.5).dp.toPx(), (46.5).dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
            // 右上双打颜色区块
            drawRect(
                color = serveHighlightColor.copy(alpha = doublesEvenScoreColorAlpha),
                topLeft = Offset(this.size.width - 61.dp.toPx(), 10.dp.toPx()),
                size = Size((48.5).dp.toPx(), 36.dp.toPx())
            )
            // 右下双打颜色区块
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
                .background(MaterialTheme.colors.primaryVariant, shape = CircleShape)
                .rotate(rotation),
            tint = MaterialTheme.colors.primaryVariant
        )
    }
}

@Preview
@Composable
fun LiveScoreActivityUiPreview() {
    BwfTheme {
        CourtField()
    }
}