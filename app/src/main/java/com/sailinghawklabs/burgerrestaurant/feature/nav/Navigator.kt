package com.sailinghawklabs.burgerrestaurant.feature.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sailinghawklabs.burgerrestaurant.feature.auth.AuthScreen
import com.sailinghawklabs.burgerrestaurant.feature.splash.SplashScreen


@Composable
fun Navigator(
    startDestination: Destination = Destination.SplashScreen,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Destination.SplashScreen> {
            SplashScreen(
                onDoneClick = {
                    navController.navigateAndDontComeBack(Destination.AuthScreen)
                }
            )
        }

        composable<Destination.AuthScreen> {
            AuthScreen(
                onGotoMainScreen = {}
            )
        }
    }

}