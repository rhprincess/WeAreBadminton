package io.twinkle.wearebadminton.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object DefaultTheme : BaseTheme {

    override val name: String = "Default"

    private val md_theme_light_primary = Color(0xFFC0000D)
    private val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    private val md_theme_light_primaryContainer = Color(0xFFFFDAD5)
    private val md_theme_light_onPrimaryContainer = Color(0xFF410001)
    private val md_theme_light_secondary = Color(0xFF775652)
    private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    private val md_theme_light_secondaryContainer = Color(0xFFFFDAD5)
    private val md_theme_light_onSecondaryContainer = Color(0xFF2C1512)
    private val md_theme_light_tertiary = Color(0xFF9C4145)
    private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
    private val md_theme_light_tertiaryContainer = Color(0xFFFFDAD9)
    private val md_theme_light_onTertiaryContainer = Color(0xFF400009)
    private val md_theme_light_error = Color(0xFFBA1A1A)
    private val md_theme_light_errorContainer = Color(0xFFFFDAD6)
    private val md_theme_light_onError = Color(0xFFFFFFFF)
    private val md_theme_light_onErrorContainer = Color(0xFF410002)
    private val md_theme_light_background = Color(0xFFFFFBFF)
    private val md_theme_light_onBackground = Color(0xFF201A19)
    private val md_theme_light_surface = Color(0xFFFFFBFF)
    private val md_theme_light_onSurface = Color(0xFF201A19)
    private val md_theme_light_surfaceVariant = Color(0xFFF5DDDA)
    private val md_theme_light_onSurfaceVariant = Color(0xFF534341)
    private val md_theme_light_outline = Color(0xFF857370)
    private val md_theme_light_inverseOnSurface = Color(0xFFFBEEEC)
    private val md_theme_light_inverseSurface = Color(0xFF362F2E)
    private val md_theme_light_inversePrimary = Color(0xFFFFB4AA)
    private val md_theme_light_surfaceTint = Color(0xFFC0000D)
    private val md_theme_light_outlineVariant = Color(0xFFD8C2BF)
    private val md_theme_light_scrim = Color(0xFF000000)

    private val md_theme_dark_primary = Color(0xFFFFB4AA)
    private val md_theme_dark_onPrimary = Color(0xFF690003)
    private val md_theme_dark_primaryContainer = Color(0xFF930007)
    private val md_theme_dark_onPrimaryContainer = Color(0xFFFFDAD5)
    private val md_theme_dark_secondary = Color(0xFFE7BDB7)
    private val md_theme_dark_onSecondary = Color(0xFF442926)
    private val md_theme_dark_secondaryContainer = Color(0xFF5D3F3B)
    private val md_theme_dark_onSecondaryContainer = Color(0xFFFFDAD5)
    private val md_theme_dark_tertiary = Color(0xFFFFB3B3)
    private val md_theme_dark_onTertiary = Color(0xFF5F131B)
    private val md_theme_dark_tertiaryContainer = Color(0xFF7E2A2F)
    private val md_theme_dark_onTertiaryContainer = Color(0xFFFFDAD9)
    private val md_theme_dark_error = Color(0xFFFFB4AB)
    private val md_theme_dark_errorContainer = Color(0xFF93000A)
    private val md_theme_dark_onError = Color(0xFF690005)
    private val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
    private val md_theme_dark_background = Color(0xFF201A19)
    private val md_theme_dark_onBackground = Color(0xFFEDE0DE)
    private val md_theme_dark_surface = Color(0xFF201A19)
    private val md_theme_dark_onSurface = Color(0xFFEDE0DE)
    private val md_theme_dark_surfaceVariant = Color(0xFF534341)
    private val md_theme_dark_onSurfaceVariant = Color(0xFFD8C2BF)
    private val md_theme_dark_outline = Color(0xFFA08C8A)
    private val md_theme_dark_inverseOnSurface = Color(0xFF201A19)
    private val md_theme_dark_inverseSurface = Color(0xFFEDE0DE)
    private val md_theme_dark_inversePrimary = Color(0xFFC0000D)
    private val md_theme_dark_surfaceTint = Color(0xFFFFB4AA)
    private val md_theme_dark_outlineVariant = Color(0xFF534341)
    private val md_theme_dark_scrim = Color(0xFF000000)

    override val LightColors = lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        primaryContainer = md_theme_light_primaryContainer,
        onPrimaryContainer = md_theme_light_onPrimaryContainer,
        secondary = md_theme_light_secondary,
        onSecondary = md_theme_light_onSecondary,
        secondaryContainer = md_theme_light_secondaryContainer,
        onSecondaryContainer = md_theme_light_onSecondaryContainer,
        tertiary = md_theme_light_tertiary,
        onTertiary = md_theme_light_onTertiary,
        tertiaryContainer = md_theme_light_tertiaryContainer,
        onTertiaryContainer = md_theme_light_onTertiaryContainer,
        error = md_theme_light_error,
        errorContainer = md_theme_light_errorContainer,
        onError = md_theme_light_onError,
        onErrorContainer = md_theme_light_onErrorContainer,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_onSurface,
        surfaceVariant = md_theme_light_surfaceVariant,
        onSurfaceVariant = md_theme_light_onSurfaceVariant,
        outline = md_theme_light_outline,
        inverseOnSurface = md_theme_light_inverseOnSurface,
        inverseSurface = md_theme_light_inverseSurface,
        inversePrimary = md_theme_light_inversePrimary,
        surfaceTint = md_theme_light_surfaceTint,
        outlineVariant = md_theme_light_outlineVariant,
        scrim = md_theme_light_scrim,
    )


    override val DarkColors = darkColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_onSecondary,
        secondaryContainer = md_theme_dark_secondaryContainer,
        onSecondaryContainer = md_theme_dark_onSecondaryContainer,
        tertiary = md_theme_dark_tertiary,
        onTertiary = md_theme_dark_onTertiary,
        tertiaryContainer = md_theme_dark_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_onTertiaryContainer,
        error = md_theme_dark_error,
        errorContainer = md_theme_dark_errorContainer,
        onError = md_theme_dark_onError,
        onErrorContainer = md_theme_dark_onErrorContainer,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_onSurface,
        surfaceVariant = md_theme_dark_surfaceVariant,
        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
        outline = md_theme_dark_outline,
        inverseOnSurface = md_theme_dark_inverseOnSurface,
        inverseSurface = md_theme_dark_inverseSurface,
        inversePrimary = md_theme_dark_inversePrimary,
        surfaceTint = md_theme_dark_surfaceTint,
        outlineVariant = md_theme_dark_outlineVariant,
        scrim = md_theme_dark_scrim,
    )
}