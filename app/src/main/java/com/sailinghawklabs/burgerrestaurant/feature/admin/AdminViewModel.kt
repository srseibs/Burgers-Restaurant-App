package com.sailinghawklabs.burgerrestaurant.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
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

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
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
            _state.update { adminState ->
                adminState.copy(products = RequestState.Loading)
            }

            delay(1000) // test the loading display

            adminRepository.readRecentProducts().collectLatest { productRequestState ->
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
                    _commands.send(AdminScreenCommand.NavigateToManageProduct(event.productId))
                }
            }


        }
    }

}
