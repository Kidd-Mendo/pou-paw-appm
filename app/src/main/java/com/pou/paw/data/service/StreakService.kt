package com.pou.paw.data.service

import com.pou.paw.data.model.UserStats

object StreakService {
    fun updateStreak(stats: UserStats, taskCompleted: Boolean): UserStats {
        return if (taskCompleted) {
            stats.copy(
                streakDays = stats.streakDays + 1,
                totalTasksCompleted = stats.totalTasksCompleted + 1,
                achievements = checkAchievements(stats.totalTasksCompleted + 1)
            )
        } else {
            stats.copy(streakDays = 0)
        }
    }

    private fun checkAchievements(total: Int): List<String> {
        val list = mutableListOf<String>()
        if (total >= 1) list.add("Primer Paso")
        if (total >= 10) list.add("Cuidador Estrella")
        if (total >= 50) list.add("Maestro de la Naturaleza")
        return list
    }
}
