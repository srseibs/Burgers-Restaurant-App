package com.sailinghawklabs.burgerrestaurant.core.data.model

import androidx.annotation.DrawableRes
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val allergyAdvice: String,
    val calories: Int?,
    val ingredients: String,
    val price: Double,
    val productImage: String
)

enum class ProductCategory(
    val title: String,
    @field:DrawableRes val icon: Int
) {
    Burgers("Burgers", Resources.Icon.Burger),
    Nuggets("Nuggets", Resources.Icon.Nuggets),
    Wraps("Wraps", Resources.Icon.Wraps),
    Desserts("Desserts", Resources.Icon.Desserts),
    Fries("Fries", Resources.Icon.Fries),
    Sauces("Sauces", Resources.Icon.Sauces),
    Drinks("Drinks", Resources.Icon.Drinks)
}