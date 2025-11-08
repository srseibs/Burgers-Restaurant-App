package com.sailinghawklabs.burgerrestaurant.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthState()
        )

    private val _commands = Channel<AuthScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            else -> TODO("Handle events")
        }
    }

}