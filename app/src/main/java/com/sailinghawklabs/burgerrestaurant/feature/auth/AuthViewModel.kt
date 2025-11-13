package com.sailinghawklabs.burgerrestaurant.feature.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sailinghawklabs.burgerrestaurant.core.data.auth.GoogleUiClient
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val customerRepository: CustomerRepository,
    private val googleAuthUiClient: GoogleUiClient
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthState.Idle
        )

    private val _commands = Channel<AuthScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            is AuthScreenEvent.RequestGoogleLogin -> {
                loginWithGoogle(activity = event.activity)
            }

            is AuthScreenEvent.RequestGuestLogin -> {
                loginAnonymously()
            }
        }
    }

    private fun logUserIntoFirebase(user: FirebaseUser) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = customerRepository.createCustomer(user)
            if (result.isSuccess()) {
                _state.value = AuthState.Success
                _commands.send(AuthScreenCommand.NavigateToMainScreen)
            } else {
                val message = result.getErrorMessage()
                _state.value = AuthState.Error("vm: $message")
            }
        }
    }

    private fun loginWithGoogle(activity: Activity) {
        println("loginWithGoogle activity: ${activity.title}")

        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val authResult = googleAuthUiClient.signInWithGoogle(activity)
                val user = authResult.user

                if (user != null) {
                    logUserIntoFirebase(user)
                } else {
                    _state.value = AuthState.Error("vm: Google sign-in failed.")
                }
            } catch (e: Exception) {
                println("loginWithGoogle exception: ${e.message}")
                _state.value = AuthState.Error(e.message ?: "vm: Google sign-in exception.")
            }
        }
    }

    private fun loginAnonymously() {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val authResult = googleAuthUiClient.guestSignIn()
                val user = authResult.user
                if (user != null) {
                    logUserIntoFirebase(user)
                } else {
                    _state.value = AuthState.Error("vm: Guest sign-in failed.")
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "vm: Guest sign-in exception.")
            }
        }
    }


}