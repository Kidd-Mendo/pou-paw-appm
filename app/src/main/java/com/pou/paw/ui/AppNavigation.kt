package com.pou.paw.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pou.paw.ui.screens.*
import com.pou.paw.ui.viewmodel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                loginViewModel.loginSuccess.collect {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            LoginScreen(
                uiState = uiState,
                onEmailChange = loginViewModel::onEmailChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onToggleRegister = loginViewModel::toggleRegistering,
                onLogin = loginViewModel::login,
                onGoogleLogin = loginViewModel::loginWithGoogle,
                onClearError = loginViewModel::clearError
            )
        }
        
        composable("dashboard") {
            val dashboardViewModel: DashboardViewModel = hiltViewModel()
            val uiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()
            DashboardScreen(
                uiState = uiState,
                onFilterSelected = dashboardViewModel::updateFilter,
                onCompleteTask = dashboardViewModel::completeTask,
                onAddClick = { navController.navigate("add") },
                onSettingsClick = { navController.navigate("settings") },
                onProfileClick = { navController.navigate("profile") }
            )
        }

        composable("add") {
            val addEntityViewModel: AddEntityViewModel = hiltViewModel()
            val uiState by addEntityViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                addEntityViewModel.saveSuccess.collect {
                    navController.popBackStack()
                }
            }

            AddEntityScreen(
                uiState = uiState,
                onNameChange = addEntityViewModel::onNameChange,
                onBreedChange = addEntityViewModel::onBreedChange,
                onCategoryChange = addEntityViewModel::onCategoryChange,
                onImageUriChange = addEntityViewModel::onImageUriChange,
                onActionChange = addEntityViewModel::onActionChange,
                onExpandedActionChange = addEntityViewModel::onExpandedActionChange,
                onFrequencyTypeChange = addEntityViewModel::onFrequencyTypeChange,
                onFrequencyValueChange = addEntityViewModel::onFrequencyValueChange,
                onDateChange = addEntityViewModel::onDateChange,
                onMessageChange = addEntityViewModel::onMessageChange,
                onFetchRandomImage = addEntityViewModel::fetchRandomImage,
                onSaveReminder = addEntityViewModel::saveReminder,
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            SettingsScreen(
                uiState = uiState,
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onToggleProfileDialog = settingsViewModel::toggleProfileDialog,
                onToggleThemeDialog = settingsViewModel::toggleThemeDialog,
                onToggleLanguageDialog = settingsViewModel::toggleLanguageDialog,
                onToggleAboutDialog = settingsViewModel::toggleAboutDialog,
                onTogglePasswordDialog = settingsViewModel::togglePasswordDialog,
                onToggleNotifications = settingsViewModel::toggleNotifications,
                onToggleReminders = settingsViewModel::toggleReminders,
                onToggleSounds = settingsViewModel::toggleSounds,
                onSetTheme = settingsViewModel::setTheme,
                onSetLanguage = settingsViewModel::setLanguage,
                onUpdateProfile = settingsViewModel::updateProfile,
                onChangePassword = settingsViewModel::changePassword
            )
        }

        composable("profile") {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                uiState = uiState,
                onDashboardClick = { 
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onAddClick = { navController.navigate("add") },
                onSettingsClick = { navController.navigate("settings") },
                onHistoryClick = { navController.navigate("history") },
                onStartEditing = profileViewModel::startEditing,
                onCancelEditing = profileViewModel::cancelEditing,
                onSaveProfile = profileViewModel::saveProfile,
                onNameChange = profileViewModel::onEditNameChange,
                onEmailChange = profileViewModel::onEditEmailChange,
                onPhotoChange = { uri -> profileViewModel.updatePhoto(uri.toString()) }
            )
        }

        composable("history") {
            val historyViewModel: HistoryViewModel = hiltViewModel()
            val uiState by historyViewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                uiState = uiState,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
