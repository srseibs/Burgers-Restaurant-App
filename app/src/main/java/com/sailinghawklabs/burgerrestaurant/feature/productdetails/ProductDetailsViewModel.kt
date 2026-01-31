package com.sailinghawklabs.burgerrestaurant.feature.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId = savedStateHandle.toRoute<Destination.ProductDetailsScreen>().productId
    private val _state = MutableStateFlow(ProductDetailsState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val suggestedProductsFlow: Flow<RequestState<List<Product>>> = _state
        .map { it.showSuggestedProductsDialog }
        .distinctUntilChanged()
        .flatMapLatest { showDialog ->
            if (showDialog) {
                productRepository.readPopularProducts().onStart {
                    emit(RequestState.Loading)
                }
            } else {
                flowOf(RequestState.Idle)
            }
        }
        .onStart { emit(RequestState.Idle) }

    private val favoriteIdsFlow: Flow<RequestState<Set<String>>> =
        customerRepository.readFavoriteIds()
            .onStart { emit(RequestState.Loading) }

    private val productByIdFlow: Flow<RequestState<Product>> =
        productRepository.readProductById(productId)
            .onStart { emit(RequestState.Loading) }

    val state = combine(
        _state,
        productByIdFlow,
        suggestedProductsFlow,
        favoriteIdsFlow
    ) { localState, productById, suggestedProducts, favoriteIdsState ->
        localState.copy(
            product = productById,
            suggestedProducts = suggestedProducts,
            isFavorite = favoriteIdsState.getSuccessDataOrNull()?.contains(productId) ?: false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ProductDetailsState()
    )

    private val _commands = Channel<ProductDetailsScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    private fun addToCart() {
        val quantity = state.value.quantity
        val product = state.value.product.getSuccessDataOrNull() ?: return

        viewModelScope.launch {
            when (
                customerRepository.addToCart(
                    productId = product.id,
                    productTitle = product.title,
                    quantityToAdd = quantity
                )
            ) {
                is RequestState.Success -> {
                    val itemTotal = product.price * quantity
                    _state.update {
                        it.copy(
                            addedCartTotal = it.addedCartTotal + itemTotal,
                            showSuggestedProductsDialog = true
                        )
                    }
                }
                is RequestState.Error -> {
                    _commands.send(
                        ProductDetailsScreenCommand.ShowMessage(
                            message = "Error adding item to cart"
                        )
                    )
                }
                else -> Unit
            }
        }
    }

    private fun addSuggestedItemToCart(productId: String, quantityToAdd: Int = 1) {
        // don't add duplicates to cart
        if (state.value.addedSuggestedIds.contains(productId)) return

        val suggestedProduct = state.value.suggestedProducts
            .getSuccessDataOrNull()
            ?.find { it.id == productId } ?: return // exit if this productId doesn't exist
        val productTitle = suggestedProduct.title
        val productPrice = suggestedProduct.price

        viewModelScope.launch {
            when (
                customerRepository.addToCart(
                    productId = productId,
                    productTitle = productTitle,
                    quantityToAdd = quantityToAdd
                )
            ) {
                is RequestState.Success -> {
                    val itemTotal = productPrice * quantityToAdd
                    _state.update {
                        it.copy(
                            addedSuggestedIds = it.addedSuggestedIds + productId,
                            addedCartTotal = it.addedCartTotal + itemTotal,
                            showSuggestedProductsDialog = true
                        )
                    }
                }
                is RequestState.Error -> {
                    _commands.send(
                        ProductDetailsScreenCommand.ShowMessage(
                            message = "Error adding Suggested Item to cart"
                        )
                    )
                }
                else -> Unit
            }
        }
    }

    private fun removeSuggestedItemFromCart(productId: String, quantityToRemove: Int = 1) {
        val isProductInCart = state.value.addedSuggestedIds.contains(productId)
        if (!isProductInCart) return
        val suggestedProduct = state.value.suggestedProducts
            .getSuccessDataOrNull()
            ?.find { it.id == productId } ?: return // exit if this productId doesn't exist
        val productTitle = suggestedProduct.title
        val productPrice = suggestedProduct.price

        viewModelScope.launch {
            when (
                customerRepository.removeFromCart(
                    productId = productId,
                    quantityToRemove = quantityToRemove
                )
            ) {
                is RequestState.Success -> {
                    _state.update {
                        it.copy(
                            addedSuggestedIds = it.addedSuggestedIds - productId,
                            addedCartTotal =
                                (it.addedCartTotal - (productPrice * quantityToRemove))
                                    .coerceAtLeast(0.0)
                        )
                    }
                }
                is RequestState.Error -> {
                    _commands.send(
                        ProductDetailsScreenCommand.ShowMessage(
                            message = "Error removing Suggested Item from cart"
                        )
                    )
                }
                else -> Unit
            }
        }
    }

    private fun buyNow() {
    }

    private fun toggleFavorite() {
        val product = state.value.product.getSuccessDataOrNull() ?: return
        viewModelScope.launch {
            when (customerRepository.toggleFavorite(productId = product.id)) {
                is RequestState.Error -> {
                    _commands.send(
                        ProductDetailsScreenCommand.ShowMessage(
                            message = "Error updating favorite"
                        )
                    )
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: ProductDetailsScreenEvent) {
        when (event) {
            ProductDetailsScreenEvent.RequestNavigateBack -> {
                viewModelScope.launch {
                    _commands.send(ProductDetailsScreenCommand.NavigateBack)
                }
            }

            is ProductDetailsScreenEvent.RequestAddToCart -> {
                addToCart()
            }

            is ProductDetailsScreenEvent.RequestRemoveFromCart -> {

            }

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

            is ProductDetailsScreenEvent.AddSuggestedItemToCart ->
                addSuggestedItemToCart(event.productId)


            is ProductDetailsScreenEvent.RemoveSuggestedItemFromCart ->
                removeSuggestedItemFromCart(event.productId)


            ProductDetailsScreenEvent.ToggleFavorite ->
                toggleFavorite()

            ProductDetailsScreenEvent.RequestBuyNow ->
                buyNow()

            ProductDetailsScreenEvent.DismissSuggestedProductsDialog -> {
                _state.update {
                    it.copy(
                        addedCartTotal = 0.0,
                        addedSuggestedIds = emptySet(),
                        showSuggestedProductsDialog = false
                    )
                }
            }
        }
    }
}
