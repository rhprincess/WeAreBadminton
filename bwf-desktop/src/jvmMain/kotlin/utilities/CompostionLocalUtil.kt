package utilities

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

val LocalScreenSize = compositionLocalOf { IntSize(375.dp.value.roundToInt(), 812.dp.value.roundToInt()) }