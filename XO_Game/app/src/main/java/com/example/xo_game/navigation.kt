package com.example.xo_game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.xo_game.history.Detail_History
import com.example.xo_game.history.GameRecord
import com.example.xo_game.history.List_History

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object Settings : Screen("settings")
    object History : Screen("history")

    object Game : Screen("game/{fieldSize}/{lineLengthwin}") {
        fun createRoute(fieldSize: Int, lineLengthwin: Int) = "game/$fieldSize/$lineLengthwin"
    }

    object ItemDetailScreen : Screen("itemDetail")
//    object ItemDetailScreen : Screen("itemDetail/{itemId}") {
//        fun createRoute(itemId: Int) = "itemDetail/$itemId"
//    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf<GameRecord?>(null) }

    NavHost(navController = navController, startDestination = Screen.Menu.route) {
        composable(Screen.Menu.route) {
            MenuScreen(
                NagativetoConfig = { navController.navigate(Screen.Settings.route) },
                onNavigateToItemList = { navController.navigate(Screen.History.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingtoPlay(
                onNavigateToXOPlayScreen = { field, line ->
                    navController.navigate(
                        Screen.Game.createRoute(field, line)
                    )
                },
                Menu = { navController.navigate(Screen.Menu.route) }
            )
        }
        composable(Screen.Game.route) { backStackEntry ->
            val fieldSize = backStackEntry.arguments?.getString("fieldSize")?.toInt() ?: 3
            val lineLengthwin = backStackEntry.arguments?.getString("lineLengthwin")?.toInt() ?: 3

            PlayTicTacToe(
                context = context,
                onBackToXOConfigPlays = { navController.navigate(Screen.Menu.route) },
                field = fieldSize,
                line = lineLengthwin,
                navController = navController
            )
        }
        composable(Screen.History.route) {
            List_History(
                context = context,
                onItemSelected = { gameRecord ->
                    selectedItem = gameRecord // Update selected item
                    navController.navigate(Screen.ItemDetailScreen.route)
                },
                Menu = { navController.navigate(Screen.Menu.route) }
            )
        }
        composable(Screen.ItemDetailScreen.route) {
            Detail_History(
                item = selectedItem, // Pass the selected item
                onBackToItemList = { navController.navigate(Screen.History.route) },
                Menu = { navController.navigate(Screen.Menu.route) }
            )
        }
    }
}
