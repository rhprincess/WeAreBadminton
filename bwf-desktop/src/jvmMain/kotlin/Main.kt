// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import navcontroller.NavController
import navcontroller.NavigationHost
import navcontroller.composable
import navcontroller.rememberNavController
import screen.MainScreen
import screen.Screen
import screen.SettingsScreen
import screen.WorldRankingScreen
import ui.theme.BwfTheme
import ui.viewmodel.WorldRankingViewModel

fun main() = application {
    val navController by rememberNavController(Screen.MainScreen.name)
    val currentScreen by remember { navController.currentScreen }

    Window(
        title = "BwfBadminton",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 375.dp, height = 812.dp),
        resizable = false,
        icon = painterResource("svg/logo-bwf-rgb.svg")
    ) {
        BwfTheme {
            CustomNavigationHost(window, navController)
        }
    }
}

@Composable
fun CustomNavigationHost(
    window: ComposeWindow,
    navController: NavController
) {
    NavigationHost(window, navController) {
        composable(Screen.MainScreen.name) {
            MainScreen(navController)
        }

        composable(Screen.WorldRankingScreen.name) {
            WorldRankingScreen(navController)
        }

        composable(Screen.SettingsScreen.name) {
            SettingsScreen(navController)
        }

        composable(Screen.WorldRankingScreen.name) {
            val worldRankingViewModel = WorldRankingViewModel.viewModel()
            WorldRankingScreen(navController, worldRankingViewModel)
        }
    }.build()
}
