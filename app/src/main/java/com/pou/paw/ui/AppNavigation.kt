package com.pou.paw.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pou.paw.PouPawApplication
import com.pou.paw.ui.screens.*
import com.pou.paw.ui.viewmodel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as PouPawApplication
    
    val reminderRepo = app.reminderRepository
    val petPlantRepo = app.petPlantRepository
    val settingsRepo = app.settingsRepository
    val statsRepo = app.statsRepository

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                        return LoginViewModel(extras.createSavedStateHandle()) as T
                    }
                }
            )
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
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                        return DashboardViewModel(
                            reminderRepo, petPlantRepo, settingsRepo, statsRepo,
                            app.filterEntitiesUseCase,
                            extras.createSavedStateHandle()
                        ) as T
                    }
                }
            )
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
            val addEntityViewModel: AddEntityViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                        return AddEntityViewModel(app.saveReminderUseCase, extras.createSavedStateHandle()) as T
                    }
                }
            )
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
                onSaveReminder = addEntityViewModel::saveReminder,
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SettingsViewModel(settingsRepo) as T
                    }
                }
            )
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
                onSetTheme = settingsViewModel::setTheme,
                onSetLanguage = settingsViewModel::setLanguage,
                onUpdateProfile = settingsViewModel::updateProfile
            )
        }

        composable("profile") {
            val profileViewModel: ProfileViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ProfileViewModel(
                            reminderRepo, 
                            petPlantRepo, 
                            settingsRepo,
                            app.getEntityCountsUseCase
                        ) as T
                    }
                }
            )
            val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                uiState = uiState,
                onDashboardClick = { 
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onAddClick = { navController.navigate("add") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }

        composable("history") {
            val historyViewModel: HistoryViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HistoryViewModel(statsRepo) as T
                    }
                }
            )
            val uiState by historyViewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                uiState = uiState,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
