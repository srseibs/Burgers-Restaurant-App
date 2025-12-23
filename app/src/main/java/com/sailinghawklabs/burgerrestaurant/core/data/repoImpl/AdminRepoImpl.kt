package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AdminRepoImpl() : AdminRepository {
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
}