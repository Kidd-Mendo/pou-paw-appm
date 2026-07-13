package com.pou.paw.data.repository

import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    val theme: Flow<String>
    val language: Flow<String>
    val userName: Flow<String>
    val userEmail: Flow<String>
    val userPhotoUri: Flow<String?>
    val registrationDate: Flow<Long>
    val notificationsEnabled: Flow<Boolean>
    val remindersEnabled: Flow<Boolean>
    val soundsEnabled: Flow<Boolean>
    
    suspend fun setTheme(theme: String)
    suspend fun setLanguage(language: String)
    suspend fun updateProfile(name: String, email: String, photoUri: String? = null)
    suspend fun toggleNotifications(enabled: Boolean)
    suspend fun toggleReminders(enabled: Boolean)
    suspend fun toggleSounds(enabled: Boolean)
    suspend fun changePassword(newPassword: String)
}
