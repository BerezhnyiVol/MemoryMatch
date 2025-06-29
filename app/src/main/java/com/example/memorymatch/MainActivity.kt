package com.example.memorymatch

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.example.memorymatch.notification.DailyReminderScheduler

// Popis: Hlavná aktivita aplikácie MemoryMatch, zabezpečujúca inicializáciu UI, navigácie a notifikácií
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Popis: Požiadanie o povolenie pre notifikácie na novších verziách Androidu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        // Popis: Naplánovanie dennej notifikácie
        DailyReminderScheduler.scheduleDailyReminder(this)

        // Popis: Nastavenie obsahu UI pomocou Jetpack Compose
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

    // Popis: Zabezpečenie správnej lokalizácie pri štarte aplikácie
    override fun attachBaseContext(newBase: Context) {
        val lang = LanguagePreferences.getLanguage(newBase)
        val context = LocaleManager.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}

// Popis: Navigácia medzi obrazovkami aplikácie (menu a hra)
@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "menu",
        modifier = modifier
    ) {
        // Popis: Obrazovka hlavného menu
        composable(route = "menu") {
            MainMenuScreen(
                onStartGame = { players, gridSize, mode ->
                    navController.navigate("game/$players/$gridSize/$mode")
                }
            )
        }

        // Popis: Obrazovka samotnej hry s parametrami
        composable(
            route = "game/{players}/{gridSize}/{mode}",
            arguments = listOf(
                navArgument("players") { type = NavType.IntType },
                navArgument("gridSize") { type = NavType.IntType },
                navArgument("mode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val players = backStackEntry.arguments?.getInt("players") ?: 1
            val gridSize = backStackEntry.arguments?.getInt("gridSize") ?: 4
            val mode = backStackEntry.arguments?.getString("mode") ?: "classic"

            GameScreen(
                players = players,
                gridSize = gridSize,
                mode = mode,
                onBackToMenu = {
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                }
            )
        }
    }
}
