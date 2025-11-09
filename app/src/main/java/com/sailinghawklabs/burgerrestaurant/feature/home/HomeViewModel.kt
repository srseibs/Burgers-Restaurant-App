package com.sailinghawklabs.burgerrestaurant.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    private val _commands = Channel<HomeScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            else -> TODO("Handle events")
        }
    }

}