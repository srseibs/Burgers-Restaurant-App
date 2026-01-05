package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import java.util.UUID
import kotlin.time.Clock

data class ManageProductState(
    val productId: String = UUID.randomUUID().toString(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),

    val imageUploaderState: RequestState<Unit> = RequestState.Idle,
    val productDownloadState: RequestState<Unit> = RequestState.Idle,
    val createProductState: RequestState<Unit> = RequestState.Idle,
    val updateProductState: RequestState<Unit> = RequestState.Idle,
    val deleteProductState: RequestState<Unit> = RequestState.Idle,


    val title: String = "",
    val description: String = "",
    val selectedCategory: ProductCategory? = null,
    val allCategories: List<ProductCategory> = emptyList(),
    val isCategoryDialogOpen: Boolean = false,
    val productImageUri: String = "",
    val calories: Int? = null,
    val allergyAdvice: String = "",
    val ingredients: String = "",
    val price: Double = 0.0,
    val isProductPopular: Boolean = false,
    val isProductNew: Boolean = false,
    val isProductDiscounted: Boolean = false,
)