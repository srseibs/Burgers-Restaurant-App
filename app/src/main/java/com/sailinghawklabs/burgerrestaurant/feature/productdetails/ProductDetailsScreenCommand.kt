package com.sailinghawklabs.burgerrestaurant.feature.productdetails

// One-time commands from the ViewModel ---> to the Screen
sealed interface ProductDetailsScreenCommand {
    data object NavigateBack : ProductDetailsScreenCommand
}