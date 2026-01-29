package com.sailinghawklabs.burgerrestaurant.feature.productdetails

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class ProductDetailsState(
    val product: RequestState<Product> = RequestState.Loading,
    val quantity: Int = 1,
    val showSuggestedProductsDialog: Boolean = false,
    val suggestedProducts: RequestState<List<Product>> = RequestState.Idle,
    val addedSuggestedIds: Set<String> = emptySet(),
    val isFavorite: Boolean = false,
    val addedCartTotal: Double = 0.0
)
