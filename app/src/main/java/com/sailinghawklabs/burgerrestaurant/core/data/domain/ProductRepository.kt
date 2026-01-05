package com.sailinghawklabs.burgerrestaurant.core.data.domain

import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun readNewProducts(limit: Long = 5): Flow<RequestState<List<Product>>>
    fun readDiscountedProducts(limit: Long = 5): Flow<RequestState<List<Product>>>
    fun readPopularProducts(limit: Long = 5): Flow<RequestState<List<Product>>>
    fun readProductsByCategory(query: String, limit: Long = 10): Flow<RequestState<List<Product>>>

}