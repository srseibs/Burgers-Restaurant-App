package com.sailinghawklabs.burgerrestaurant.feature.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
    private val auth: FirebaseAuth,
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
        _state.value = AuthState.Loading
        viewModelScope.launch {
            val result = customerRepository.createCustomer(user)
            if (result.isSuccess) {
                _state.value = AuthState.Success
                _commands.send(AuthScreenCommand.NavigateToMainScreen)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Could not create customer"
                _state.value = AuthState.Error("vm: $message")
            }
        }
    }

    private fun loginWithGoogle(activity: Activity) {
        _state.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val authResult = googleAuthUiClient.signInWithGoogle(activity)
                val user = authResult.user

                if (user != null) {
                    logUserIntoFirebase(user)
                } else {
                    _state.value = AuthState.Error("vm: Google sign-in failed.")
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "vm: Google sign-in exception.")
            }
        }
    }

    private fun loginAnonymously() {
        _state.value = AuthState.Loading
        viewModelScope.launch {
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