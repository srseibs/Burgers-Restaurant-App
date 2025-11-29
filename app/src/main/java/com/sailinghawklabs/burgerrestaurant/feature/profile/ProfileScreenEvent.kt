package com.sailinghawklabs.burgerrestaurant.feature.profile

import com.sailinghawklabs.burgerrestaurant.core.data.model.Country

// Events to the ViewModel <---- from the Screen
sealed interface ProfileScreenEvent {
    data object RequestNavigateBack : ProfileScreenEvent
    data class FirstNameChanged(val text: String) : ProfileScreenEvent
    data class LastNameChanged(val text: String) : ProfileScreenEvent
    data class EmailChanged(val text: String) : ProfileScreenEvent
    data class CityChanged(val text: String) : ProfileScreenEvent
    data class PostalCodeChanged(val postalCode: Int?) : ProfileScreenEvent
    data class AddressChanged(val text: String) : ProfileScreenEvent
    data class PhoneNumberChanged(val text: String) : ProfileScreenEvent
    data class CountryChanged(val country: Country) : ProfileScreenEvent
    data object Submit : ProfileScreenEvent

}