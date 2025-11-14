package com.sailinghawklabs.burgerrestaurant.feature.profile

data class ProfileState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)