package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class ProductOverviewState(
    val newProducts: RequestState<List<Product>> = RequestState.Idle,
    val discountedProducts: RequestState<List<Product>> = RequestState.Idle,
    val popularProducts: RequestState<List<Product>> = RequestState.Idle,

    val selectedCategory: ProductCategory? = null,
    val categoryProducts: RequestState<List<Product>> = RequestState.Idle,

    )