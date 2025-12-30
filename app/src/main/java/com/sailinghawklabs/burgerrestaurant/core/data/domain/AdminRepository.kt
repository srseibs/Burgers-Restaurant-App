package com.sailinghawklabs.burgerrestaurant.core.data.domain

import android.net.Uri
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun uploadProductImageToStorage(imageUri: Uri): Result<String>
    suspend fun deleteProductImageFromStorage(downloadUrl: String): Result<Unit>
    suspend fun createNewProduct(product: Product)

    suspend fun updateProductThumbnail(
        productId: String,
        downloadUrl: String
    ): Result<Unit>

}