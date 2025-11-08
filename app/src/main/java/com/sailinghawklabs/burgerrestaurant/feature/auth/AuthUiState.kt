package com.sailinghawklabs.burgerrestaurant.feature.auth

data class AuthState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)