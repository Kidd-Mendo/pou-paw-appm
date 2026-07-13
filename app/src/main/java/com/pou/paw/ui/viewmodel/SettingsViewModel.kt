package com.pou.paw.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.repository.ISettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val currentTheme: String = "Claro",
    val currentLanguage: String = "Español",
    val userName: String = "",
    val userEmail: String = "",
    val notificationsEnabled: Boolean = true,
    val remindersEnabled: Boolean = true,
    val soundsEnabled: Boolean = true,
    val showThemeDialog: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val showAboutDialog: Boolean = false,
    val showProfileDialog: Boolean = false,
    val showPasswordDialog: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    private val _showThemeDialog = MutableStateFlow(false)
    private val _showLanguageDialog = MutableStateFlow(false)
    private val _showAboutDialog = MutableStateFlow(false)
    private val _showProfileDialog = MutableStateFlow(false)
    private val _showPasswordDialog = MutableStateFlow(false)

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.theme,
        settingsRepository.language,
        settingsRepository.userName,
        settingsRepository.userEmail,
        settingsRepository.notificationsEnabled,
        settingsRepository.remindersEnabled,
        settingsRepository.soundsEnabled,
        _showThemeDialog,
        _showLanguageDialog,
        _showAboutDialog,
        _showProfileDialog,
        _showPasswordDialog
    ) { args ->
        val langCode = args[1] as String
        SettingsUiState(
            currentTheme = args[0] as String,
            currentLanguage = if (langCode == "en") "English" else "Español",
            userName = args[2] as String,
            userEmail = args[3] as String,
            notificationsEnabled = args[4] as Boolean,
            remindersEnabled = args[5] as Boolean,
            soundsEnabled = args[6] as Boolean,
            showThemeDialog = args[7] as Boolean,
            showLanguageDialog = args[8] as Boolean,
            showAboutDialog = args[9] as Boolean,
            showProfileDialog = args[10] as Boolean,
            showPasswordDialog = args[11] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun toggleThemeDialog(show: Boolean) { _showThemeDialog.value = show }
    fun toggleLanguageDialog(show: Boolean) { _showLanguageDialog.value = show }
    fun toggleAboutDialog(show: Boolean) { _showAboutDialog.value = show }
    fun toggleProfileDialog(show: Boolean) { _showProfileDialog.value = show }
    fun togglePasswordDialog(show: Boolean) { _showPasswordDialog.value = show }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
            toggleThemeDialog(false)
        }
    }

    fun setLanguage(languageDisplayName: String) {
        val code = if (languageDisplayName.contains("English", ignoreCase = true) || 
                       languageDisplayName.contains("en", ignoreCase = true)) "en" else "es"
        viewModelScope.launch {
            settingsRepository.setLanguage(code)
            toggleLanguageDialog(false)
        }
    }

    fun updateProfile(name: String, email: String) {
        viewModelScope.launch {
            settingsRepository.updateProfile(name, email)
            toggleProfileDialog(false)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.toggleNotifications(enabled) }
    }

    fun toggleReminders(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.toggleReminders(enabled) }
    }

    fun toggleSounds(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.toggleSounds(enabled) }
    }

    fun changePassword(password: String) {
        viewModelScope.launch {
            settingsRepository.changePassword(password)
            togglePasswordDialog(false)
        }
    }
}
