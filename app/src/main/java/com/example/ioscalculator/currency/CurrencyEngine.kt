package com.example.ioscalculator.currency

import kotlinx.serialization.Serializable
import kotlin.math.roundToLong

@Serializable
data class CurrencyRate(
    val code: String,
    val name: String,
    val country: String,
    val rate: Double = 1.0  // relative to base (USD)
)

@Serializable
data class CurrencyResponse(
    val base: String = "USD",
    val date: String,
    val rates: Map<String, Double>
)

object CurrencyEngine {

    private val DEFAULT_RATES = mapOf(
        "USD" to CurrencyRate("USD", "United States Dollar", "United States", 1.0),
        "EUR" to CurrencyRate("EUR", "Euro", "Europe", 0.92),
        "GBP" to CurrencyRate("GBP", "British Pound", "United Kingdom", 0.79),
        "JPY" to CurrencyRate("JPY", "Japanese Yen", "Japan", 151.5),
        "CNY" to CurrencyRate("CNY", "Chinese Yuan", "China", 7.24),
        "IDR" to CurrencyRate("IDR", "Indonesian Rupiah", "Indonesia", 15570.0),
        "KRW" to CurrencyRate("KRW", "South Korean Won", "South Korea", 1330.0),
        "SGD" to CurrencyRate("SGD", "Singapore Dollar", "Singapore", 1.35),
        "AUD" to CurrencyRate("AUD", "Australian Dollar", "Australia", 1.52),
        "CAD" to CurrencyRate("CAD", "Canadian Dollar", "Canada", 1.36),
        "CHF" to CurrencyRate("CHF", "Swiss Franc", "Switzerland", 0.89),
        "HKD" to CurrencyRate("HKD", "Hong Kong Dollar", "Hong Kong", 7.82),
        "NZD" to CurrencyRate("NZD", "New Zealand Dollar", "New Zealand", 1.64),
        "INR" to CurrencyRate("INR", "Indian Rupee", "India", 83.1),
        "BRL" to CurrencyRate("BRL", "Brazilian Real", "Brazil", 4.95),
        "MXN" to CurrencyRate("MXN", "Mexican Peso", "Mexico", 17.0),
        "ZAR" to CurrencyRate("ZAR", "South African Rand", "South Africa", 18.8),
        "RUB" to CurrencyRate("RUB", "Russian Ruble", "Russia", 92.5),
        "TRY" to CurrencyRate("TRY", "Turkish Lira", "Turkey", 32.1),
        "THB" to CurrencyRate("THB", "Thai Baht", "Thailand", 35.8),
        "MYR" to CurrencyRate("MYR", "Malaysian Ringgit", "Malaysia", 4.7),
        "PHP" to CurrencyRate("PHP", "Philippine Peso", "Philippines", 56.2),
        "VND" to CurrencyRate("VND", "Vietnamese Dong", "Vietnam", 24500.0),
        "AED" to CurrencyRate("AED", "UAE Dirham", "UAE", 3.67),
        "SAR" to CurrencyRate("SAR", "Saudi Riyal", "Saudi Arabia", 3.75),
        "PLN" to CurrencyRate("PLN", "Polish Zloty", "Poland", 3.98),
        "SEK" to CurrencyRate("SEK", "Swedish Krona", "Sweden", 10.6),
        "NOK" to CurrencyRate("NOK", "Norwegian Krone", "Norway", 10.8),
        "DKK" to CurrencyRate("DKK", "Danish Krone", "Denmark", 6.9),
        "CZK" to CurrencyRate("CZK", "Czech Koruna", "Czech Republic", 23.1),
        "HUF" to CurrencyRate("HUF", "Hungarian Forint", "Hungary", 362.0),
        "ILS" to CurrencyRate("ILS", "Israeli Shekel", "Israel", 3.7),
        "CLP" to CurrencyRate("CLP", "Chilean Peso", "Chile", 920.0),
        "COP" to CurrencyRate("COP", "Colombian Peso", "Colombia", 3900.0),
        "PEN" to CurrencyRate("PEN", "Peruvian Sol", "Peru", 3.7),
        "ARS" to CurrencyRate("ARS", "Argentine Peso", "Argentina", 850.0),
        "EGP" to CurrencyRate("EGP", "Egyptian Pound", "Egypt", 30.9),
        "NGN" to CurrencyRate("NGN", "Nigerian Naira", "Nigeria", 1500.0),
        "KES" to CurrencyRate("KES", "Kenyan Shilling", "Kenya", 130.0),
        "GHS" to CurrencyRate("GHS", "Ghanaian Cedi", "Ghana", 13.2),
        "MAD" to CurrencyRate("MAD", "Moroccan Dirham", "Morocco", 9.9),
        "TND" to CurrencyRate("TND", "Tunisian Dinar", "Tunisia", 3.1),
        "XOF" to CurrencyRate("XOF", "CFA Franc BCEAO", "West Africa", 600.0),
        "XAF" to CurrencyRate("XAF", "CFA Franc BEAC", "Central Africa", 600.0),
    )

    val allCurrencies: List<CurrencyRate> = DEFAULT_RATES.values.toList().sortedBy { it.code }

    fun getRate(code: String, base: String = "USD", rates: Map<String, Double> = DEFAULT_RATES.mapValues { it.value.rate }): Double {
        val baseRate = rates[base] ?: 1.0
        val targetRate = rates[code] ?: 1.0
        return targetRate / baseRate
    }

    fun convert(amount: Double, from: String, to: String, rates: Map<String, Double>): Double {
        if (from == to) return amount
        val fromRate = rates[from] ?: 1.0
        val toRate = rates[to] ?: 1.0
        return amount * (toRate / fromRate)
    }

    fun formatResult(value: Double): String {
        if (value.isInfinite() || value.isNaN()) return "Error"
        val rounded = (value * 10000).roundToLong() / 10000.0
        return if (rounded == rounded.toLong().toDouble()) rounded.toLong().toString() else rounded.toString()
    }
}