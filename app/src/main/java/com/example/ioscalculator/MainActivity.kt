package com.example.ioscalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.ViewModel
import androidx.lifecycle.viewmodel.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ioscalculator.data.CalculatorDatabase
import com.example.ioscalculator.data.HistoryDao
import com.example.ioscalculator.engine.CalculatorEngine
import com.example.ioscalculator.preferences.PreferencesRepository
import com.example.ioscalculator.ui.screen.CalculatorScreen
import com.example.ioscalculator.ui.screen.ConverterScreen
import com.example.ioscalculator.ui.screen.CurrencyScreen
import com.example.ioscalculator.ui.screen.HistoryScreen
import com.example.ioscalculator.ui.screen.SettingsScreen
import com.example.ioscalculator.ui.theme.iOSCalculatorTheme

class MainActivity : ComponentActivity() {

    private val calculatorEngine by lazy { CalculatorEngine() }
    private val preferencesRepository by lazy { PreferencesRepository(this, kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.SupervisorJob())) }
    private val historyDao: HistoryDao by lazy { (application as CalculatorApp).historyDao }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            iOSCalculatorTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentScreen by remember { mutableStateOf(Screen.CALCULATOR) }

                    NavHost(navController, startDestination = Screen.CALCULATOR.route) {
                        composable(Screen.CALCULATOR.route) {
                            CalculatorScreen(
                                engine = calculatorEngine,
                                onNavigateToConverter = { navController.navigate(Screen.CONVERTER.route) },
                                onNavigateToCurrency = { navController.navigate(Screen.CURRENCY.route) },
                                onNavigateToHistory = { navController.navigate(Screen.HISTORY.route) },
                                onNavigateToSettings = { navController.navigate(Screen.SETTINGS.route) }
                            )
                        }
                        composable(Screen.CONVERTER.route) {
                            ConverterScreen(onBack = { navController.popBackStack() })
                        }
                        composable(Screen.CURRENCY.route) {
                            CurrencyScreen(onBack = { navController.popBackStack() })
                        }
                        composable(Screen.HISTORY.route) {
                            HistoryScreen(
                                onBack = { navController.popBackStack() },
                                history = historyDao.getAll().collectAsState(initial = emptyList()).value,
                                onClearHistory = { historyDao.clearAll() },
                                onItemClick = { entry ->
                                    calculatorEngine.clearAll()
                                    // Parse and set result - simplified
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.SETTINGS.route) {
                            SettingsScreen(
                                onBack = { navController.popBackStack() },
                                preferences = preferencesRepository
                            )
                        }
                    }
                }
            }
        }
    }

    enum class Screen(val route: String) {
        CALCULATOR("calculator"),
        CONVERTER("converter"),
        CURRENCY("currency"),
        HISTORY("history"),
        SETTINGS("settings")
    }
}