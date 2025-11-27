package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await

class CustomerRepoImpl() : CustomerRepository {

    override fun getCurrentUserId(): String? = FirebaseAuth.getInstance().currentUser?.uid


    override suspend fun createCustomer(user: FirebaseUser): RequestState<Unit> {
        return try {
            val customerCollection = Firebase.firestore.collection("customer")
            val docRef = customerCollection.document(user.uid)
            val snapshot = docRef.get().await()

            if (!snapshot.exists()) {
                val customer = Customer(
                    id = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown",
                    profilePictureUrl = user.photoUrl.toString()
                )
                docRef.set(customer).await()
            }
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error creating customer: ${e.message}")
        }
    }

    override suspend fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try {
            delay(5000)
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection("customer")
                    .document(userId)
                    .snapshots()
                    .collect { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val postalCode =
                                (documentSnapshot.get("postalCode") as? Long)?.toInt()

                            val phoneNumber =
                                (documentSnapshot.get("phoneNumber") as? Map<*, *>)?.let { map ->
                                    PhoneNumber(
                                        dialCode = (map["CountryCode"] as? Long)?.toInt()
                                            ?: return@let null,
                                        number = map["Number"] as? String ?: return@let null
                                    )
                                }

                            val country =
                                (documentSnapshot.get("country") as? Map<*, *>)?.let { map ->
                                    Country(
                                        name = map["name"] as? String ?: return@let null,
                                        code = map["code"] as? String ?: return@let null,
                                        dialCode = (map["dialCode"] as? Long)?.toInt()
                                            ?: return@let null,
                                        flagUrl = map["flagUrl"] as? String
                                    )
                            }

                            val customer = Customer(
                                id = documentSnapshot.id,
                                firstName = documentSnapshot.get("firstName") as String,
                                lastName = documentSnapshot.get("lastName") as String,
                                email = documentSnapshot.get("email") as String,
                                city = documentSnapshot.get("city") as? String?,
                                address = documentSnapshot.get("address") as String?,
                                profilePictureUrl = documentSnapshot.get("photoUrl") as String?,
                                postalCode = postalCode,
                                phoneNumber = phoneNumber,
                                country = country
                            )
                            send(RequestState.Success(customer))
                        } else {
                            send(RequestState.Error("Customer not found"))
                        }
                    }

            } else {
                send(RequestState.Error("User not authenticated"))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error reading customer information: ${e.message}"))
        }
    }

    override suspend fun updateCustomer(customer: Customer): RequestState<Unit> {
        return try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection("customer")
                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get()
                    .await()
                if (existingCustomer.exists()) {
                    val phoneNumberMap = customer.phoneNumber?.let {
                        mapOf(
                            "CountryCode" to it.dialCode,
                            "Number" to it.number
                        )
                    }

                    val countryMap = customer.country?.let {
                        mapOf(
                            "name" to it.name,
                            "code" to it.code,
                            "dialCode" to it.dialCode,
                            "flagUrl" to it.flagUrl
                        )
                    }
                    customerCollection
                        .document(customer.id)
                        .update(
                            mapOf(
                                "firstName" to customer.firstName,
                                "lastName" to customer.lastName,
                                "city" to customer.city,
                                "postalCode" to customer.postalCode,
                                "address" to customer.address,
                                "phoneNumber" to phoneNumberMap,
                                "country" to countryMap
                            )
                        )
                        .await()
                    RequestState.Success(Unit)
                } else {
                    RequestState.Error("Update failed, customer data not found")
                }
            } else {
                RequestState.Error("User not authenticated")
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating customer: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error while signing out: ${e.message}")
        }
    }
}
