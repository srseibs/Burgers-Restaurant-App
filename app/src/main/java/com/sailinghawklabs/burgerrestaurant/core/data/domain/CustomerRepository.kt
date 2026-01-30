package com.sailinghawklabs.burgerrestaurant.core.data.domain

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    fun getCurrentUserId(): String?

    suspend fun createCustomer(user: FirebaseUser): RequestState<Unit>

    fun readCurrentCustomerFlow(): Flow<RequestState<Customer>>

    suspend fun updateCustomer(customer: Customer): RequestState<Unit>

    suspend fun updateProfilePictureUrl(url: String): RequestState<Unit>

    suspend fun updateProfilePhoto(
        localUrl: Uri,
        onProgress: (percent: Float) -> Unit
    ): RequestState<String>

    suspend fun signOut(): RequestState<Unit>

    // Cart functions
    suspend fun addToCart(
        productId: String,
        productTitle: String,
        quantityToAdd: Int
    ): RequestState<Unit>

    suspend fun removeFromCart(
        productId: String,
        quantityToRemove: Int
    ): RequestState<Unit>


    // Favorite functions
    suspend fun toggleFavorite(productId: String): RequestState<Boolean>
    suspend fun isFavorite(productId: String): RequestState<Boolean>
    fun readFavoriteIds(): Flow<RequestState<Set<String>>>

}