package com.sailinghawklabs.burgerrestaurant.core.data.domain

import com.google.firebase.auth.FirebaseUser
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    fun getCurrentUserId(): String?

    suspend fun createCustomer(user: FirebaseUser): RequestState<Unit>

    suspend fun readCustomerFlow(): Flow<RequestState<Customer>>

    suspend fun updateCustomer(customer: Customer): RequestState<Unit>

    suspend fun signOut(): RequestState<Unit>

}