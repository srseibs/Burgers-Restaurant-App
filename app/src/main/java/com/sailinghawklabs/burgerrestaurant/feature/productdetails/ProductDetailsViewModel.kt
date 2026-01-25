package com.sailinghawklabs.burgerrestaurant.feature.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val productId = savedStateHandle.toRoute<Destination.ProductDetailsScreen>().productId
    private val _state = MutableStateFlow(ProductDetailsState())
    val state = _state
        .onStart {
            getProductById()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProductDetailsState()
        )

    private val _commands = Channel<ProductDetailsScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()
    // https://youtu.be/xPzS0Gih_IU?si=3fOR9foYue7SZ488&t=1717

    private fun getProductById() {
        viewModelScope.launch {
            _state.update {
                it.copy(product = RequestState.Loading)
            }
            delay(2000)
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
            ProductDetailsScreenEvent.RequestNavigateBack -> {
                viewModelScope.launch {
                    _commands.send(ProductDetailsScreenCommand.NavigateBack)
                }
            }
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