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

    @Serializable
    data object ProductOverviewScreen : Destination

    @Serializable
    data object ProfileScreen : Destination

    @Serializable
    data object CartScreen : Destination

    @Serializable
    data object Notifications : Destination

    @Serializable
    data object Categories : Destination
}
