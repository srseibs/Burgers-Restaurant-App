package com.sailinghawklabs.burgerrestaurant.feature.productdetails

// Events to the ViewModel <---- from the Screen
sealed interface ProductDetailsScreenEvent {
    data object IncrementQuantity : ProductDetailsScreenEvent
    data object DecrementQuantity : ProductDetailsScreenEvent
    data object ToggleFavorite : ProductDetailsScreenEvent

    data class RequestAddToCart(val productId: String) : ProductDetailsScreenEvent
    data class RequestRemoveFromCart(val productId: String) : ProductDetailsScreenEvent

    data class AddSuggestedItemToCart(val productId: String) : ProductDetailsScreenEvent
    data class RemoveSuggestedItemFromCart(val productId: String) : ProductDetailsScreenEvent

    data object RequestBuyNow : ProductDetailsScreenEvent
    data object RequestNavigateBack : ProductDetailsScreenEvent
    data object DismissSuggestedProductsDialog : ProductDetailsScreenEvent
}
