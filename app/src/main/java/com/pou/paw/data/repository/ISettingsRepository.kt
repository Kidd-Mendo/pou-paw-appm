package com.pou.paw.data.repository

import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    val theme: Flow<String>
    val language: Flow<String>
    val userName: Flow<String>
    val userEmail: Flow<String>
    
    suspend fun setTheme(theme: String)
    suspend fun setLanguage(language: String)
    suspend fun updateProfile(name: String, email: String)
}
