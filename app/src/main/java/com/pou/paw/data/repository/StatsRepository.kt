package com.pou.paw.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import com.pou.paw.data.model.UserStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class StatsRepository @Inject constructor(private val prefs: SharedPreferences) : IStatsRepository {
    private val _stats = MutableStateFlow(
        UserStats(
            streakDays = prefs.getInt("streak_days", 0),
            totalTasksCompleted = prefs.getInt("total_tasks", 0),
            achievements = prefs.getStringSet("achievements", emptySet())?.toList() ?: emptyList()
        )
    )
    override val userStats: Flow<UserStats> = _stats.asStateFlow()

    override suspend fun updateStreak(days: Int) = withContext(Dispatchers.IO) {
        prefs.edit().putInt("streak_days", days).apply()
        _stats.value = _stats.value.copy(streakDays = days)
    }

    override suspend fun incrementTasks() = withContext(Dispatchers.IO) {
        val newTotal = _stats.value.totalTasksCompleted + 1
        prefs.edit().putInt("total_tasks", newTotal).apply()
        _stats.value = _stats.value.copy(totalTasksCompleted = newTotal)
        
        // Verificar logros por tareas completadas
        checkTaskAchievements(newTotal)
    }

    override suspend fun addAchievement(achievement: String) = withContext(Dispatchers.IO) {
        val current = _stats.value.achievements.toMutableList()
        if (!current.contains(achievement)) {
            current.add(achievement)
            prefs.edit().putStringSet("achievements", current.toSet()).apply()
            _stats.value = _stats.value.copy(achievements = current)
        }
    }

    private suspend fun checkTaskAchievements(total: Int) {
        if (total == 1) addAchievement("Primer paso: Completa tu primera tarea")
        if (total == 10) addAchievement("Cuidador principiante: 10 tareas completadas")
        if (total == 50) addAchievement("Experto en cuidados: 50 tareas completadas")
    }
}
