package com.example.memorymatch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.memorymatch.ui.screens.GameScreen
import com.example.memorymatch.ui.screens.MainMenuScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MainMenuScreen(
                onStartGame = { players, gridSize ->
                    navController.navigate("game/$players/$gridSize")
                },
                onStatistics = {
                    navController.navigate("statistics")
                }
            )
        }
        composable("game/{players}/{gridSize}") { backStackEntry ->
            val players = backStackEntry.arguments?.getString("players")?.toInt() ?: 1
            val gridSize = backStackEntry.arguments?.getString("gridSize")?.toInt() ?: 4
            GameScreen(players, gridSize) {
                navController.navigate("menu") {
                    popUpTo("menu") { inclusive = true }
                }
            }
        }

    }
}
