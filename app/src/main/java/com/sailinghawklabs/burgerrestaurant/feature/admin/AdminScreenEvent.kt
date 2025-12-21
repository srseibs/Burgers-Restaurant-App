package com.sailinghawklabs.burgerrestaurant.feature.admin

// Events to the ViewModel <---- from the Screen
sealed interface AdminScreenEvent {
    data object RequestNavigateBack : AdminScreenEvent
    data class RequestNavigateToManageProduct(val productId: String?) : AdminScreenEvent
}
