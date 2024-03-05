package dev.datlag.pulz.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

data object Colors {

    private const val THEME_LIGHT_PRIMARY = 0xFF00658d
    private const val THEME_LIGHT_ON_PRIMARY = 0xFFffffff
    private const val THEME_LIGHT_PRIMARY_CONTAINER = 0xFFc6e7ff
    private const val THEME_LIGHT_ON_PRIMARY_CONTAINER = 0xFF001e2d

    private const val THEME_DARK_PRIMARY = 0xFF82cfff
    private const val THEME_DARK_ON_PRIMARY = 0xFF00344b
    private const val THEME_DARK_PRIMARY_CONTAINER = 0xFF004c6b
    private const val THEME_DARK_ON_PRIMARY_CONTAINER = 0xFFc6e7ff

    private const val THEME_LIGHT_SECONDARY = 0xFF4f616e
    private const val THEME_LIGHT_ON_SECONDARY = 0xFFffffff
    private const val THEME_LIGHT_SECONDARY_CONTAINER = 0xFFd2e5f4
    private const val THEME_LIGHT_ON_SECONDARY_CONTAINER = 0xFF0b1d28

    private const val THEME_DARK_SECONDARY = 0xFFb6c9d8
    private const val THEME_DARK_ON_SECONDARY = 0xFF21323e
    private const val THEME_DARK_SECONDARY_CONTAINER = 0xFF374955
    private const val THEME_DARK_ON_SECONDARY_CONTAINER = 0xFFd2e5f4

    private const val THEME_LIGHT_TERTIARY = 0xFF4d57a9
    private const val THEME_LIGHT_ON_TERTIARY = 0xFFffffff
    private const val THEME_LIGHT_TERTIARY_CONTAINER = 0xFFdfe0ff
    private const val THEME_LIGHT_ON_TERTIARY_CONTAINER = 0xFF000964

    private const val THEME_DARK_TERTIARY = 0xFFbdc2ff
    private const val THEME_DARK_ON_TERTIARY = 0xFF1c2678
    private const val THEME_DARK_TERTIARY_CONTAINER = 0xFF353e90
    private const val THEME_DARK_ON_TERTIARY_CONTAINER = 0xFFdfe0ff

    private const val THEME_LIGHT_ERROR = 0xFFbb1723
    private const val THEME_LIGHT_ON_ERROR = 0xFFffffff
    private const val THEME_LIGHT_ERROR_CONTAINER = 0xFFffdad7
    private const val THEME_LIGHT_ON_ERROR_CONTAINER = 0xFF410004

    private const val THEME_DARK_ERROR = 0xFFffb3ae
    private const val THEME_DARK_ON_ERROR = 0xFF68000b
    private const val THEME_DARK_ERROR_CONTAINER = 0xFF930014
    private const val THEME_DARK_ON_ERROR_CONTAINER = 0xFFffdad7

    private const val THEME_LIGHT_BACKGROUND = 0xFFfcfcff
    private const val THEME_LIGHT_ON_BACKGROUND = 0xFF191c1e

    private const val THEME_DARK_BACKGROUND = 0xFF191c1e
    private const val THEME_DARK_ON_BACKGROUND = 0xFFe2e2e5

    private const val THEME_LIGHT_SURFACE = 0xFFfcfcff
    private const val THEME_LIGHT_ON_SURFACE = 0xFF191c1e
    private const val THEME_LIGHT_SURFACE_VARIANT = 0xFFdde3ea
    private const val THEME_LIGHT_ON_SURFACE_VARIANT = 0xFF41484d

    private const val THEME_DARK_SURFACE = 0xFF191c1e
    private const val THEME_DARK_ON_SURFACE = 0xFFe2e2e5
    private const val THEME_DARK_SURFACE_VARIANT = 0xFF41484d
    private const val THEME_DARK_ON_SURFACE_VARIANT = 0xFFc1c7ce

    private const val THEME_LIGHT_OUTLINE = 0xFF71787e
    private const val THEME_LIGHT_INVERSE_SURFACE = 0xFF2e3133
    private const val THEME_LIGHT_INVERSE_ON_SURFACE = 0xFFf0f0f3
    private const val THEME_LIGHT_INVERSE_PRIMARY = 0xFF88ceff

    private const val THEME_DARK_OUTLINE = 0xFF8b9198
    private const val THEME_DARK_INVERSE_SURFACE = 0xFFe2e2e5
    private const val THEME_DARK_INVERSE_ON_SURFACE = 0xFF2e3133
    private const val THEME_DARK_INVERSE_PRIMARY = 0xFF006590

    fun getDarkScheme() = darkColorScheme(
        primary = Color(THEME_DARK_PRIMARY),
        onPrimary = Color(THEME_DARK_ON_PRIMARY),
        primaryContainer = Color(THEME_DARK_PRIMARY_CONTAINER),
        onPrimaryContainer = Color(THEME_DARK_ON_PRIMARY_CONTAINER),

        secondary = Color(THEME_DARK_SECONDARY),
        onSecondary = Color(THEME_DARK_ON_SECONDARY),
        secondaryContainer = Color(THEME_DARK_SECONDARY_CONTAINER),
        onSecondaryContainer = Color(THEME_DARK_ON_SECONDARY_CONTAINER),

        tertiary = Color(THEME_DARK_TERTIARY),
        onTertiary = Color(THEME_DARK_ON_TERTIARY),
        tertiaryContainer = Color(THEME_DARK_TERTIARY_CONTAINER),
        onTertiaryContainer = Color(THEME_DARK_ON_TERTIARY_CONTAINER),

        error = Color(THEME_DARK_ERROR),
        errorContainer = Color(THEME_DARK_ERROR_CONTAINER),
        onError = Color(THEME_DARK_ON_ERROR),
        onErrorContainer = Color(THEME_DARK_ON_ERROR_CONTAINER),

        background = Color(THEME_DARK_BACKGROUND),
        onBackground = Color(THEME_DARK_ON_BACKGROUND),

        surface = Color(THEME_DARK_SURFACE),
        onSurface = Color(THEME_DARK_ON_SURFACE),
        surfaceVariant = Color(THEME_DARK_SURFACE_VARIANT),
        onSurfaceVariant = Color(THEME_DARK_ON_SURFACE_VARIANT),

        outline = Color(THEME_DARK_OUTLINE),
        inverseSurface = Color(THEME_DARK_INVERSE_SURFACE),
        inverseOnSurface = Color(THEME_DARK_INVERSE_ON_SURFACE),
        inversePrimary = Color(THEME_DARK_INVERSE_PRIMARY)
    )

    fun getLightScheme() = lightColorScheme(
        primary = Color(THEME_LIGHT_PRIMARY),
        onPrimary = Color(THEME_LIGHT_ON_PRIMARY),
        primaryContainer = Color(THEME_LIGHT_PRIMARY_CONTAINER),
        onPrimaryContainer = Color(THEME_LIGHT_ON_PRIMARY_CONTAINER),

        secondary = Color(THEME_LIGHT_SECONDARY),
        onSecondary = Color(THEME_LIGHT_ON_SECONDARY),
        secondaryContainer = Color(THEME_LIGHT_SECONDARY_CONTAINER),
        onSecondaryContainer = Color(THEME_LIGHT_ON_SECONDARY_CONTAINER),

        tertiary = Color(THEME_LIGHT_TERTIARY),
        onTertiary = Color(THEME_LIGHT_ON_TERTIARY),
        tertiaryContainer = Color(THEME_LIGHT_TERTIARY_CONTAINER),
        onTertiaryContainer = Color(THEME_LIGHT_ON_TERTIARY_CONTAINER),

        error = Color(THEME_LIGHT_ERROR),
        errorContainer = Color(THEME_LIGHT_ERROR_CONTAINER),
        onError = Color(THEME_LIGHT_ON_ERROR),
        onErrorContainer = Color(THEME_LIGHT_ON_ERROR_CONTAINER),

        background = Color(THEME_LIGHT_BACKGROUND),
        onBackground = Color(THEME_LIGHT_ON_BACKGROUND),

        surface = Color(THEME_LIGHT_SURFACE),
        onSurface = Color(THEME_LIGHT_ON_SURFACE),
        surfaceVariant = Color(THEME_LIGHT_SURFACE_VARIANT),
        onSurfaceVariant = Color(THEME_LIGHT_ON_SURFACE_VARIANT),

        outline = Color(THEME_LIGHT_OUTLINE),
        inverseSurface = Color(THEME_LIGHT_INVERSE_SURFACE),
        inverseOnSurface = Color(THEME_LIGHT_INVERSE_ON_SURFACE),
        inversePrimary = Color(THEME_LIGHT_INVERSE_PRIMARY)
    )
}