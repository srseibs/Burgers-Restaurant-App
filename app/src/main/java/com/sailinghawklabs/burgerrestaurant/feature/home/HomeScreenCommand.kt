package com.sailinghawklabs.burgerrestaurant.feature.home

// One-time commands from the ViewModel ---> to the Screen
sealed interface HomeScreenCommand {
    data object ExitDueToUserSignedOff : HomeScreenCommand
    data object NavigateToProfile : HomeScreenCommand
    data object NavigateToAdmin : HomeScreenCommand
    data class ShowErrorMessage(val message: String) : HomeScreenCommand
    data object NavigateToProductOverview : HomeScreenCommand
}