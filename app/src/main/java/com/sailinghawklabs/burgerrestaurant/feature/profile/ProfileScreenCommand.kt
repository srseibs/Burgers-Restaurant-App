package com.sailinghawklabs.burgerrestaurant.feature.profile

// One-time commands from the ViewModel ---> to the Screen
sealed interface ProfileScreenCommand {
    data object NavigateToMainScreen : ProfileScreenCommand
    data class ShowToast(val message: String) : ProfileScreenCommand
}
