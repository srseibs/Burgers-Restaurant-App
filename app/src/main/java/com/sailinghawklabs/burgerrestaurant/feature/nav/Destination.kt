package com.sailinghawklabs.burgerrestaurant.feature.nav

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object SplashScreen : Destination

    @Serializable
    data object AuthScreen : Destination


    @Serializable
    data object HomeGraph : Destination

}