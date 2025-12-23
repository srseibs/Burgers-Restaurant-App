package com.sailinghawklabs.burgerrestaurant.core.data.domain

import android.net.Uri

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun uploadProductImageToStorage(imageUri: Uri): Result<String>
    suspend fun deleteProductImageFromStorage(downloadUrl: String): Result<Unit>

}