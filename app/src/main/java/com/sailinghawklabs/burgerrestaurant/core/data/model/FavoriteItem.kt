package com.sailinghawklabs.burgerrestaurant.core.data.model

import kotlin.time.Clock

data class FavoriteItem(
    val productId: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)
