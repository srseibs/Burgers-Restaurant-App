package com.sailinghawklabs.burgerrestaurant.feature.home.domain

import androidx.annotation.DrawableRes
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources

enum class BottomBarDestination(
    @get:DrawableRes val icon: Int,
    val title: String,
    val label: String,
    val destination: Destination
) {
    ProductOverviewScreen(
        icon = Resources.Icon.Home,
        title = "Burgers",
        label = "Products",
        destination = Destination.ProductOverviewScreen
    ),
    CartScreen(
        icon = Resources.Icon.ShoppingCart,
        title = "Burgers",
        label = "Cart",
        destination = Destination.CartScreen
    ),
    NotificationScreen(
        icon = Resources.Icon.Bell,
        title = "Burgers",
        label = "Notifications",
        destination = Destination.Notifications
    ),
    CategoryScreen(
        icon = Resources.Icon.Categories,
        title = "Burgers",
        label = "Categories",
        destination = Destination.Categories
    )
}



