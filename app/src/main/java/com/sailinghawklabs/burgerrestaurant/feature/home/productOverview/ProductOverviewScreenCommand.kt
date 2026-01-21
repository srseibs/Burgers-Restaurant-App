package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

// One-time commands from the ViewModel ---> to the Screen
sealed interface ProductOverviewScreenCommand {
    data object NavigateToMainScreen : ProductOverviewScreenCommand
    data class NavigateToProductDetailsScreen(val productId: String) : ProductOverviewScreenCommand
}