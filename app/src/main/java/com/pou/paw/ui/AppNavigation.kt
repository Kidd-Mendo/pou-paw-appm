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
import com.pou.paw.data.model.UserStats

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pou.paw.PouPawApplication
import com.pou.paw.ui.viewmodel.DashboardViewModel
import com.pou.paw.ui.viewmodel.AddEntityViewModel
import com.pou.paw.ui.viewmodel.ProfileViewModel
import com.pou.paw.ui.viewmodel.HistoryViewModel
import com.pou.paw.ui.viewmodel.SettingsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.pou.paw.ui.viewmodel.LoginViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = (context.applicationContext as PouPawApplication).repository

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
                        return DashboardViewModel(repository) as T
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
                        return AddEntityViewModel(repository) as T
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
                        val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                        return SettingsViewModel(prefs) as T
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
                        return ProfileViewModel(repository) as T
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
            val historyViewModel: HistoryViewModel = viewModel()
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
