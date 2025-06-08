package com.example.memorymatch

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.memorymatch.data.LanguagePreferences
import com.example.memorymatch.data.LocaleManager
import com.example.memorymatch.ui.screens.GameScreen
import com.example.memorymatch.ui.screens.MainMenuScreen
import com.example.memorymatch.ui.theme.MemoryMatchTheme
import com.example.memorymatch.navigation.AppNavigation
import com.example.memorymatch.notification.DailyReminderScheduler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        DailyReminderScheduler.scheduleDailyReminder(this)

        setContent {
            MemoryMatchTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    override fun attachBaseContext(newBase: Context) {
        val lang = LanguagePreferences.getLanguage(newBase)
        val context = LocaleManager.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "menu",
        modifier = modifier
    ) {

        composable("menu") {
            MainMenuScreen(
                onStartGame = { players, gridSize ->
                    navController.navigate("game/$players/$gridSize")
                },
            )
        }
        composable(
            route = "game/{players}/{gridSize}",
            arguments = listOf(
                navArgument("players") { type = NavType.IntType },
                navArgument("gridSize") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val players = backStackEntry.arguments?.getInt("players") ?: 1
            val gridSize = backStackEntry.arguments?.getInt("gridSize") ?: 4
            GameScreen(
                players = players,
                gridSize = gridSize,
                onBackToMenu = {
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                }
            )
        }
    }
}
