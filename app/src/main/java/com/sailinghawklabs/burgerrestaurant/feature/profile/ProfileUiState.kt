package com.sailinghawklabs.burgerrestaurant.feature.profile

import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class ProfileState(
    val screenReady: RequestState<Unit> = RequestState.Loading,
    val countries: RequestState<List<Country>> = RequestState.Loading,
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String? = null,
    val address: String? = null,
    val country: Country? = null,
    val postalCode: Int? = null,
    val phoneNumber: PhoneNumber? = null,
    val profilePictureUrl: String? = null
)

