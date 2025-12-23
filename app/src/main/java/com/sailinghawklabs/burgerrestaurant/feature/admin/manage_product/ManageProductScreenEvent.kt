package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import android.net.Uri

// Events to the ViewModel <---- from the Screen
sealed interface ManageProductScreenEvent {
    data class RequestUploadImage(val imageUri: Uri?) : ManageProductScreenEvent
    data object DeleteUploadedImage : ManageProductScreenEvent
    data object RetryImageAccess : ManageProductScreenEvent
}