package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory

// Events to the ViewModel <---- from the Screen
sealed interface ProductOverviewScreenEvent {
    data class CategorySelected(val category: ProductCategory?) : ProductOverviewScreenEvent
    data class ProductClicked(val productId: String) : ProductOverviewScreenEvent
}