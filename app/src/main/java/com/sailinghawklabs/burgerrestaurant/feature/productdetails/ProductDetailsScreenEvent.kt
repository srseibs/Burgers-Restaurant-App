package com.sailinghawklabs.burgerrestaurant.feature.productdetails

// Events to the ViewModel <---- from the Screen
sealed interface ProductDetailsScreenEvent {
    data object IncrementQuantity : ProductDetailsScreenEvent
    data object DecrementQuantity : ProductDetailsScreenEvent
    data object ToggleFavorite : ProductDetailsScreenEvent
    data object RequestAddToCart : ProductDetailsScreenEvent
    data object RequestBuyNow : ProductDetailsScreenEvent
    data object RequestNavigateBack : ProductDetailsScreenEvent
}
