package com.example.ioscalculator.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkIOSColorScheme = darkColorScheme(
    primary = Color(0xFFFF9F0A),      // iOS Orange
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFCC7700),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF333333),    // Dark gray buttons
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF505050),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFFD4D4D4),     // Light gray buttons
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFEBEBF5),
    onTertiaryContainer = Color.Black,
    error = Color(0xFFFF6B6B),
    onError = Color.Black,
    errorContainer = Color(0xFF8B0000),
    onErrorContainer = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = Color(0xFFEBEBF599),
    outline = Color(0xFF505050),
    outlineVariant = Color(0xFF333333),
    shadow = Color.Black,
    scrim = Color.Black,
    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    inversePrimary = Color(0xFFFF9F0A),
    surfaceTint = Color(0xFFFF9F0A)
)

private val LightIOSColorScheme = lightColorScheme(
    primary = Color(0xFFFF9F0A),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFFFD699),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFFD4D4D4),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFEBEBF5),
    onSecondaryContainer = Color.Black,
    tertiary = Color(0xFF333333),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF505050),
    onTertiaryContainer = Color.White,
    error = Color(0xFFDC3545),
    onError = Color.White,
    errorContainer = Color(0xFFFFDADA),
    onErrorContainer = Color.Black,
    background = Color(0xFFF2F2F7),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEBEBF5),
    onSurfaceVariant = Color(0xFF333333),
    outline = Color(0xFF8E8E93),
    outlineVariant = Color(0xFFD4D4D4),
    shadow = Color.Black,
    scrim = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    inversePrimary = Color(0xFFFF9F0A),
    surfaceTint = Color(0xFFFF9F0A)
)

@Composable
fun AppColorScheme(
    isSystemInDarkTheme: Boolean,
    themeMode: Int = 0 // 0=system, 1=light, 2=dark
) = when (themeMode) {
    1 -> LightIOSColorScheme
    2 -> DarkIOSColorScheme
    else -> if (isSystemInDarkTheme) DarkIOSColorScheme else LightIOSColorScheme
}