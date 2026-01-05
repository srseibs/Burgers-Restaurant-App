package com.sailinghawklabs.burgerrestaurant.core.data.domain

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun readNewProducts(): Flow<RequestState<List<Product>>>
    fun readDiscountedProducts(): Flow<RequestState<List<Product>>>
    fun readPopularProducts(): Flow<RequestState<List<Product>>>
    fun readProductsByCategory(category: String): Flow<RequestState<List<Product>>>

}