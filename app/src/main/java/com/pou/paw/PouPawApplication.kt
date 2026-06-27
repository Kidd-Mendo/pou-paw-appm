package com.pou.paw

import android.app.Application
import com.pou.paw.data.local.AppDatabase
import com.pou.paw.data.repository.*

class PouPawApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    
    val reminderRepository: IReminderRepository by lazy {
        ReminderRepository(database.reminderDao()) 
    }
    
    val petPlantRepository: IPetPlantRepository by lazy {
        PetPlantRepository(database.petDao(), database.plantDao())
    }
    
    val settingsRepository: ISettingsRepository by lazy {
        val prefs = getSharedPreferences("root_paw_settings", MODE_PRIVATE)
        SettingsRepository(prefs)
    }
    
    val statsRepository: IStatsRepository by lazy {
        val prefs = getSharedPreferences("root_paw_settings", MODE_PRIVATE)
        StatsRepository(prefs)
    }
}
