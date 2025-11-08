package com.sailinghawklabs.burgerrestaurant.feature.auth

// Events to the ViewModel <---- from the Screen
sealed interface AuthScreenEvent {
    data class TitleChanged(val string: String) : AuthScreenEvent
}