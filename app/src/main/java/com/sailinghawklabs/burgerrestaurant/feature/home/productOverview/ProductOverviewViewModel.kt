package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ProductOverviewViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {


    private val _state = MutableStateFlow(ProductOverviewState())

    // This flow reacts to category changes and triggers the repository call.
    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryProducts = _state.map { it.selectedCategory?.title ?: "" }
        .distinctUntilChanged()
        .flatMapLatest { categoryTitle ->
            getProductsByCategory(categoryTitle)
        }

    val state = combine(
        getNewProducts(),
        getDiscountedProducts(),
        getPopularProducts(),
        categoryProducts,
        _state
    ) { newProducts, discountedProducts, popularProducts, categoryProductsResult, state ->

        val heroCandidatesFromNewProducts = newProducts.getSuccessDataOrNull()
            ?.sortedByDescending { it.createdAt }
            ?.take(3)
            ?: emptyList()

        val heroProductNew = heroCandidatesFromNewProducts.getOrNull(index = state.heroIndex)

        state.copy(
            newProducts = newProducts,
            discountedProducts = discountedProducts,
            popularProducts = popularProducts,
            heroCandidates = heroCandidatesFromNewProducts,
            categoryProducts = categoryProductsResult, // Use the result from the reactive flow
            heroProduct = heroProductNew,
            heroPaused = state.selectedCategory != null
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ProductOverviewState(
            newProducts = RequestState.Loading,
            discountedProducts = RequestState.Loading,
            popularProducts = RequestState.Loading
        )
    )

    // this causes the heroIndex to slowly cycle through the candidates, unless paused
    init {
        viewModelScope.launch {
            // Create a flow that only emits when the inputs to our animation logic change.
            state.map { it.heroCandidates to it.heroPaused } // creates Pair(List<Product>, Boolean)
                .distinctUntilChanged()
                .collectLatest { (candidates, paused) ->
                    // This block is re-executed when candidates or paused changes.
                    // collectLatest ensures any previous loop is cancelled.
                    if (paused || candidates.size <= 1) {
                        return@collectLatest // Animation is paused, so do nothing.
                    }

                    // Animation is active, start the loop.
                    while (isActive) {
                        delay(5000)
                        _state.update {
                            val nextIndex = (it.heroIndex + 1) % candidates.size
                            it.copy(heroIndex = nextIndex)
                        }
                    }
                }
        }
    }


    private val _commands = Channel<ProductOverviewScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    private fun getNewProducts(): Flow<RequestState<List<Product>>> {
        return productRepository.readNewProducts()
    }

    private fun getDiscountedProducts(): Flow<RequestState<List<Product>>> {
        return productRepository.readDiscountedProducts()
    }

    private fun getPopularProducts(): Flow<RequestState<List<Product>>> {
        return productRepository.readPopularProducts()
    }

    private fun getProductsByCategory(query: String): Flow<RequestState<List<Product>>> {
        return productRepository.readProductsByCategory(query)
    }

    fun onEvent(event: ProductOverviewScreenEvent) {
        when (event) {
            is ProductOverviewScreenEvent.CategorySelected -> {
                _state.update { productOverviewState ->
                    productOverviewState.copy(selectedCategory = event.category)
                }
            }

            is ProductOverviewScreenEvent.ProductClicked -> {

            }
        }
    }

}
