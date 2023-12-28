package com.example.babytalkmonitor.ui.screens.navigation

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.babytalkmonitor.ui.screens.loadingScreen.LoadingScreen
import com.example.babytalkmonitor.ui.screens.recordingScreen.RecordingScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(navController = navController, startDestination = NavigationEnums.LOADING.name) {

        composable(NavigationEnums.LOADING.name) {
            LoadingScreen(
                innerPadding = innerPadding,
                { navController.navigate(NavigationEnums.HOME.name) }
            )
        }


        composable(NavigationEnums.HOME.name) {
            RecordingScreen(
                innerPadding = innerPadding,
                { navController.navigate(NavigationEnums.HOME.name) }
            )
        }
    }
}