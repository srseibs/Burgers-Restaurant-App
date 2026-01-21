package com.sailinghawklabs.burgerrestaurant.feature.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailsState())
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProductDetailsState()
        )

    private val _commands = Channel<ProductDetailsScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    private fun getProductById(productId: String) {
        viewModelScope.launch {
            productRepository.readProductById(productId).collectLatest { requestState ->
                _state.update {
                    it.copy(product = requestState)
                }
            }
        }
    }

    private fun addToCart() {
    }

    private fun buyNow() {
    }

    private fun toggleFavorite() {

    }

    fun onEvent(event: ProductDetailsScreenEvent) {
        when (event) {
            ProductDetailsScreenEvent.RequestNavigateBack -> {}
            ProductDetailsScreenEvent.RequestAddToCart -> {}


            ProductDetailsScreenEvent.IncrementQuantity -> {
                _state.update {
                    it.copy(quantity = (it.quantity + 1).coerceAtMost(99))
                }
            }

            ProductDetailsScreenEvent.DecrementQuantity -> {
                _state.update {
                    it.copy(quantity = (it.quantity - 1).coerceAtLeast(1))
                }
            }

            ProductDetailsScreenEvent.ToggleFavorite -> {}

            ProductDetailsScreenEvent.RequestBuyNow -> {}

        }
    }
}