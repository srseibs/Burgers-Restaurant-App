package com.sailinghawklabs.burgerrestaurant.feature.profile

// Events to the ViewModel <---- from the Screen
sealed interface ProfileScreenEvent {
    data object RequestNavigateBack : ProfileScreenEvent
    data class FirstNameChanged(val text: String) : ProfileScreenEvent
    data class LastNameChanged(val text: String) : ProfileScreenEvent
    data class EmailChanged(val text: String) : ProfileScreenEvent
    data class CityChanged(val text: String) : ProfileScreenEvent
    data class PostalCodeChanged(val text: String) : ProfileScreenEvent
    data class PhoneNumberChanged(val text: String) : ProfileScreenEvent
    data object Submit : ProfileScreenEvent
}