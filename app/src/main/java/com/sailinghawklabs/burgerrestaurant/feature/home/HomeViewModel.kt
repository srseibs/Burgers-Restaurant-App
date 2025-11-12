package com.sailinghawklabs.burgerrestaurant.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            getCurrentUser()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )


    private fun getCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        _state.update {
            it.copy(
                currentUser = currentUser
            )
        }
    }

    private val _commands = Channel<HomeScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            else -> TODO("Handle events")
        }
    }

}