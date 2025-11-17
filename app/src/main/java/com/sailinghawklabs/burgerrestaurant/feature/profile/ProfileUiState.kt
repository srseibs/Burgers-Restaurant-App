package com.sailinghawklabs.burgerrestaurant.feature.profile

import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class ProfileState(
    val screenReady: RequestState<Unit> = RequestState.Loading,
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String? = null,
    val address: String? = null,
    val postalCode: Int? = null,
    val phoneNumber: PhoneNumber? = null,
    val profilePictureUrl: String? = null
)

class DataValidator {

}

fun String.validateFirstName(): String? {
    if (isNotEmpty() && length !in 3..50)
        return "Must be 3 to 50 characters"
    return null
}

fun String.validateLastName(): String? {
    if (isNotEmpty() && length !in 3..50)
        return "Must be 3 to 50 characters"
    return null
}

fun String?.validateCity(): String? {
    if (!isNullOrBlank() && length !in 3..50)
        return "Must be 3 to 50 characters"
    return null
}

fun Int?.validatePostalCode(): String? {
    val stringCode = toString()
    if (stringCode.isNotBlank() && stringCode.length !in 3..10)
        return "Must be 3 to 10 characters"
    return null
}

fun String?.validateAddress(): String? {
    if (!isNullOrBlank() && length !in 3..50)
        return "Must be 3 to 50 characters"
    return null
}

fun PhoneNumber?.validatePhoneNumber(): String? {
    if (this != null) {
        val phoneString = this.dialCode.toString() + this.number
        if (phoneString.length !in 8..15)
            return "Must be 8 to 15 characters"
    }
    return null
}


