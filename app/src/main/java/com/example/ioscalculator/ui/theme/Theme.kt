package com.example.ioscalculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.SystemFonts

private val InterFontFamily = FontFamily(
    Font(SystemFonts.SansSerif, FontWeight.Normal),
    Font(SystemFonts.SansSerif, FontWeight.Medium),
    Font(SystemFonts.SansSerif, FontWeight.SemiBold),
    Font(SystemFonts.SansSerif, FontWeight.Bold)
)

private val IOSTypography = Typography(
    displayLarge = Typography.default.displayLarge.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Bold),
    displayMedium = Typography.default.displayMedium.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Bold),
    displaySmall = Typography.default.displaySmall.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold),
    headlineLarge = Typography.default.headlineLarge.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold),
    headlineMedium = Typography.default.headlineMedium.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = Typography.default.headlineSmall.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
    titleLarge = Typography.default.titleLarge.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
    titleMedium = Typography.default.titleMedium.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
    titleSmall = Typography.default.titleSmall.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
    bodyLarge = Typography.default.bodyLarge.copy(fontFamily = InterFontFamily),
    bodyMedium = Typography.default.bodyMedium.copy(fontFamily = InterFontFamily),
    bodySmall = Typography.default.bodySmall.copy(fontFamily = InterFontFamily),
    labelLarge = Typography.default.labelLarge.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
    labelMedium = Typography.default.labelMedium.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
    labelSmall = Typography.default.labelSmall.copy(fontFamily = InterFontFamily, fontWeight = FontWeight.Medium),
)

@Composable
fun iOSCalculatorTheme(
    darkTheme: Boolean = false,
    themeMode: Int = 0,
    content: @Composable () -> Unit
) {
    val colorScheme = AppColorScheme(darkTheme, themeMode)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = IOSTypography,
        content = content
    )
}