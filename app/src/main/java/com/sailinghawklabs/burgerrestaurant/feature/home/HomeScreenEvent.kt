package com.sailinghawklabs.burgerrestaurant.feature.home

// Events to the ViewModel <---- from the Screen
sealed interface HomeScreenEvent {
    data class TitleChanged(val string: String) : HomeScreenEvent
}