@file:Suppress("MemberVisibilityCanBePrivate")

package io.twinkle.wearebadminton.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

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
fun BwfBadmintonTheme(
    theme: Int = Theme.DEFAULT_THEME,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    statusBarColor: Color = Color.Unspecified,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> Theme.get(theme).DarkColors
        else -> Theme.get(theme).LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            if (statusBarColor != Color.Unspecified) {
                (view.context as Activity).window.statusBarColor = statusBarColor.toArgb()
            } else {
                (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            }
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}