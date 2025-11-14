package com.sailinghawklabs.burgerrestaurant.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    private val _commands = Channel<ProfileScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.RequestNavigateBack -> {
                viewModelScope.launch {
                    _commands.send(ProfileScreenCommand.NavigateToMainScreen)
                }
            }
//https://youtu.be/wdMTyY_-a34?si=e1K371E9jsWbJBdj&t=2743
        }
    }

}