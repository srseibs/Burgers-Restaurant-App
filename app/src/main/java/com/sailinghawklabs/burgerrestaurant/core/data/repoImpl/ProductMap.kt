package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import com.google.firebase.firestore.DocumentSnapshot
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product

fun DocumentSnapshot.toProduct(): Product {
    return Product(
        id = id,
        title = getString("title").orEmpty(),
        description = getString("description").orEmpty(),
        category = getString("category").orEmpty(),
        allergyAdvice = getString("allergyAdvice").orEmpty(),
        calories = getLong("calories")?.toInt(),
        ingredients = getString("ingredients").orEmpty(),
        price = getDouble("price") ?: 0.0,
        productImage = getString("productImage").orEmpty(),
        isNew = getBoolean("new") ?: false,
        isPopular = getBoolean("popular") ?: false,
        isDiscounted = getBoolean("discounted") ?: false
    )
}