package ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object PureAndroidTheme : BaseTheme {

    override val name: String = "Pure Android"

    private val md_theme_light_primary = Color(0xFF00696A)
    private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    private val md_theme_light_primaryContainer = Color(0xFF6FF6F8)
    private val md_theme_light_secondary = Color(0xFF4A6363)
    private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    private val md_theme_light_error = Color(0xFFBA1A1A)
    private val md_theme_light_onError = Color(0xFFFFFFFF)
    private val md_theme_light_background = Color(0xFFFAFDFC)
    private val md_theme_light_onBackground = Color(0xFF191C1C)
    private val md_theme_light_surface = Color(0xFFFAFDFC)
    private val md_theme_light_onSurface = Color(0xFF191C1C)

    private val md_theme_dark_primary = Color(0xFF4CDADB)
    private val md_theme_dark_onPrimary = Color(0xFF003737)
    private val md_theme_dark_primaryContainer = Color(0xFF004F50)
    private val md_theme_dark_secondary = Color(0xFFB0CCCB)
    private val md_theme_dark_onSecondary = Color(0xFF1B3435)
    private val md_theme_dark_secondaryContainer = Color(0xFF324B4B)
    private val md_theme_dark_error = Color(0xFFFFB4AB)
    private val md_theme_dark_onError = Color(0xFF690005)
    private val md_theme_dark_background = Color(0xFF191C1C)
    private val md_theme_dark_onBackground = Color(0xFFE0E3E2)
    private val md_theme_dark_surface = Color(0xFF191C1C)
    private val md_theme_dark_onSurface = Color(0xFFE0E3E2)

    override val lightColors = lightColors(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        secondary = md_theme_light_secondary,
        onSecondary = md_theme_light_onSecondary,
        error = md_theme_light_error,
        onError = md_theme_light_onError,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_onSurface,
        primaryVariant = md_theme_light_primaryContainer,
        secondaryVariant = md_theme_dark_secondaryContainer
    )


    override val darkColors = darkColors(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_onSecondary,
        error = md_theme_dark_error,
        onError = md_theme_dark_onError,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_onSurface,
        primaryVariant = md_theme_dark_primaryContainer,
        secondaryVariant = md_theme_dark_secondaryContainer
    )
}