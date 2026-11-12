package com.example.ioscalculator.converter

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

data class Unit(
    val id: String,
    val name: String,
    val symbol: String,
    val toBase: Double,  // multiplier to base unit
    val fromBase: Double // multiplier from base unit
) {
    fun convert(value: Double, target: Unit): Double {
        return value * toBase / target.toBase
    }
}

data class Category(
    val id: String,
    val name: String,
    val units: List<Unit>,
    val baseUnit: String
)

object ConverterEngine {

    private val MC = MathContext(15, RoundingMode.HALF_EVEN)

    val categories = listOf(
        Category(
            id = "length",
            name = "Length",
            baseUnit = "meter",
            units = listOf(
                Unit("meter", "Meter", "m", 1.0, 1.0),
                Unit("kilometer", "Kilometer", "km", 1000.0, 0.001),
                Unit("centimeter", "Centimeter", "cm", 0.01, 100.0),
                Unit("millimeter", "Millimeter", "mm", 0.001, 1000.0),
                Unit("micrometer", "Micrometer", "µm", 1e-6, 1e6),
                Unit("mile", "Mile", "mi", 1609.344, 1/1609.344),
                Unit("yard", "Yard", "yd", 0.9144, 1/0.9144),
                Unit("foot", "Foot", "ft", 0.3048, 1/0.3048),
                Unit("inch", "Inch", "in", 0.0254, 1/0.0254),
                Unit("nautical_mile", "Nautical Mile", "nmi", 1852.0, 1/1852.0),
            )
        ),
        Category(
            id = "area",
            name = "Area",
            baseUnit = "square_meter",
            units = listOf(
                Unit("square_meter", "Square Meter", "m²", 1.0, 1.0),
                Unit("square_kilometer", "Square Kilometer", "km²", 1e6, 1e-6),
                Unit("square_centimeter", "Square Centimeter", "cm²", 1e-4, 1e4),
                Unit("square_millimeter", "Square Millimeter", "mm²", 1e-6, 1e6),
                Unit("hectare", "Hectare", "ha", 10000.0, 1e-4),
                Unit("acre", "Acre", "ac", 4046.86, 1/4046.86),
                Unit("square_mile", "Square Mile", "mi²", 2.59e6, 1/2.59e6),
                Unit("square_yard", "Square Yard", "yd²", 0.836127, 1/0.836127),
                Unit("square_foot", "Square Foot", "ft²", 0.092903, 1/0.092903),
                Unit("square_inch", "Square Inch", "in²", 0.00064516, 1/0.00064516),
            )
        ),
        Category(
            id = "volume",
            name = "Volume",
            baseUnit = "liter",
            units = listOf(
                Unit("liter", "Liter", "L", 1.0, 1.0),
                Unit("milliliter", "Milliliter", "mL", 0.001, 1000.0),
                Unit("cubic_meter", "Cubic Meter", "m³", 1000.0, 0.001),
                Unit("cubic_centimeter", "Cubic Centimeter", "cm³", 0.001, 1000.0),
                Unit("gallon_us", "US Gallon", "gal", 3.78541, 1/3.78541),
                Unit("gallon_uk", "UK Gallon", "gal", 4.54609, 1/4.54609),
                Unit("quart_us", "US Quart", "qt", 0.946353, 1/0.946353),
                Unit("pint_us", "US Pint", "pt", 0.473176, 1/0.473176),
                Unit("cup_us", "US Cup", "cup", 0.236588, 1/0.236588),
                Unit("fluid_ounce_us", "US Fl Oz", "fl oz", 0.0295735, 1/0.0295735),
                Unit("cubic_foot", "Cubic Foot", "ft³", 28.3168, 1/28.3168),
                Unit("cubic_inch", "Cubic Inch", "in³", 0.0163871, 1/0.0163871),
            )
        ),
        Category(
            id = "weight",
            name = "Weight",
            baseUnit = "kilogram",
            units = listOf(
                Unit("kilogram", "Kilogram", "kg", 1.0, 1.0),
                Unit("gram", "Gram", "g", 0.001, 1000.0),
                Unit("milligram", "Milligram", "mg", 1e-6, 1e6),
                Unit("metric_ton", "Metric Ton", "t", 1000.0, 0.001),
                Unit("pound", "Pound", "lb", 0.453592, 1/0.453592),
                Unit("ounce", "Ounce", "oz", 0.0283495, 1/0.0283495),
                Unit("stone", "Stone", "st", 6.35029, 1/6.35029),
                Unit("short_ton", "Short Ton", "ton", 907.185, 1/907.185),
                Unit("long_ton", "Long Ton", "lt", 1016.05, 1/1016.05),
            )
        ),
        Category(
            id = "temperature",
            name = "Temperature",
            baseUnit = "celsius",
            units = listOf(
                Unit("celsius", "Celsius", "°C", 1.0, 1.0),
                Unit("fahrenheit", "Fahrenheit", "°F", 1.0, 1.0), // special handling
                Unit("kelvin", "Kelvin", "K", 1.0, 1.0), // special handling
                Unit("rankine", "Rankine", "°R", 1.0, 1.0), // special handling
                Unit("reaumur", "Réaumur", "°Ré", 1.0, 1.0), // special handling
            )
        ),
        Category(
            id = "time",
            name = "Time",
            baseUnit = "second",
            units = listOf(
                Unit("second", "Second", "s", 1.0, 1.0),
                Unit("millisecond", "Millisecond", "ms", 0.001, 1000.0),
                Unit("minute", "Minute", "min", 60.0, 1/60.0),
                Unit("hour", "Hour", "h", 3600.0, 1/3600.0),
                Unit("day", "Day", "day", 86400.0, 1/86400.0),
                Unit("week", "Week", "wk", 604800.0, 1/604800.0),
            )
        )
    )

    fun getCategory(id: String): Category? = categories.find { it.id == id }

    fun convert(value: Double, from: Unit, to: Unit, category: Category): Double {
        return when (category.id) {
            "temperature" -> convertTemperature(value, from, to)
            else -> value * from.toBase / to.toBase
        }
    }

    private fun convertTemperature(value: Double, from: Unit, to: Unit): Double {
        // Convert to Celsius first
        val celsius = when (from.id) {
            "celsius" -> value
            "fahrenheit" -> (value - 32) * 5/9
            "kelvin" -> value - 273.15
            "rankine" -> (value - 491.67) * 5/9
            "reaumur" -> value * 5/4
            else -> value
        }
        // Convert from Celsius to target
        return when (to.id) {
            "celsius" -> celsius
            "fahrenheit" -> celsius * 9/5 + 32
            "kelvin" -> celsius + 273.15
            "rankine" -> (celsius + 273.15) * 9/5
            "reaumur" -> celsius * 4/5
            else -> celsius
        }
    }

    fun formatResult(value: Double): String {
        if (value.isInfinite() || value.isNaN()) return "Error"
        val bd = BigDecimal(value, MC)
        val stripped = bd.stripTrailingZeros()
        val plain = stripped.toPlainString()
        return if (plain.length > 15) stripped.toEngineeringString() else plain
    }
}