package com.sailinghawklabs.burgerrestaurant.core.data.model

import kotlin.time.Clock


data class CartItem(
    val productId: String,
    val quantity: Int,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
)

