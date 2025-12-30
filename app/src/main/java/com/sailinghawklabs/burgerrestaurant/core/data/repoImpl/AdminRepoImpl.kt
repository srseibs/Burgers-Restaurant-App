package com.sailinghawklabs.burgerrestaurant.core.data.repoImpl

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AdminRepoImpl() : AdminRepository {


    private fun DocumentSnapshot.toProduct(): Product {
        return Product(
            id = id,
            title = getString("title").orEmpty(),
            description = getString("description").orEmpty(),
            category = getString("category").orEmpty(),
            allergyAdvice = getString("allergyAdvice").orEmpty(),
            calories = getLong("calories")?.toInt(),
            ingredients = getString("ingredients").orEmpty(),
            price = getDouble("price") ?: 0.0,
            productImage = getString("productImage").orEmpty()
        )
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



}