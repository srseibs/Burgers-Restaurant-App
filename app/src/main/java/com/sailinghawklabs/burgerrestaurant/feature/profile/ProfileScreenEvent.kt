package com.sailinghawklabs.burgerrestaurant.feature.profile

// Events to the ViewModel <---- from the Screen
sealed interface ProfileScreenEvent {
    data object RequestNavigateBack : ProfileScreenEvent
}