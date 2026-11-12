package com.example.ioscalculator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: HistoryEntry)

    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 100")
    fun getAll(): Flow<List<HistoryEntry>>

    @Query("DELETE FROM history")
    suspend fun clearAll()

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun delete(id: Long)
}