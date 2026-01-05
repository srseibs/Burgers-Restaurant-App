package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.storage
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AdminRepoImpl() : AdminRepository {

    companion object {
        private const val PREFIX_SEARCH_END_CHAR = "\uf8ff"
    }

    override fun getCurrentUserId() =
        Firebase.auth.currentUser?.uid

    override suspend fun uploadProductImageToStorage(imageUri: Uri): Result<String> {
        val currentUserId = getCurrentUserId()
            ?: return Result.failure(IllegalStateException("No user is authenticated"))

        return try {
            val fileName = UUID.randomUUID().toString()
            val storageRef = Firebase.storage.reference
                .child("users/$currentUserId/products/$fileName")
            storageRef.putFile(imageUri).await()

            val downloadUrl = storageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    override suspend fun deleteProductImageFromStorage(downloadUrl: String): Result<Unit> {
        return try {
            val storagePath = Firebase.storage.getReferenceFromUrl(downloadUrl)
            storagePath.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    override suspend fun createNewProduct(product: Product) {
        Firebase.firestore
            .collection("products")
            .document(product.id)
            .set(product)
            .await()
    }

    override suspend fun updateProductThumbnail(
        productId: String,
        downloadUrl: String
    ): Result<Unit> {
        return try {
            val database = Firebase.firestore
            val productCollection = database.collection("products")
            val productDocRef = productCollection.document(productId)

            productDocRef.update("productImage", downloadUrl).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(
                IllegalStateException(
                    "Error while updating image thumbnail: ${e.message}"
                )
            )
        }
    }

    override fun readRecentProducts(numberOfProducts: Long): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val currentUserId = getCurrentUserId()
                if (currentUserId != null) {
                    val database = Firebase.firestore
                    val productCollection = database.collection("products")
                        .orderBy("createdAt", Query.Direction.DESCENDING)
                        .limit(numberOfProducts)
                        .snapshots()
                        .collectLatest { queryDocumentSnapshots ->
                            val products =
                                queryDocumentSnapshots.documents.mapNotNull { documentSnapshot ->
                                    documentSnapshot.toProduct()
                                }
                            send(RequestState.Success(products))
                        }

                } else {
                    send(RequestState.Error(message = "No user is authenticated"))
                }
            } catch (e: Exception) {
                send(
                    RequestState.Error(
                        message =
                            "Error reading products from database. ${e.message ?: "Unknown Error"}"
                    )
                )
            }
        }

    override suspend fun readProductById(productId: String): RequestState<Product> {
        return try {
            val database = Firebase.firestore
            val productDocRef = database
                .collection("products")
                .document(productId)
                .get()
                .await()
            if (productDocRef.exists()) {
                val product = productDocRef.toProduct()
                RequestState.Success(product)
            } else {
                RequestState.Error(message = "Product not found")
            }

        } catch (e: Exception) {
            RequestState.Error(
                message = "Error reading product from database. ${e.message ?: "Unknown Error"}"
            )
        }
    }

    override suspend fun updateProduct(
        product: Product
    ): Result<Unit> {
        return try {
            val database = Firebase.firestore
            val productCollection = database.collection("products")
            val productDocRef = productCollection.document(product.id)

            productDocRef.set(product).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                IllegalStateException(
                    "Error while updating product: ${e.message}"
                )
            )
        }
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val database = Firebase.firestore
            val productCollection = database.collection("products")
            val productDocRef = productCollection.document(productId)

            productDocRef.delete().await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                IllegalStateException(
                    "Error while deleting product: ${e.message}"
                )
            )
        }
    }

    // https://youtu.be/YOye1vyUd04?si=zjvOkIhNG9DJdNYI&t=588
    override fun searchProductByTitle(
        query: String,
        numberOfProducts: Long
    ): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val database = Firebase.firestore
                val productCollection = database.collection("products")
                if (query.isBlank()) {
                    send(RequestState.Success(emptyList()))
                    return@channelFlow
                }
                productCollection
                    .orderBy("title", Query.Direction.ASCENDING)
                    .startAt(query)
                    .endAt(query + PREFIX_SEARCH_END_CHAR)
                    .limit(numberOfProducts)
                    .snapshots()
                    .collectLatest { queryDocumentSnapshots ->
                        val products = queryDocumentSnapshots.documents
                            .map { documentSnapshot ->
                                documentSnapshot.toProduct()
                            }
                        send(RequestState.Success(products))
                    }

            } catch (e: Exception) {
                send(
                    RequestState.Error(
                        message = "Error searching products from database. ${e.message ?: "Unknown Error"}"
                    )
                )
            }
        }
}