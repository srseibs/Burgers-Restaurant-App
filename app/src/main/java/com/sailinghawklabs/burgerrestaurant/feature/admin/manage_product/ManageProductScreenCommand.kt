package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

// One-time commands from the ViewModel ---> to the Screen
sealed interface ManageProductScreenCommand {
    data object NavigateToMainScreen : ManageProductScreenCommand
    data class ShowMessage(val message: String) : ManageProductScreenCommand
}