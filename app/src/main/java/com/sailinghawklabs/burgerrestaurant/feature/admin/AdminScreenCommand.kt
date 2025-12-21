package com.sailinghawklabs.burgerrestaurant.feature.admin

// One-time commands from the ViewModel ---> to the Screen
sealed interface AdminScreenCommand {
    data object NavigateBack : AdminScreenCommand
}