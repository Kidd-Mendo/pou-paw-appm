package com.pou.paw.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.repository.ISettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isRegistering: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val settingsRepository: ISettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Unit>()
    val loginSuccess: SharedFlow<Unit> = _loginSuccess.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun toggleRegistering() {
        _uiState.update { it.copy(isRegistering = !it.isRegistering) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Al iniciar sesión, guardamos el correo en el repositorio de settings
            // Extraemos un nombre predeterminado del correo (lo que va antes del @)
            val defaultName = _uiState.value.email.split("@").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "Usuario"
            
            settingsRepository.updateProfile(
                name = defaultName,
                email = _uiState.value.email
            )
            
            // Simulación de login exitoso
            _loginSuccess.emit(Unit)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            // Simulación con datos de Google
            settingsRepository.updateProfile(
                name = "Google User",
                email = "user.google@gmail.com"
            )
            _loginSuccess.emit(Unit)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
