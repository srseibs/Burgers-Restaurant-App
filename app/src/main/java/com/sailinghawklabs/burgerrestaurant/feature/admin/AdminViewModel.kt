package com.sailinghawklabs.burgerrestaurant.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AdminState()
        )

    private val _commands = Channel<AdminScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: AdminScreenEvent) {
        when (event) {
            AdminScreenEvent.RequestNavigateBack -> {
                viewModelScope.launch {
                    _commands.send(AdminScreenCommand.NavigateBack)
                }
            }

            else -> TODO("Handle events")
        }
    }

}