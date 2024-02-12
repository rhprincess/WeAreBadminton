// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.convertapi.client.Config
import navcontroller.NavController
import navcontroller.NavigationHost
import navcontroller.composable
import navcontroller.rememberNavController
import screen.*
import ui.theme.BwfTheme
import ui.theme.GlobalThemeChanger
import ui.viewmodel.*
import ui.widget.WindowTitleBar
import utilities.LocalScreenSize
import utilities.LocalWindowState

fun main() = application {
    Config.setDefaultSecret("UyNa180DV1l0RE8c")
    val navController by rememberNavController(Screen.MainScreen)
    val windowState = rememberWindowState(width = 375.dp, height = 865.dp)
    Window(
        title = "世界羽联信息平台",
        onCloseRequest = ::exitApplication,
        state = windowState,
        icon = painterResource("svg/logo-bwf-rgb.svg"),
        undecorated = true,
        transparent = true,
        resizable = false
    ) {
        CompositionLocalProvider(
            LocalScreenSize provides windowState.size,
            LocalWindowState provides windowState
        ) {
            GlobalThemeChanger { theme, darkTheme ->
                BwfTheme(theme = theme, darkTheme = darkTheme) {
                    Box(
                        Modifier.padding(if (windowState.placement == WindowPlacement.Maximized) 0.dp else 10.dp)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(8.dp))
                    ) {
                        Column(
                            Modifier
                                .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            WindowTitleBar(windowState, onCloseEvent = { exitApplication() })
                            CustomNavigationHost(window, navController, theme, darkTheme)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomNavigationHost(
    window: ComposeWindow,
    navController: NavController,
    theme: MutableState<Int>,
    darkTheme: MutableState<Boolean>
) {
    NavigationHost(window, navController) {
        composable(Screen.MainScreen) {
            MainScreen(navController)
        }

        composable(Screen.SettingsScreen) {
            SettingsScreen(navController, theme, darkTheme)
        }

        composable(Screen.WorldRankingScreen) {
            val worldRankingViewModel = WorldRankingViewModel.viewModel()
            WorldRankingScreen(navController, worldRankingViewModel)
        }

        composable(Screen.PlayerProfileScreen) {
            val playerProfileViewModel = PlayerProfileViewModel.viewModel()
            PlayerProfileScreen(navController, playerProfileViewModel)
        }

        composable(Screen.CurrentLiveScreen) {
            CurrentLiveScreen(navController)
        }

        composable(Screen.LiveMatchScreen) {
            val liveMatchViewModel = LiveMatchViewModel.viewModel()
            LiveMatchScreen(navController, liveMatchViewModel)
        }

        composable(Screen.AllYearMatchesScreen) {
            val allYearMatchesViewModel = AllYearMatchesViewModel.viewModel()
            AllYearMatchesScreen(navController, allYearMatchesViewModel)
        }

        composable(Screen.HeadToHeadScreen) {
            val hthViewModel = HTHViewModel.viewModel()
            HTHScreen(navController, hthViewModel)
        }

        composable(Screen.PopularPlayersScreen) {
            val popularPlayersViewModel = PlayersViewModel.viewModel()
            PopularPlayersScreen(navController, popularPlayersViewModel)
        }
    }.build()
}
