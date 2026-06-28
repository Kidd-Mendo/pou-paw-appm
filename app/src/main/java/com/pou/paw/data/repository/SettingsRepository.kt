package com.pou.paw.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository @Inject constructor(private val prefs: SharedPreferences) : ISettingsRepository {
    private val _theme = MutableStateFlow(prefs.getString("theme", "Claro") ?: "Claro")
    override val theme: Flow<String> = _theme.asStateFlow()

    private val _language = MutableStateFlow(prefs.getString("language", "es") ?: "es")
    override val language: Flow<String> = _language.asStateFlow()

    private val _userName = MutableStateFlow(prefs.getString("user_name", "Usuario") ?: "Usuario")
    override val userName: Flow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow(prefs.getString("user_email", "") ?: "")
    override val userEmail: Flow<String> = _userEmail.asStateFlow()

    override suspend fun setTheme(theme: String) {
        prefs.edit().putString("theme", theme).apply()
        _theme.value = theme
    }

    override suspend fun setLanguage(language: String) {
        prefs.edit().putString("language", language).apply()
        _language.value = language
    }

    override suspend fun updateProfile(name: String, email: String) {
        prefs.edit().putString("user_name", name).putString("user_email", email).apply()
        _userName.value = name
        _userEmail.value = email
    }
}
