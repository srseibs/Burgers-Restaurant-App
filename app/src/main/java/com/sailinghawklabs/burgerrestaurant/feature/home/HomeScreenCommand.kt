package com.sailinghawklabs.burgerrestaurant.feature.home

// One-time commands from the ViewModel ---> to the Screen
sealed interface HomeScreenCommand {
    data object NavigateToMainScreen : HomeScreenCommand
}