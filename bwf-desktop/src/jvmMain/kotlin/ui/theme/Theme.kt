package ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import utilities.Constants
import utilities.DataStoreUtils

@Suppress("MemberVisibilityCanBePrivate")
object Theme {
    const val DEFAULT_THEME = 0
    const val AUTUMN_LIKE_THEME = 1
    const val OCEAN_LIKE_THEME = 2
    const val SUMMER_LIKE_THEME = 3
    const val PINKKA_THEME = 4
    const val MINT_THEME = 5
    const val ORANGE_THEME = 6
    const val PURE_ANDROID_THEME = 7
    const val WEIRD_THEME = 8
    const val QCUMBER_THEME = 9
    const val THEME_SIZE = 10

    fun get(theme: Int): BaseTheme {
        return when (theme) {
            DEFAULT_THEME -> DefaultTheme
            AUTUMN_LIKE_THEME -> AutumnLikeTheme
            OCEAN_LIKE_THEME -> OceanLikeTheme
            SUMMER_LIKE_THEME -> SummerLikeTheme
            PINKKA_THEME -> PinkKaTheme
            MINT_THEME -> MintTheme
            ORANGE_THEME -> OrangeTheme
            PURE_ANDROID_THEME -> PureAndroidTheme
            WEIRD_THEME -> WeirdTheme
            QCUMBER_THEME -> QcumberTheme
            else -> DefaultTheme
        }
    }
}

@Composable
fun BwfTheme(
    theme: MutableState<Int>,
    darkTheme: MutableState<Boolean>,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        darkTheme.value = DataStoreUtils.readBooleanData(Constants.KEY_DARK_MODE, false)
    }
    val colors = if (darkTheme.value) {
        Theme.get(theme.value).darkColors
    } else {
        Theme.get(theme.value).lightColors
    }
    MaterialTheme(colors = colors, content = content)
}

@Composable
fun GlobalThemeChanger(content: @Composable (theme: MutableState<Int>, darkTheme: MutableState<Boolean>) -> Unit) {
    val trigger = remember { mutableStateOf(false) }
    val theme1 = remember { mutableStateOf(0) }
    theme1.value = DataStoreUtils.readIntData(Constants.KEY_THEME)
    content(theme1,trigger)
}