package com.sailinghawklabs.burgerrestaurant.feature.home.domain

import androidx.annotation.DrawableRes
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources

enum class BottomBarDestination(
    @get:DrawableRes val icon: Int,
    val title: String,
    val destination: Destination
) {
    ProductOverviewScreen(
        icon = Resources.Icon.Home,
        title = "Burgers",
        destination = Destination.ProductOverviewScreen
    ),
    CartScreen(
        icon = Resources.Icon.ShoppingCart,
        title = "Burgers",
        destination = Destination.CartScreen
    ),
    NotificationScreen(
        icon = Resources.Icon.Home,
        title = "Burgers",
        destination = Destination.Notifications
    ),
    CategoryScreen(
        icon = Resources.Icon.Home,
        title = "Burgers",
        destination = Destination.Categories
    )
}



