package com.sailinghawklabs.burgerrestaurant.core.data.domain

import android.net.Uri
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun uploadProductImageToStorage(imageUri: Uri): Result<String>
    suspend fun deleteProductImageFromStorage(downloadUrl: String): Result<Unit>
    suspend fun createNewProduct(product: Product)

    suspend fun updateProductThumbnail(
        productId: String,
        downloadUrl: String
    ): Result<Unit>

    fun readRecentProducts(
        numberOfProducts: Long = 10
    ): Flow<RequestState<List<Product>>>

    suspend fun readProductById(productId: String): RequestState<Product>

}