package com.example.ioscalculator.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.rxjava2.RxDataStoreBuilder
import androidx.datastore.rxjava2.RxDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class PreferencesRepository(context: Context, scope: kotlinx.coroutines.CoroutineScope) {

    private val dataStore: DataStore<Preferences> = context.preferencesDataStore("calculator_prefs")

    private object Keys {
        val ANGLE_MODE = preferencesKey<Int>("angle_mode")
        val INVERSE_MODE = preferencesKey<Boolean>("inverse_mode")
        val THEME_MODE = preferencesKey<Int>("theme_mode")
        val LAST_CURRENCY_BASE = preferencesKey<String>("last_currency_base")
        val CACHED_RATES = preferencesKey<String>("cached_rates")
        val RATES_TIMESTAMP = preferencesKey<Long>("rates_timestamp")
    }

    // Angle mode: 0 = DEG, 1 = RAD
    val angleModeFlow = dataStore.data.map { it[Keys.ANGLE_MODE] ?: 0 }
    suspend fun setAngleMode(mode: Int) { dataStore.edit { it[Keys.ANGLE_MODE] = mode } }

    val inverseModeFlow = dataStore.data.map { it[Keys.INVERSE_MODE] ?: false }
    suspend fun setInverseMode(enabled: Boolean) { dataStore.edit { it[Keys.INVERSE_MODE] = enabled } }

    // Theme: 0 = System, 1 = Light, 2 = Dark
    val themeModeFlow = dataStore.data.map { it[Keys.THEME_MODE] ?: 0 }
    suspend fun setThemeMode(mode: Int) { dataStore.edit { it[Keys.THEME_MODE] = mode } }

    // Currency
    val lastCurrencyBaseFlow = dataStore.data.map { it[Keys.LAST_CURRENCY_BASE] ?: "USD" }
    suspend fun setLastCurrencyBase(base: String) { dataStore.edit { it[Keys.LAST_CURRENCY_BASE] = base } }

    val cachedRatesFlow = dataStore.data.map { it[Keys.CACHED_RATES] ?: "" }
    val ratesTimestampFlow = dataStore.data.map { it[Keys.RATES_TIMESTAMP] ?: 0L }
    suspend fun cacheRates(json: String) {
        dataStore.edit {
            it[Keys.CACHED_RATES] = json
            it[Keys.RATES_TIMESTAMP] = System.currentTimeMillis()
        }
    }
}