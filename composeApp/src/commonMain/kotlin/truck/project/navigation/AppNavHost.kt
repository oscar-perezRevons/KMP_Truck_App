package com.ucb.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import truck.project.truck_guia.presentation.screen.TruckScreen

@Composable
fun AppNavHost() {


    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = NavRoute.TruckBrands) {

        composable<NavRoute.TruckBrands> {
            TruckScreen()
        }
    }
}
