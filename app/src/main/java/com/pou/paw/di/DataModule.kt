package com.pou.paw.di

import android.content.Context
import android.content.SharedPreferences
import com.pou.paw.data.local.AppDatabase
import com.pou.paw.data.local.PetDao
import com.pou.paw.data.local.PlantDao
import com.pou.paw.data.local.ReminderDao
import com.pou.paw.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReminderRepository(repo: ReminderRepository): IReminderRepository

    @Binds
    @Singleton
    abstract fun bindPetPlantRepository(repo: PetPlantRepository): IPetPlantRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(repo: SettingsRepository): ISettingsRepository

    @Binds
    @Singleton
    abstract fun bindStatsRepository(repo: StatsRepository): IStatsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao = database.reminderDao()

    @Provides
    fun providePetDao(database: AppDatabase): PetDao = database.petDao()

    @Provides
    fun providePlantDao(database: AppDatabase): PlantDao = database.plantDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("root_paw_settings", Context.MODE_PRIVATE)
    }
}
