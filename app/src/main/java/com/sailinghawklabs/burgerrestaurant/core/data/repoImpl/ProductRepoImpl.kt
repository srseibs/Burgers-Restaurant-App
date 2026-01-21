package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class ProductRepoImpl() : ProductRepository {

    override fun readNewProducts(limit: Long): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            send(RequestState.Loading)
            val database = Firebase.firestore
            database.collection("products")
                .whereEqualTo("new", true)
                .limit(limit)
                .snapshots()
                .collectLatest { snapshots ->
                    val products = snapshots.documents.map { it.toProduct() }
                    send(RequestState.Success(products))
                }

        } catch (e: Exception) {
            send(RequestState.Error(message = "Error reading new products from database: ${e.message}"))
        }
    }

    override fun readDiscountedProducts(limit: Long): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                send(RequestState.Loading)
                val database = Firebase.firestore
                database.collection("products")
                    .whereEqualTo("discounted", true)
                    .limit(limit)
                    .snapshots()
                    .collectLatest { snapshots ->
                        val products = snapshots.documents.map { it.toProduct() }
                        send(RequestState.Success(products))
                    }

            } catch (e: Exception) {
                send(
                    RequestState.Error(
                        message = "Error reading discounted products from database: ${e.message}"
                    )
                )
            }
        }

    override fun readPopularProducts(limit: Long): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            send(RequestState.Loading)
            val database = Firebase.firestore
            database.collection("products")
                .whereEqualTo("popular", true)
                .limit(limit)
                .snapshots()
                .collectLatest { snapshots ->
                    val products = snapshots.documents.map { it.toProduct() }
                    send(RequestState.Success(products))
                }

        } catch (e: Exception) {
            send(
                RequestState.Error(
                    message = "Error reading popular products from database: ${e.message}"
                )
            )
        }
    }

    override fun readProductsByCategory(
        query: String,
        limit: Long
    ): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            send(RequestState.Loading)
            val database = Firebase.firestore
            database.collection("products")
                .whereEqualTo("category", query)
                .limit(limit)
                .snapshots()
                .collectLatest { snapshots ->
                    val products = snapshots.documents.map { it.toProduct() }
                    send(RequestState.Success(products))
                }

        } catch (e: Exception) {
            send(
                RequestState.Error(
                    message = "Error category products from database: ${e.message}"
                )
            )
        }
    }

    override fun readProductById(productId: String): Flow<RequestState<Product>> = channelFlow {
        try {
            send(RequestState.Loading)
            val database = Firebase.firestore
            database.collection("products")
                .document(productId)
                .snapshots()
                .collectLatest { snapshots ->
                    if (!snapshots.exists()) {
                        send(RequestState.Error(message = "Product not found"))
                        return@collectLatest
                    }
                    val product = snapshots
                        .toProduct()
                    send(RequestState.Success(data = product))
                }

        } catch (e: Exception) {
            send(
                RequestState.Error(
                    message = "Error reading product by ID from database: ${e.message}"
                )
            )
        }
    }
}