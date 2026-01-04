package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
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

    override fun readCurrentCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection("customer")
                    .document(userId)
                    .snapshots()
                    .collectLatest { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val postalCode = (documentSnapshot.get("postalCode") as? Long)?.toInt()
                            val phoneNumberMap = documentSnapshot.get("phoneNumber") as? Map<*, *>
                            val phoneNumber = phoneNumberMap?.let {
                                val dialCode = (it["CountryCode"] as? Long)?.toInt()
                                val number = it["Number"] as? String
                                if (dialCode != null && number != null) {
                                    PhoneNumber(dialCode, number)
                                } else {
                                    null
                                }
                            }

                            val countryMap = documentSnapshot.get("country") as? Map<*, *>

                            val countryObject = countryMap?.let { map ->
                                val name = map["name"] as? String
                                val code = map["code"] as? String
                                val dialCode = (map["dialCode"] as? Long)?.toInt()
                                val flagUrl = map["flagUrl"] as? String

                                if (name != null && code != null && dialCode != null && flagUrl != null)
                                    Country(
                                        name = name,
                                        code = code,
                                        dialCode = dialCode,
                                        flagUrl = flagUrl
                                    )
                                else
                                    null
                            }

                            val customer = Customer(
                                id = documentSnapshot.id,
                                firstName = documentSnapshot.get("firstName") as String,
                                lastName = documentSnapshot.get("lastName") as String,
                                email = documentSnapshot.get("email") as String,
                                city = documentSnapshot.get("city") as? String?,
                                postalCode = postalCode,
                                phoneNumber = phoneNumber,
                                address = documentSnapshot.get("address") as String?,
                                country = countryObject,
                                profilePictureUrl = documentSnapshot.get("photoUrl") as String?,
                                isAdmin = documentSnapshot.get("admin") as Boolean
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

    override suspend fun updateProfilePictureUrl(url: String): RequestState<Unit> = try {
        val uid = getCurrentUserId() ?: return RequestState.Error("User not authenticated")

        Firebase.firestore.collection("customer")
            .document(uid)
            .update("photoProfileUrl", url)
            .await()

        try {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val request = userProfileChangeRequest { photoUri = url.toUri() }
                user.updateProfile(request).await()
                user.reload().await()
            }
        } catch (e: Exception) {
            RequestState.Error("Error updating firebase profile photo URL: ${e.message}")
        }

        RequestState.Success(Unit)

    } catch (e: Exception) {
        RequestState.Error("Error updating firestore profile picture URL: ${e.message}")
    }


    override suspend fun updateProfilePhoto(
        localUrl: Uri,
        onProgress: (percent: Float) -> Unit
    ): RequestState<String> = try {
        val uid = getCurrentUserId() ?: return RequestState.Error("User not authenticated")
        val rootStorageRef = FirebaseStorage.getInstance().reference
        val now = System.currentTimeMillis()
        val ref = rootStorageRef.child("user/$uid/profile/pic_{$now}.jpg")
        val metadate = storageMetadata {
            contentType = "image/jpeg"
            setCustomMetadata("uploadedBy", uid)
        }
        val task = ref.putFile(localUrl, metadate)
        task
            .addOnProgressListener { snapshot ->
                val progressPct = if (snapshot.totalByteCount > 0) {
                    snapshot.bytesTransferred.toFloat() / snapshot.totalByteCount.toFloat()
                } else {
                    0f
                }.coerceIn(0f, 100f)
                onProgress(progressPct)
            }
            .await()
        val url = ref.downloadUrl.await().toString()
        RequestState.Success(url)
    } catch (e: Exception) {
        RequestState.Error("Error uploading profile photo: ${e.message}")
    }
}
