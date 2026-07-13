package com.pou.paw.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SettingsRepository @Inject constructor(private val prefs: SharedPreferences) : ISettingsRepository {
    private val _theme = MutableStateFlow(prefs.getString("theme", "Claro") ?: "Claro")
    override val theme: Flow<String> = _theme.asStateFlow()

    private val _language = MutableStateFlow(prefs.getString("language", "es") ?: "es")
    override val language: Flow<String> = _language.asStateFlow()

    private val _userName = MutableStateFlow(prefs.getString("user_name", "Usuario") ?: "Usuario")
    override val userName: Flow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow(prefs.getString("user_email", "") ?: "")
    override val userEmail: Flow<String> = _userEmail.asStateFlow()

    private val _userPhotoUri = MutableStateFlow(prefs.getString("user_photo_uri", null))
    override val userPhotoUri: Flow<String?> = _userPhotoUri.asStateFlow()

    private val _registrationDate = MutableStateFlow(getOrInitRegistrationDate())
    override val registrationDate: Flow<Long> = _registrationDate.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean("notifications_enabled", true))
    override val notificationsEnabled: Flow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _remindersEnabled = MutableStateFlow(prefs.getBoolean("reminders_enabled", true))
    override val remindersEnabled: Flow<Boolean> = _remindersEnabled.asStateFlow()

    private val _soundsEnabled = MutableStateFlow(prefs.getBoolean("sounds_enabled", true))
    override val soundsEnabled: Flow<Boolean> = _soundsEnabled.asStateFlow()

    private fun getOrInitRegistrationDate(): Long {
        val date = prefs.getLong("registration_date", 0L)
        return if (date == 0L) {
            val now = System.currentTimeMillis()
            prefs.edit().putLong("registration_date", now).apply()
            now
        } else {
            date
        }
    }

    override suspend fun setTheme(theme: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString("theme", theme).apply()
        _theme.value = theme
    }

    override suspend fun setLanguage(language: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString("language", language).apply()
        _language.value = language
    }

    override suspend fun updateProfile(name: String, email: String, photoUri: String?) = withContext(Dispatchers.IO) {
        val editor = prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
        
        if (photoUri != null) {
            editor.putString("user_photo_uri", photoUri)
        }
        
        editor.apply()
        
        _userName.value = name
        _userEmail.value = email
        if (photoUri != null) {
            _userPhotoUri.value = photoUri
        }
    }

    override suspend fun toggleNotifications(enabled: Boolean) = withContext(Dispatchers.IO) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
        _notificationsEnabled.value = enabled
    }

    override suspend fun toggleReminders(enabled: Boolean) = withContext(Dispatchers.IO) {
        prefs.edit().putBoolean("reminders_enabled", enabled).apply()
        _remindersEnabled.value = enabled
    }

    override suspend fun toggleSounds(enabled: Boolean) = withContext(Dispatchers.IO) {
        prefs.edit().putBoolean("sounds_enabled", enabled).apply()
        _soundsEnabled.value = enabled
    }

    override suspend fun changePassword(newPassword: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString("user_password", newPassword).apply()
    }
}
