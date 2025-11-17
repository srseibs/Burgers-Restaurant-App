package com.sailinghawklabs.burgerrestaurant.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    val isFormValid: Boolean
        get() = with(_state.value) {
            firstName.validateFirstName() == null &&
                    lastName.validateLastName() == null &&
                    city.validateCity() == null &&
                    postalCode.validatePostalCode() == null &&
                    address.validateAddress() == null &&
                    phoneNumber.validatePhoneNumber() == null
        }


    private val _commands = Channel<ProfileScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.RequestNavigateBack -> {
                viewModelScope.launch {
                    _commands.send(ProfileScreenCommand.NavigateToMainScreen)
                }
            }

            is ProfileScreenEvent.CityChanged -> {
                _state.update { it.copy(city = event.text) }
            }

            is ProfileScreenEvent.EmailChanged -> {
                _state.update { it.copy(email = event.text) }
            }

            is ProfileScreenEvent.FirstNameChanged -> {
                _state.update { it.copy(firstName = event.text) }
            }

            is ProfileScreenEvent.LastNameChanged -> {
                _state.update { it.copy(lastName = event.text) }
            }

            is ProfileScreenEvent.PhoneNumberChanged -> {
                //  _state.update { it.copy(phoneNumber = event.text) }
            }

            is ProfileScreenEvent.PostalCodeChanged -> {
                _state.update { it.copy(postalCode = event.text.toIntOrNull()) }
            }

            ProfileScreenEvent.Submit -> {
            }
        }
    }

}
