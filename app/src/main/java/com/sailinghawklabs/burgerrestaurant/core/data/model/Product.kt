package com.sailinghawklabs.burgerrestaurant.core.data.model

import androidx.annotation.DrawableRes
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import kotlin.time.Clock.System

data class Product(
    val id: String,
    val createdAt: Long = System.now().toEpochMilliseconds(),
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

val fakeProducts = listOf(
    Product(
        id = "1",
        title = "Classic Burger",
        description = "A delicious classic burger with a juicy beef patty, fresh lettuce, tomato, and our special sauce.",
        category = ProductCategory.Burgers.title,
        allergyAdvice = "Contains gluten, dairy, and eggs.",
        calories = 550,
        ingredients = "Beef patty, brioche bun, lettuce, tomato, onion, pickles, special sauce.",
        price = 8.99,
        productImage = "https://picsum.photos/seed/1/300/300"
    ),
    Product(
        id = "2",
        title = "Chicken Wrap",
        description = "Grilled chicken, crisp lettuce, and tangy caesar dressing wrapped in a soft tortilla.",
        category = ProductCategory.Wraps.title,
        allergyAdvice = "Contains gluten and dairy.",
        calories = 450,
        ingredients = "Grilled chicken breast, flour tortilla, romaine lettuce, caesar dressing, parmesan cheese.",
        price = 7.49,
        productImage = "https://picsum.photos/seed/2/300/300"
    ),
    Product(
        id = "3",
        title = "Crispy Fries",
        description = "Golden brown and perfectly salted, our crispy fries are the perfect side.",
        category = ProductCategory.Fries.title,
        allergyAdvice = "May contain traces of gluten.",
        calories = 300,
        ingredients = "Potatoes, vegetable oil, salt.",
        price = 2.99,
        productImage = "https://picsum.photos/seed/3/300/300"
    ),
    Product(
        id = "4",
        title = "Soda",
        description = "A refreshing can of soda.",
        category = ProductCategory.Drinks.title,
        allergyAdvice = "None.",
        calories = 150,
        ingredients = "Carbonated water, high fructose corn syrup, natural flavors.",
        price = 1.99,
        productImage = "https://picsum.photos/seed/4/300/300"
    ),
    Product(
        id = "5",
        title = "Chocolate Brownie",
        description = "A rich and fudgy chocolate brownie, served warm.",
        category = ProductCategory.Desserts.title,
        allergyAdvice = "Contains gluten, dairy, and eggs.",
        calories = 400,
        ingredients = "Flour, sugar, butter, cocoa powder, eggs, chocolate chips.",
        price = 3.49,
        productImage = "https://picsum.photos/seed/5/300/300"
    )
)
