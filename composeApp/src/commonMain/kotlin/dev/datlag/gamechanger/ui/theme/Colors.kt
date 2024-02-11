package dev.datlag.gamechanger.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

data object Colors {

    private const val THEME_LIGHT_PRIMARY = 0xFF146D33
    private const val THEME_LIGHT_ON_PRIMARY = 0xFFFFFFFF
    private const val THEME_LIGHT_PRIMARY_CONTAINER = 0xFFA1F6AD
    private const val THEME_LIGHT_ON_PRIMARY_CONTAINER = 0xFF00210A

    private const val THEME_DARK_PRIMARY = 0xFF85D993
    private const val THEME_DARK_ON_PRIMARY = 0xFF003916
    private const val THEME_DARK_PRIMARY_CONTAINER = 0xFF005322
    private const val THEME_DARK_ON_PRIMARY_CONTAINER = 0xFFA1F6AD

    private const val THEME_LIGHT_SECONDARY = 0xFF516351
    private const val THEME_LIGHT_ON_SECONDARY = 0xFFFFFFFF
    private const val THEME_LIGHT_SECONDARY_CONTAINER = 0xFFD3E8D1
    private const val THEME_LIGHT_ON_SECONDARY_CONTAINER = 0xFF0F1F11

    private const val THEME_DARK_SECONDARY = 0xFFB8CCB6
    private const val THEME_DARK_ON_SECONDARY = 0xFF233425
    private const val THEME_DARK_SECONDARY_CONTAINER = 0xFF394B3A
    private const val THEME_DARK_ON_SECONDARY_CONTAINER = 0xFFD3E8D1

    private const val THEME_LIGHT_TERTIARY = 0xFF39656D
    private const val THEME_LIGHT_ON_TERTIARY = 0xFFFFFFFF
    private const val THEME_LIGHT_TERTIARY_CONTAINER = 0xFFBDEAF4
    private const val THEME_LIGHT_ON_TERTIARY_CONTAINER = 0xFF001F24

    private const val THEME_DARK_TERTIARY = 0xFFA1CED7
    private const val THEME_DARK_ON_TERTIARY = 0xFF00363E
    private const val THEME_DARK_TERTIARY_CONTAINER = 0xFF1F4D55
    private const val THEME_DARK_ON_TERTIARY_CONTAINER = 0xFFBDEAF4

    private const val THEME_LIGHT_ERROR = 0xFFBA1A1A
    private const val THEME_LIGHT_ON_ERROR = 0xFFFFFFFF
    private const val THEME_LIGHT_ERROR_CONTAINER = 0xFFFFDAD6
    private const val THEME_LIGHT_ON_ERROR_CONTAINER = 0xFF410002

    private const val THEME_DARK_ERROR = 0xFFFFB4AB
    private const val THEME_DARK_ON_ERROR = 0xFF93000A
    private const val THEME_DARK_ERROR_CONTAINER = 0xFF690005
    private const val THEME_DARK_ON_ERROR_CONTAINER = 0xFFFFDAD6

    private const val THEME_LIGHT_BACKGROUND = 0xFFFCFDF7
    private const val THEME_LIGHT_ON_BACKGROUND = 0xFF1A1C19

    private const val THEME_DARK_BACKGROUND = 0xFF1A1C19
    private const val THEME_DARK_ON_BACKGROUND = 0xFFE2E3DD

    private const val THEME_LIGHT_SURFACE = 0xFFFCFDF7
    private const val THEME_LIGHT_ON_SURFACE = 0xFF1A1C19
    private const val THEME_LIGHT_SURFACE_VARIANT = 0xFFDDE5DA
    private const val THEME_LIGHT_ON_SURFACE_VARIANT = 0xFF414941

    private const val THEME_DARK_SURFACE = 0xFF1A1C19
    private const val THEME_DARK_ON_SURFACE = 0xFFE2E3DD
    private const val THEME_DARK_SURFACE_VARIANT = 0xFF414941
    private const val THEME_DARK_ON_SURFACE_VARIANT = 0xFFC1C9BE

    private const val THEME_LIGHT_OUTLINE = 0xFF727970
    private const val THEME_LIGHT_INVERSE_SURFACE = 0xFF2E312E
    private const val THEME_LIGHT_INVERSE_ON_SURFACE = 0xFFF0F1EC
    private const val THEME_LIGHT_INVERSE_PRIMARY = 0xFF85D993

    private const val THEME_DARK_OUTLINE = 0xFF8B9389
    private const val THEME_DARK_INVERSE_SURFACE = 0xFFE2E3DD
    private const val THEME_DARK_INVERSE_ON_SURFACE = 0xFF1A1C19
    private const val THEME_DARK_INVERSE_PRIMARY = 0xFF146D33

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