package com.sailinghawklabs.burgerrestaurant.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.feature.admin.AdminScreenCommand.NavigateToManageProduct
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.FlowPreview
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

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())

    @OptIn(FlowPreview::class)
    val state = _state
        .onStart {
            loadState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AdminState()
        )

    private val _commands = Channel<AdminScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    private fun loadState() {
        viewModelScope.launch {
            val currentQuery = _state.value.searchQuery

            _state.update { adminState ->
                adminState.copy(products = RequestState.Loading)
            }

            delay(1000) // test the loading display

            if (currentQuery.isEmpty()) {
                adminRepository.readRecentProducts()
            } else {
                adminRepository.searchProductByTitle(currentQuery)
            }.collectLatest { productRequestState ->
                _state.update { adminState ->
                    adminState.copy(products = productRequestState)
                }
            }
        }
    }

    fun onEvent(event: AdminScreenEvent) {
        when (event) {
            is AdminScreenEvent.RequestNavigateBack -> {
                viewModelScope.launch {
                    _commands.send(AdminScreenCommand.NavigateBack)
                }
            }

            is AdminScreenEvent.RequestNavigateToManageProduct -> {
                viewModelScope.launch {
                    _commands.send(NavigateToManageProduct(event.productId))
                }
            }

            is AdminScreenEvent.SearchQueryChanged -> {
                _state.update { adminState ->
                    adminState.copy(searchQuery = event.newQuery)
                }
                loadState()
            }
        }
    }

}
