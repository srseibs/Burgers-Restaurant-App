package com.sailinghawklabs.burgerrestaurant.feature.ui.nav

import androidx.navigation.NavController


fun NavController.navigateAndDontComeBack(
    destination: Any
) {
    val currentBackStackEntry = this.currentBackStackEntry
    val currentRoute = currentBackStackEntry?.destination?.route

    this.navigate(destination) {
        if (currentRoute != null) {
            popUpTo(currentRoute) {
                inclusive = true
            }
        }
        launchSingleTop = true
    }
}