package com.pou.paw.data.repository

import com.pou.paw.data.model.UserStats
import kotlinx.coroutines.flow.Flow

interface IStatsRepository {
    val userStats: Flow<UserStats>
    suspend fun updateStreak(days: Int)
    suspend fun incrementTasks()
}
