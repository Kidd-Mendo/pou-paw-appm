package com.pou.paw.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import com.pou.paw.data.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StatsRepository @Inject constructor(private val prefs: SharedPreferences) : IStatsRepository {
    private val _stats = MutableStateFlow(
        UserStats(
            streakDays = prefs.getInt("streak_days", 0),
            totalTasksCompleted = prefs.getInt("total_tasks", 0)
        )
    )
    override val userStats: Flow<UserStats> = _stats.asStateFlow()

    override suspend fun updateStreak(days: Int) {
        prefs.edit().putInt("streak_days", days).apply()
        _stats.value = _stats.value.copy(streakDays = days)
    }

    override suspend fun incrementTasks() {
        val newTotal = _stats.value.totalTasksCompleted + 1
        prefs.edit().putInt("total_tasks", newTotal).apply()
        _stats.value = _stats.value.copy(totalTasksCompleted = newTotal)
    }
}
