package com.example.ioscalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * iOS-style calculator button - circular, with press feedback
 */
@Composable
fun CalculatorButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    buttonType: ButtonType = ButtonType.NUMBER,
    enabled: Boolean = true,
    fontSize: Int = 28
) {
    val colors = MaterialTheme.colorScheme
    val (backgroundColor, contentColor, pressedColor) = when (buttonType) {
        ButtonType.NUMBER -> colors.secondary to colors.onSecondary to colors.secondaryContainer
        ButtonType.OPERATOR -> colors.primary to colors.onPrimary to colors.primaryContainer
        ButtonType.FUNCTION -> colors.tertiary to colors.onTertiary to colors.tertiaryContainer
        ButtonType.CLEAR -> colors.tertiary to colors.onTertiary to colors.tertiaryContainer
    }

    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f))
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
        propagateMinConstraints = false
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Medium,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f)
        )
    }
}

enum class ButtonType {
    NUMBER,      // Dark gray: 0-9, .
    OPERATOR,    // Orange: + - × ÷ = %
    FUNCTION,    // Light gray: C +/− % sin cos etc
    CLEAR        // Light gray: AC
}