package com.sailinghawklabs.burgerrestaurant.feature.auth

// One-time commands from the ViewModel ---> to the Screen
sealed interface AuthScreenCommand {
    data object NavigateToMainScreen : AuthScreenCommand
}