// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import navcontroller.NavController
import navcontroller.NavigationHost
import navcontroller.composable
import navcontroller.rememberNavController
import screen.*
import ui.theme.BwfTheme
import ui.viewmodel.LocalMatchViewModel
import ui.viewmodel.PlayerProfileViewModel
import ui.viewmodel.WorldRankingViewModel
import utilities.Constants
import utilities.DataStoreUtils
import utilities.LocalScreenSize


fun main() = application {
    val navController by rememberNavController(Screen.MainScreen.name)
    val currentScreen by remember { navController.currentScreen }
    val theme = mutableStateOf(0)
    Window(
        title = "BwfBadminton",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 375.dp, height = 812.dp),
        icon = painterResource("svg/logo-bwf-rgb.svg")
    ) {
        theme.value = DataStoreUtils.readIntData(Constants.KEY_THEME)
        CompositionLocalProvider(LocalScreenSize provides IntSize(window.width, window.height)) {
            BwfTheme(theme = theme.value) {
                CustomNavigationHost(window, navController)
            }
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

        composable(Screen.PlayerProfileScreen.name) {
            val playerProfileViewModel = PlayerProfileViewModel.viewModel()
            PlayerProfileScreen(navController, playerProfileViewModel)
        }

        composable(Screen.LocalMatchScreen.name) {
            val localMatchViewModel = LocalMatchViewModel.viewModel()
            LocalMatchScreen(navController, localMatchViewModel)
        }
    }.build()
}
