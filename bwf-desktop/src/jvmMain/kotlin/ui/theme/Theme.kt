package ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

object Theme {
    const val DEFAULT_THEME = 0
    const val AUTUMN_LIKE_THEME = 1
    const val OCEAN_LIKE_THEME = 2
    const val SUMMER_LIKE_THEME = 3
    const val PINKKA_THEME = 4
    const val MINT_THEME = 5
    const val ORANGE_THEME = 6
    const val PURE_ANDROID_THEME = 7
    const val THEME_SIZE = 8

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
            else -> DefaultTheme
        }
    }
}

@Composable
fun BwfTheme(
    theme: Int = Theme.DEFAULT_THEME,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        Theme.get(theme).darkColors
    } else {
        Theme.get(theme).lightColors
    }
    MaterialTheme(colors = colors, content = content)
}