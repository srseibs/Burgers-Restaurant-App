package com.sailinghawklabs.burgerrestaurant.core.data.domain

import com.google.firebase.auth.FirebaseUser
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

interface CustomerRepository {

    fun getCurrentUserId(): String?

    suspend fun createCustomer(user: FirebaseUser): Result<Unit>

    suspend fun signOut(): RequestState<Unit>

}