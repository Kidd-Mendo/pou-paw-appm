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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("dashboard") {
            DashboardScreen(
                onAddClick = { navController.navigate("add") },
                onSettingsClick = { navController.navigate("settings") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
        composable("add") {
            AddEntityScreen(onBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen(
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
            HistoryScreen(
                stats = UserStats(
                    streakDays = 5,
                    totalTasksCompleted = 12,
                    achievements = listOf("Primer Paso", "Cuidador Estrella")
                ),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
