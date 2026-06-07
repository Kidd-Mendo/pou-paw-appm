package com.pou.paw.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pou.paw.ui.screens.DashboardScreen
import com.pou.paw.ui.screens.AddEntityScreen
import com.pou.paw.ui.screens.LoginScreen

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
            DashboardScreen(onAddClick = { navController.navigate("add") })
        }
        composable("add") {
            AddEntityScreen(onBack = { navController.popBackStack() })
        }
    }
}
