package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProductOverviewViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {


    private val _state = MutableStateFlow(ProductOverviewState())
    val state = combine(
        getNewProducts(),
        getDiscountedProducts(),
        getPopularProducts(),
        _state
    ) { newProducts, discountedProducts, popularProducts, state ->
        state.copy(
            newProducts = newProducts,
            discountedProducts = discountedProducts,
            popularProducts = popularProducts,
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

    fun onEvent(event: ProductOverviewScreenEvent) {
        when (event) {
            is ProductOverviewScreenEvent.CategorySelected -> {
                _state.update { productOverviewState ->
                    productOverviewState.copy(selectedCategory = event.category)
                }
            }
        }
    }

}
