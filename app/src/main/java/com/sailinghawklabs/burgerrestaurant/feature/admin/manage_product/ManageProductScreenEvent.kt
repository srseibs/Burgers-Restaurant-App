package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import android.net.Uri
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory

// Events to the ViewModel <---- from the Screen
sealed interface ManageProductScreenEvent {
    data class RequestUploadImage(val imageUri: Uri?) : ManageProductScreenEvent
    data object DeleteUploadedImage : ManageProductScreenEvent
    data object RetryImageAccess : ManageProductScreenEvent

    data object CategoryFieldClicked : ManageProductScreenEvent
    data object CategoryDialogDismissed : ManageProductScreenEvent
    data class CategorySelected(val category: ProductCategory) : ManageProductScreenEvent

    data class UpdateTitle(val newValue: String) : ManageProductScreenEvent
    data class UpdateDescription(val newValue: String) : ManageProductScreenEvent
    data class UpdateCalories(val newValue: Int) : ManageProductScreenEvent
    data class UpdateAllergyAdvice(val newValue: String) : ManageProductScreenEvent
    data class UpdateIngredients(val newValue: String) : ManageProductScreenEvent
    data class UpdatePrice(val newValue: Double) : ManageProductScreenEvent

    data object CreateNewProduct : ManageProductScreenEvent
    data object UpdateExistingProduct : ManageProductScreenEvent
    data object DeleteExistingProduct : ManageProductScreenEvent


}

