package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import kotlinx.coroutines.tasks.await

class CustomerRepoImpl() : CustomerRepository {

    override fun getCurrentUserId(): String? =
        FirebaseAuth.getInstance().currentUser?.uid


    override suspend fun createCustomer(user: FirebaseUser): Result<Unit> = runCatching {
        val customerCollection = Firebase.firestore.collection("customer")
        val docRef = customerCollection.document(user.uid)
        val snapshot = docRef.get().await()

        if (!snapshot.exists()) {
            val customer = Customer(
                id = user.uid,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                email = user.email ?: "Unknown"
            )
            docRef.set(customer).await()
        }
    }

}