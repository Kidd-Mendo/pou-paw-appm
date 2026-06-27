package com.pou.paw.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pou.paw.ui.screens.DashboardScreen
import com.pou.paw.ui.screens.AddEntityScreen
import com.pou.paw.ui.screens.LoginScreen
import com.pou.paw.ui.screens.SettingsScreen
import com.pou.paw.ui.screens.ProfileScreen
import com.pou.paw.ui.screens.HistoryScreen

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pou.paw.PouPawApplication
import com.pou.paw.ui.viewmodel.DashboardViewModel
import com.pou.paw.ui.viewmodel.AddEntityViewModel
import com.pou.paw.ui.viewmodel.ProfileViewModel
import com.pou.paw.ui.viewmodel.HistoryViewModel
import com.pou.paw.ui.viewmodel.SettingsViewModel
import com.pou.paw.ui.viewmodel.LoginViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as PouPawApplication
    
    // Repositorios centralizados desde la Application (SSOT)
    val reminderRepo = app.reminderRepository
    val petPlantRepo = app.petPlantRepository
    val settingsRepo = app.settingsRepository
    val statsRepo = app.statsRepository

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        composable("dashboard") {
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DashboardViewModel(reminderRepo, petPlantRepo, settingsRepo, statsRepo) as T
                    }
                }
            )
            DashboardScreen(
                viewModel = dashboardViewModel,
                onAddClick = { navController.navigate("add") },
                onSettingsClick = { navController.navigate("settings") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
        
        composable("add") {
            val addEntityViewModel: AddEntityViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AddEntityViewModel(reminderRepo) as T
                    }
                }
            )
            AddEntityScreen(
                viewModel = addEntityViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable("settings") {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SettingsViewModel(settingsRepo) as T
                    }
                }
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable("profile") {
            val profileViewModel: ProfileViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ProfileViewModel(reminderRepo, petPlantRepo, settingsRepo) as T
                    }
                }
            )
            ProfileScreen(
                viewModel = profileViewModel,
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
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HistoryViewModel(statsRepo) as T
                    }
                }
            )
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
