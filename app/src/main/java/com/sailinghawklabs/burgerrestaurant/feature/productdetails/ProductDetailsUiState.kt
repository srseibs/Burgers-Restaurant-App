package com.sailinghawklabs.burgerrestaurant.feature.productdetails

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class ProductDetailsState(
    val product: RequestState<Product> = RequestState.Idle,
    val quantity: Int = 1
)
