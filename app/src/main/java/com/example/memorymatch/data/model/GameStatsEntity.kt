package com.example.memorymatch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_statistics")
data class GameStatisticsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val attempts: Int,
    val gameTimeMillis: Long,
    val boardSize: String,
    val numberOfPlayers: Int,
    val winner: String?
)
