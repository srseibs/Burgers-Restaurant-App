package com.sailinghawklabs.burgerrestaurant.feature.home.domain

enum class CustomDrawerState {
    Opened,
    Closed
}

fun CustomDrawerState.isOpen() = this == CustomDrawerState.Opened

fun CustomDrawerState.reverse(): CustomDrawerState {
    return when (this) {
        CustomDrawerState.Opened -> CustomDrawerState.Closed
        CustomDrawerState.Closed -> CustomDrawerState.Opened
    }
}
