package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class ProductOverviewState(
    val newProducts: RequestState<List<Product>> = RequestState.Idle,
    val discountedProducts: RequestState<List<Product>> = RequestState.Idle,
    val popularProducts: RequestState<List<Product>> = RequestState.Idle,
    val categoryProducts: RequestState<List<Product>> = RequestState.Idle,


    val heroCandidates: List<Product> = emptyList(),
    val heroIndex: Int = 0,
    val heroProduct: Product? = null,
    val heroPaused: Boolean = true,


    val selectedCategory: ProductCategory? = null

)