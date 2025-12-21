package com.sailinghawklabs.burgerrestaurant.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

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

    private fun logout() {
        viewModelScope.launch {
            val result = customerRepository.signOut()

            if (result.isSuccess()) {
                _commands.send(HomeScreenCommand.ExitDueToUserSignedOff)
            } else if (result.isError()) {
                _commands.send(
                    HomeScreenCommand.ShowErrorMessage(result.getErrorMessage())
                )
            }
        }
    }

    private val _commands = Channel<HomeScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.LogoutRequest -> {
                logout()
            }

            is HomeScreenEvent.RequestProfile -> {
                viewModelScope.launch {
                    _commands.send(HomeScreenCommand.NavigateToProfile)
                }
            }

            is HomeScreenEvent.RequestAdmin -> {
                viewModelScope.launch {
                    _commands.send(HomeScreenCommand.NavigateToAdmin)
                }
            }

        }
    }

}