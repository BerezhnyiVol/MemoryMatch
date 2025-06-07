package com.example.memorymatch.data.model

import com.example.memorymatch.data.dao.GameStatisticsDao

class GameStatisticsRepository(private val dao: GameStatisticsDao) {

    suspend fun insertGame(statistics: GameStatisticsEntity) {
        dao.insertGame(statistics)
    }

    suspend fun getAllGames(): List<GameStatisticsEntity> {
        return dao.getAllGames()
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
