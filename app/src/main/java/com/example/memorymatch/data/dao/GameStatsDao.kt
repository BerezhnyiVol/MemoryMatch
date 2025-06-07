package com.example.memorymatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.memorymatch.data.model.GameStatisticsEntity

@Dao
interface GameStatisticsDao {

    @Insert
    suspend fun insertGame(statistics: GameStatisticsEntity)

    @Query("SELECT * FROM game_statistics ORDER BY date DESC")
    suspend fun getAllGames(): List<GameStatisticsEntity>

    @Query("DELETE FROM game_statistics")
    suspend fun clearAll()
}
