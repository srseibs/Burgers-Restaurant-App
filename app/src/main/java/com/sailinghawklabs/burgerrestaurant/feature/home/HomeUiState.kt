package com.sailinghawklabs.burgerrestaurant.feature.home

data class HomeState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)