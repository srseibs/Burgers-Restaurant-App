package com.sailinghawklabs.burgerrestaurant.feature.home

// Events to the ViewModel <---- from the Screen
sealed interface HomeScreenEvent {
    data object LogoutRequest : HomeScreenEvent
    data object RequestProfile : HomeScreenEvent
    data object RequestAdmin : HomeScreenEvent
    data object RequestProductOverview : HomeScreenEvent
    data class RequestProductDetails(val productId: String) : HomeScreenEvent
}
