package com.example.ioscalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ioscalculator.R
import com.example.ioscalculator.engine.CalculatorEngine
import com.example.ioscalculator.ui.components.CalculatorButton
import com.example.ioscalculator.ui.components.ButtonType
import com.example.ioscalculator.ui.theme.iOSCalculatorTheme

@Composable
fun CalculatorScreen(
    engine: CalculatorEngine = remember { CalculatorEngine() },
    onNavigateToConverter: () -> Unit = {},
    onNavigateToCurrency: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    var display by remember { mutableStateOf(engine.getDisplay()) }
    var formula by remember { mutableStateOf(engine.getExpression()) }
    var angleMode by remember { mutableStateOf(engine.getAngleMode()) }
    var inverseMode by remember { mutableStateOf(engine.isInverseMode()) }
    var showScientific by remember { mutableStateOf(false) }

    val updateDisplay = {
        display = engine.getDisplay()
        formula = engine.getExpression()
        angleMode = engine.getAngleMode()
        inverseMode = engine.isInverseMode()
    }

    val handleClick = { action: () -> Unit ->
        action()
        updateDisplay()
    }

    iOSCalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Top bar
                TopBar(
                    angleMode = angleMode,
                    inverseMode = inverseMode,
                    onAngleModeToggle = { handleClick { engine.toggleAngleMode() } },
                    onInverseToggle = { handleClick { engine.toggleInverseMode() } },
                    onScientificToggle = { showScientific = !showScientific },
                    showScientific = showScientific,
                    onHistoryClick = onNavigateToHistory,
                    onSettingsClick = onNavigateToSettings
                )

                // Display area
                DisplayArea(display = display, formula = formula)

                // Scientific row (when expanded)
                if (showScientific) {
                    ScientificRow(
                        inverseMode = inverseMode,
                        onFunctionClick = { fn -> handleClick { engine.applyFunction(fn) } }
                    )
                }

                // Main button grid
                ButtonGrid(
                    showScientific = showScientific,
                    onButtonClick = { btn ->
                        when (btn) {
                            is Digit -> handleClick { engine.inputDigit(btn.digit) }
                            is OperatorBtn -> handleClick { engine.inputOperator(btn.operator) }
                            is FunctionBtn -> handleClick { engine.applyFunction(btn.function) }
                            is ActionBtn -> handleClick { btn.action() }
                        }
                    }
                )

                // Mode selector
                ModeSelector(
                    onBasicClick = { onNavigateToCurrency() },
                    onScientificClick = { onNavigateToCurrency() },
                    onConverterClick = onNavigateToConverter,
                    onCurrencyClick = onNavigateToCurrency
                )
            }
        }
    }
}

@Composable
fun TopBar(
    angleMode: CalculatorEngine.AngleMode,
    inverseMode: Boolean,
    onAngleModeToggle: () -> Unit,
    onInverseToggle: () -> Unit,
    onScientificToggle: () -> Unit,
    showScientific: Boolean,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        // Angle mode
        Text(
            text = angleMode.name,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Inverse indicator
        if (inverseMode) {
            Text(
                text = "INV",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // History & Settings icons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = onHistoryClick) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DisplayArea(display: String, formula: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (formula.isNotBlank()) {
                Text(
                    text = formula,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            Text(
                text = display,
                fontSize = 96.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ScientificRow(
    inverseMode: Boolean,
    onFunctionClick: (CalculatorEngine.Function) -> Unit
) {
    val functions = if (inverseMode) {
        listOf(
            CalculatorEngine.Function.ASIN, CalculatorEngine.Function.ACOS, CalculatorEngine.Function.ATAN,
            CalculatorEngine.Function.EXP, CalculatorEngine.Function.EXP10, CalculatorEngine.Function.SQUARE,
            CalculatorEngine.Function.RECIPROCAL, CalculatorEngine.Function.PI, CalculatorEngine.Function.E
        )
    } else {
        listOf(
            CalculatorEngine.Function.SIN, CalculatorEngine.Function.COS, CalculatorEngine.Function.TAN,
            CalculatorEngine.Function.LN, CalculatorEngine.Function.LOG, CalculatorEngine.Function.SQRT,
            CalculatorEngine.Function.FACTORIAL, CalculatorEngine.Function.PI, CalculatorEngine.Function.E
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        functions.forEach { fn ->
            CalculatorButton(
                modifier = Modifier.weight(1f),
                text = fn.symbol(inverseMode),
                onClick = { onFunctionClick(fn) },
                buttonType = ButtonType.FUNCTION,
                fontSize = 16
            )
        }
    }
}

@Composable
fun ButtonGrid(
    showScientific: Boolean,
    onButtonClick: (Any) -> Unit
) {
    // Main 4x5 grid (numbers + operators)
    val rows = listOf(
        listOf(
            ActionBtn("C", ButtonType.CLEAR, { it.clearAll() }),
            ActionBtn("+/-", ButtonType.FUNCTION, { it.toggleSign() }),
            ActionBtn("%", ButtonType.FUNCTION, { it.calculatePercent() }),
            OperatorBtn(CalculatorEngine.Operator.DIVIDE)
        ),
        listOf(
            Digit('7'), Digit('8'), Digit('9'),
            OperatorBtn(CalculatorEngine.Operator.MULTIPLY)
        ),
        listOf(
            Digit('4'), Digit('5'), Digit('6'),
            OperatorBtn(CalculatorEngine.Operator.SUBTRACT)
        ),
        listOf(
            Digit('1'), Digit('2'), Digit('3'),
            OperatorBtn(CalculatorEngine.Operator.ADD)
        ),
        listOf(
            ActionBtn("0", ButtonType.NUMBER, { it.inputDigit('0') }, wide = true),
            Digit('.'),
            ActionBtn("=", ButtonType.OPERATOR, { it.calculate() })
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { btn ->
                    when (btn) {
                        is Digit -> CalculatorButton(
                            modifier = Modifier.weight(1f),
                            text = btn.digit.toString(),
                            onClick = { onButtonClick(btn) },
                            buttonType = ButtonType.NUMBER
                        )
                        is OperatorBtn -> CalculatorButton(
                            modifier = Modifier.weight(1f),
                            text = btn.operator.symbol,
                            onClick = { onButtonClick(btn) },
                            buttonType = ButtonType.OPERATOR
                        )
                        is ActionBtn -> CalculatorButton(
                            modifier = Modifier
                                .weight(if (btn.wide) 2f else 1f)
                                .fillMaxWidth(),
                            text = btn.label,
                            onClick = { onButtonClick(btn) },
                            buttonType = btn.type
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModeSelector(
    onBasicClick: () -> Unit,
    onScientificClick: () -> Unit,
    onConverterClick: () -> Unit,
    onCurrencyClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        listOf(
            "Basic" to onBasicClick,
            "Scientific" to onScientificClick,
            "Converter" to onConverterClick,
            "Currency" to onCurrencyClick
        ).forEach { (label, action) ->
            androidx.compose.material3.Button(
                onClick = action,
                modifier = Modifier.weight(1f).height(40.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// Data classes for button grid
sealed class GridButton
data class Digit(val digit: Char) : GridButton()
data class OperatorBtn(val operator: CalculatorEngine.Operator) : GridButton()
data class FunctionBtn(val function: CalculatorEngine.Function) : GridButton()
data class ActionBtn(
    val label: String,
    val type: ButtonType,
    val action: (CalculatorEngine) -> Unit,
    val wide: Boolean = false
) : GridButton()

// Extensions
fun CalculatorEngine.Function.symbol(inverse: Boolean): String = when (this) {
    SIN -> if (inverse) "sin⁻¹" else "sin"
    COS -> if (inverse) "cos⁻¹" else "cos"
    TAN -> if (inverse) "tan⁻¹" else "tan"
    ASIN -> "sin⁻¹"
    ACOS -> "cos⁻¹"
    ATAN -> "tan⁻¹"
    LN -> "ln"
    LOG -> "log"
    EXP -> "eˣ"
    EXP10 -> "10ˣ"
    SQRT -> "√"
    SQUARE -> "x²"
    RECIPROCAL -> "1/x"
    FACTORIAL -> "x!"
    PI -> "π"
    E -> "e"
}

// Icons
object Icons {
    object Default {
        val History = androidx.compose.material.icons.filled.History
        val Settings = androidx.compose.material.icons.filled.Settings
        val SwapVert = androidx.compose.material.icons.filled.SwapVert
        val ExpandMore = androidx.compose.material.icons.filled.ExpandMore
        val Refresh = androidx.compose.material.icons.filled.Refresh
        val ChevronRight = androidx.compose.material.icons.filled.ChevronRight
    }
}