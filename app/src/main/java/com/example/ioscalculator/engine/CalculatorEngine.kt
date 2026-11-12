package com.example.ioscalculator.engine

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.*

/**
 * Core calculator engine - extracted and rewritten from TranssionCalculator smali analysis.
 * Implements: expression parsing, operator precedence, scientific functions, memory, error handling.
 */
class CalculatorEngine {

    companion object {
        const val MAX_DIGITS = 15
        const val MAX_HISTORY = 100
        private const val MC = MathContext(MAX_DIGITS + 5, RoundingMode.HALF_EVEN)
    }

    // State
    private var expression = ""
    private var currentInput = "0"
    private var lastResult: BigDecimal? = null
    private var pendingOperator: Operator? = null
    private var pendingOperand: BigDecimal? = null
    private var isNewInput = true
    private var angleMode: AngleMode = AngleMode.DEG
    private var inverseMode = false
    private var memory: BigDecimal = BigDecimal.ZERO
    private var hasError = false

    // History callback
    var onHistoryUpdate: ((String, String) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    enum class AngleMode { DEG, RAD }
    enum class Operator(val precedence: Int, val symbol: String) {
        ADD(1, "+"), SUBTRACT(1, "−"), MULTIPLY(2, "×"), DIVIDE(2, "÷"),
        POWER(3, "^"), FACTORIAL(4, "!"), PERCENT(2, "%")
    }

    // ========== Public API ==========

    fun getDisplay(): String = if (hasError) "Error" else currentInput

    fun getExpression(): String = expression

    fun getAngleMode(): AngleMode = angleMode

    fun isInverseMode(): Boolean = inverseMode

    fun hasMemory(): Boolean = memory != BigDecimal.ZERO

    fun toggleAngleMode() {
        angleMode = if (angleMode == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
    }

    fun toggleInverseMode() {
        inverseMode = !inverseMode
    }

    fun clearAll() {
        expression = ""
        currentInput = "0"
        lastResult = null
        pendingOperator = null
        pendingOperand = null
        isNewInput = true
        hasError = false
    }

    fun clearEntry() {
        currentInput = "0"
        isNewInput = true
        hasError = false
    }

    fun backspace() {
        if (hasError) { clearAll(); return }
        if (isNewInput || currentInput == "0") return
        if (currentInput.length == 1 || (currentInput.length == 2 && currentInput.startsWith("-"))) {
            currentInput = "0"
        } else {
            currentInput = currentInput.dropLast(1)
        }
    }

    fun inputDigit(digit: Char) {
        if (hasError) clearAll()
        if (isNewInput) {
            currentInput = digit.toString()
            isNewInput = false
        } else if (currentInput.length < MAX_DIGITS) {
            if (currentInput == "0") currentInput = digit.toString()
            else currentInput += digit
        }
    }

    fun inputDecimalPoint() {
        if (hasError) clearAll()
        if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }
    }

    fun inputOperator(op: Operator) {
        if (hasError) return
        val value = parseCurrentInput()
        
        if (pendingOperator != null && !isNewInput) {
            calculatePending(value)
        } else {
            pendingOperand = value
            lastResult = value
        }
        
        pendingOperator = op
        expression = formatForDisplay(pendingOperand!!) + " " + op.symbol + " "
        isNewInput = true
    }

    fun calculate(): String? {
        if (hasError || pendingOperator == null) return currentInput
        val value = parseCurrentInput()
        calculatePending(value)
        pendingOperator = null
        pendingOperand = null
        expression = ""
        isNewInput = true
        return currentInput
    }

    fun calculatePercent() {
        if (hasError) return
        val value = parseCurrentInput() / 100
        currentInput = formatResult(value)
        isNewInput = true
    }

    fun toggleSign() {
        if (hasError || currentInput == "0") return
        if (currentInput.startsWith("-")) currentInput = currentInput.substring(1)
        else currentInput = "-$currentInput"
    }

    // Scientific functions
    fun applyFunction(fn: Function): String? {
        if (hasError) return null
        val value = parseCurrentInput()
        val result = when (fn) {
            Function.SIN -> sin(value)
            Function.COS -> cos(value)
            Function.TAN -> tan(value)
            Function.ASIN -> asin(value)
            Function.ACOS -> acos(value)
            Function.ATAN -> atan(value)
            Function.LN -> ln(value)
            Function.LOG -> log10(value)
            Function.EXP -> exp(value)
            Function.EXP10 -> exp10(value)
            Function.SQRT -> sqrt(value)
            Function.SQUARE -> value * value
            Function.RECIPROCAL -> reciprocal(value)
            Function.FACTORIAL -> factorial(value)
            Function.PI -> BigDecimal.PI
            Function.E -> BigDecimal(Math.E.toString())
        }
        currentInput = formatResult(result)
        lastResult = result
        isNewInput = true
        return currentInput
    }

    // Memory operations
    fun memoryClear() { memory = BigDecimal.ZERO }
    fun memoryRecall(): String { currentInput = formatResult(memory); isNewInput = true; return currentInput }
    fun memoryAdd() { memory += parseCurrentInput() }
    fun memorySubtract() { memory -= parseCurrentInput() }

    // ========== Internal Calculation ==========

    private fun calculatePending(value: BigDecimal) {
        val left = pendingOperand ?: value
        val right = value
        val result = try {
            when (pendingOperator) {
                Operator.ADD -> left + right
                Operator.SUBTRACT -> left - right
                Operator.MULTIPLY -> left * right
                Operator.DIVIDE -> if (right == BigDecimal.ZERO) throw ArithmeticException() else left.divide(right, MC)
                Operator.POWER -> pow(left, right)
                Operator.PERCENT -> left * right / 100
                null -> right
            }
        } catch (e: Exception) {
            handleError()
            return
        }
        pendingOperand = result
        lastResult = result
        currentInput = formatResult(result)
    }

    private fun parseCurrentInput(): BigDecimal {
        return try {
            BigDecimal(currentInput)
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }
    }

    // ========== Scientific Functions ==========

    private fun toRadians(d: BigDecimal): Double {
        val deg = d.toDouble()
        return if (angleMode == AngleMode.DEG) Math.toRadians(deg) else deg
    }

    private fun fromRadians(rad: Double): BigDecimal {
        return if (angleMode == AngleMode.DEG) BigDecimal(Math.toDegrees(rad)) else BigDecimal(rad)
    }

    private fun sin(d: BigDecimal): BigDecimal = fromRadians(kotlin.math.sin(toRadians(d)))
    private fun cos(d: BigDecimal): BigDecimal = fromRadians(kotlin.math.cos(toRadians(d)))
    private fun tan(d: BigDecimal): BigDecimal {
        val rad = toRadians(d)
        val cosVal = kotlin.math.cos(rad)
        if (abs(cosVal) < 1e-15) throw ArithmeticException()
        return fromRadians(kotlin.math.tan(rad))
    }
    private fun asin(d: BigDecimal): BigDecimal {
        val v = d.toDouble()
        if (v < -1.0 || v > 1.0) throw ArithmeticException()
        return fromRadians(kotlin.math.asin(v))
    }
    private fun acos(d: BigDecimal): BigDecimal {
        val v = d.toDouble()
        if (v < -1.0 || v > 1.0) throw ArithmeticException()
        return fromRadians(kotlin.math.acos(v))
    }
    private fun atan(d: BigDecimal): BigDecimal = fromRadians(kotlin.math.atan(d.toDouble()))
    private fun ln(d: BigDecimal): BigDecimal {
        if (d <= BigDecimal.ZERO) throw ArithmeticException()
        return BigDecimal(kotlin.math.log(d.toDouble()))
    }
    private fun log10(d: BigDecimal): BigDecimal {
        if (d <= BigDecimal.ZERO) throw ArithmeticException()
        return BigDecimal(kotlin.math.log10(d.toDouble()))
    }
    private fun exp(d: BigDecimal): BigDecimal = BigDecimal(Math.exp(d.toDouble()))
    private fun exp10(d: BigDecimal): BigDecimal = BigDecimal(Math.pow(10.0, d.toDouble()))
    private fun sqrt(d: BigDecimal): BigDecimal {
        if (d < BigDecimal.ZERO) throw ArithmeticException()
        return BigDecimal(kotlin.math.sqrt(d.toDouble()))
    }
    private fun reciprocal(d: BigDecimal): BigDecimal {
        if (d == BigDecimal.ZERO) throw ArithmeticException()
        return BigDecimal.ONE.divide(d, MC)
    }
    private fun factorial(d: BigDecimal): BigDecimal {
        val n = d.toLong()
        if (n < 0 || n != d.toDouble() || n > 20) throw ArithmeticException()
        var result = BigDecimal.ONE
        for (i in 2..n) result *= i
        return result
    }
    private fun pow(base: BigDecimal, exp: BigDecimal): BigDecimal {
        return BigDecimal(Math.pow(base.toDouble(), exp.toDouble()))
    }

    // ========== Formatting ==========

    private fun formatResult(value: BigDecimal): String {
        try {
            val stripped = value.stripTrailingZeros()
            val plain = stripped.toPlainString()
            if (plain.length > MAX_DIGITS) {
                return stripped.toEngineeringString()
            }
            return plain
        } catch (e: Exception) {
            return value.toPlainString()
        }
    }

    private fun formatForDisplay(value: BigDecimal): String = formatResult(value)

    private fun handleError() {
        hasError = true
        currentInput = "Error"
        onError?.invoke("Calculation error")
    }

    // ========== History ==========

    fun addToHistory(expr: String, result: String) {
        onHistoryUpdate?.invoke(expr, result)
    }

    enum class Function {
        SIN, COS, TAN, ASIN, ACOS, ATAN,
        LN, LOG, EXP, EXP10, SQRT, SQUARE,
        RECIPROCAL, FACTORIAL, PI, E
    }
}

/**
 * BigDecimal extension for PI constant
 */
val BigDecimal.PI: BigDecimal
    get() = BigDecimal(Math.PI.toString())