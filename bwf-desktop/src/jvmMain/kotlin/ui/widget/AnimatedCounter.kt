package ui.widget

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 25.sp,
    fontColor: Color = Color.Black,
    style: TextStyle = MaterialTheme.typography.body2
) {
    var oldCount by remember {
        mutableStateOf(count)
    }
    SideEffect {
        oldCount = count
    }
    Box(modifier = modifier) {
        Row(modifier = Modifier.align(Alignment.Center)) {
            val countString = count.toString()
            val oldCountString = oldCount.toString()
            for (i in countString.indices) {
                val oldChar = oldCountString.getOrNull(i)
                val newChar = countString[i]
                val char = if (oldChar == newChar) {
                    oldCountString[i]
                } else {
                    countString[i]
                }
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        if (count < oldCount) {
                            slideInVertically { -it } with slideOutVertically { it }
                        } else {
                            slideInVertically { it } with slideOutVertically { -it }
                        }
                    }
                ) { s ->
                    Text(
                        text = s.toString(),
                        style = style,
                        fontSize = fontSize,
                        color = fontColor,
                        softWrap = false
                    )
                }
            }
        }
    }
}