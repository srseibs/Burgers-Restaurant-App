package com.sailinghawklabs.burgerrestaurant.feature.admin

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class AdminState(
    val products: RequestState<List<Product>> = RequestState.Idle,
    val searchQuery: String = ""
)