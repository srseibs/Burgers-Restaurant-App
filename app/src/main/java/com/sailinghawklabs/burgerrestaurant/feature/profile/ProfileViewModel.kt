package com.sailinghawklabs.burgerrestaurant.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.profile.component.FormValidators
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            getCustomer()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    val isFormDataValid: Boolean
        get() {
            val state = _state.value
            return FormValidators.validateFirstName(state.firstName) == null &&
                    FormValidators.validateLastName(state.lastName) == null &&
                    FormValidators.validateCity(state.city) == null &&
                    FormValidators.validatePostalCode(state.postalCode) == null &&
                    FormValidators.validateAddress(state.address) == null &&
                    FormValidators.validatePhoneNumber(state.phoneNumber) == null
        }


    private val _commands = Channel<ProfileScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()


    private fun updateCustomer() {
        viewModelScope.launch {
            val currentState = _state.value // Create a stable snapshot of the state
            val requestState = customerRepository.updateCustomer(
                customer = Customer(
                    id = currentState.id,
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    city = currentState.city,
                    address = currentState.address,
                    postalCode = currentState.postalCode,
                    email = currentState.email,
                    phoneNumber = currentState.phoneNumber
                )
            )
            when (requestState) {
                is RequestState.Success -> {

                }

                is RequestState.Error -> {
//                    _commands.send(ProfileScreenCommand(requestState.message))
                }

                else -> Unit
            }
        }
    }

    private fun getCustomer() {
        viewModelScope.launch {
            customerRepository.readCustomerFlow().collectLatest { requestState ->
                when {
                    requestState.isSuccess() -> {
                        val fetchedData = requestState.getSuccessData()
                        val dialCode = fetchedData.phoneNumber?.dialCode

                        _state.update {
                            it.copy(
                                id = fetchedData.id,
                                firstName = fetchedData.firstName,
                                lastName = fetchedData.lastName,
                                city = fetchedData.city,
                                email = fetchedData.email,
                                phoneNumber = fetchedData.phoneNumber,
                                postalCode = fetchedData.postalCode,
                                address = fetchedData.address,
                                profilePictureUrl = fetchedData.profilePictureUrl,
                                screenReady = RequestState.Success(Unit)

                            )
                        }
                    }

                    requestState.isError() -> {
                        _state.update {
                            it.copy(
                                screenReady = RequestState.Error(requestState.getErrorMessage())
                            )
                        }

                    }

                    else -> Unit
                }

            }
        }
    }


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
                _state.update {
                    it.copy(
                        phoneNumber = PhoneNumber(
                            dialCode = it.phoneNumber?.dialCode ?: 0,
                            number = event.text
                        )
                    )
                }
            }

            is ProfileScreenEvent.PostalCodeChanged -> {
                _state.update { it.copy(postalCode = event.postalCode) }
            }

            is ProfileScreenEvent.AddressChanged -> {
                _state.update { it.copy(address = event.text) }
            }

            ProfileScreenEvent.Submit -> {
                if (isFormDataValid) {
                    updateCustomer()
                }
            }
        }
    }

}
