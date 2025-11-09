package com.sailinghawklabs.burgerrestaurant.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val city: String? = null,
    val postalCode: Int? = null,
    val phoneNumber: PhoneNumber? = null,
    val isAdmin: Boolean = false
)

@Serializable
data class PhoneNumber(
    val dialCode: Int,
    val number: String
)


//https://youtu.be/p3DdiyKiQ_I?si=JDOWdlQF8KtDojZj&t=3817