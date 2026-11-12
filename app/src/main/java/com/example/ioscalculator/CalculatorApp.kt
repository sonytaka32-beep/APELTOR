package com.example.ioscalculator

import android.app.Application
import androidx.room.Room
import com.example.ioscalculator.data.CalculatorDatabase
import com.example.ioscalculator.data.HistoryDao
import com.example.ioscalculator.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CalculatorApp : Application() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job)

    private var _database: CalculatorDatabase? = null
    val database: CalculatorDatabase
        get() {
            if (_database == null) {
                _database = Room.databaseBuilder(
                    this,
                    CalculatorDatabase::class.java,
                    "calculator_db"
                ).fallbackToDestructiveMigration().build()
            }
            return _database!!
        }

    val historyDao: HistoryDao
        get() = database.historyDao()

    val preferencesRepository: PreferencesRepository by lazy {
        PreferencesRepository(this, scope)
    }

    override fun onTerminate() {
        super.onTerminate()
        job.cancel()
        _database?.close()
    }
}