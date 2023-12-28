package com.example.babytalkmonitor

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.babytalkmonitor.ui.screens.navigation.NavGraph
import com.example.babytalkmonitor.ui.screens.navigation.NavigationEnums


@Composable
fun BtmApp (navController : NavHostController? = null){
    var navCont : NavHostController = rememberNavController()

    var logMsg = navCont
        .currentBackStackEntryAsState().value?.destination?.route
    Log.e("ACTIVITY ", "WE ARE HERE >>> $logMsg")
    if (navController != null) {
        navCont = navController
    }

    Scaffold(

    ) { innerPadding ->

        NavGraph(navController = navCont, innerPadding = innerPadding)

    }
}