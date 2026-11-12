package com.example.ioscalculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorTextStyles(typography: Typography) {
    // Large display for result (iOS style: 72-96pt)
    val displayStyle = TextStyle(
        fontFamily = typography.displayLarge.fontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-2.0).sp,
        lineHeight = 96.sp
    )
    
    // Formula display (smaller, above result)
    val formulaStyle = TextStyle(
        fontFamily = typography.bodyLarge.fontFamily,
        fontWeight = FontWeight.Regular,
        fontSize = 20.sp,
        letterSpacing = (-0.2).sp,
        color = typography.bodyLarge.color?.copy(alpha = 0.6f)
    )
    
    // Button text
    val buttonStyle = TextStyle(
        fontFamily = typography.labelLarge.fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        letterSpacing = (0.0).sp
    )
    
    // Function button text (smaller)
    val functionButtonStyle = TextStyle(
        fontFamily = typography.labelMedium.fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = (0.0).sp
    )
}