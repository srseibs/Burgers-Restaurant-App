package com.sailinghawklabs.burgerrestaurant.feature.admin

data class AdminState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)