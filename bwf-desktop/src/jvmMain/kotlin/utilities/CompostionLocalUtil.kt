package utilities

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val LocalScreenSize = compositionLocalOf { DpSize(375.dp, 812.dp) }