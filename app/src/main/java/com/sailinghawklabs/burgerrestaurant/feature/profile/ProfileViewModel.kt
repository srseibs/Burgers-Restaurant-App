package com.sailinghawklabs.burgerrestaurant.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CountryRepository
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.core.data.model.Customer
import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.profile.component.FormValidators
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            loadCountries()
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
            val persistedCountry = currentState.country?.let {
                Country(
                    name = it.name,
                    code = it.code,
                    dialCode = it.dialCode,
                    flagUrl = it.flagUrl
                )
            }
            val requestState = customerRepository.updateCustomer(
                customer = Customer(
                    id = currentState.id,
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    city = currentState.city,
                    address = currentState.address,
                    postalCode = currentState.postalCode,
                    email = currentState.email,
                    phoneNumber = currentState.phoneNumber,
                    country = persistedCountry
                )
            )
            when (requestState) {
                is RequestState.Success -> {
                    _commands.send(ProfileScreenCommand.ShowToast("Update successful"))
                    delay(1000)
                    _commands.send(ProfileScreenCommand.NavigateToMainScreen)
                }

                is RequestState.Error -> {
                    _commands.send(
                        ProfileScreenCommand.ShowToast("Update failed : ${requestState.message}")
                    )
                }

                else -> Unit
            }
        }
    }

    private suspend fun loadCountries() = viewModelScope.launch {
        countryRepository.fetchCountries()
            .onStart { _state.update { it.copy(countries = RequestState.Loading) } }
            .collect { countryRequestState ->
                if (countryRequestState.isSuccess()) {
                    val countries = countryRequestState.getSuccessData()
                    _state.value.phoneNumber?.dialCode?.let { dialCode ->
                        countries.firstOrNull() { it.dialCode == dialCode }?.let { match ->
                            _state.update {
                                it.copy(country = match)
                            }
                        }
                    }
                    _state.update {
                        it.copy(countries = countryRequestState)
                    }
                }
            }
    }


    private suspend fun getCustomer() {
        viewModelScope.launch {

            _state.update { it.copy(screenReady = RequestState.Loading) }

            customerRepository.readCustomerFlow().collectLatest { requestState ->
                when {
                    requestState.isSuccess() -> {
                        val fetchedData = requestState.getSuccessData()
                        val dialCode = fetchedData.phoneNumber?.dialCode


                        val countryRequest = _state.value.countries
                        val countryList = if (countryRequest.isSuccess()) {
                            countryRequest.getSuccessData()
                        } else {
                            emptyList()
                        }

                        val mappedCountry =
                            if (dialCode != null && countryList.isNotEmpty()) {
                                countryList.firstOrNull { it.dialCode == dialCode }
                            } else
                                _state.value.country

                        println("countryList.size: ${countryList.size}")
                        println("mapped country: $mappedCountry")

                        _state.update {
                            it.copy(
                                id = fetchedData.id,
                                firstName = fetchedData.firstName,
                                lastName = fetchedData.lastName,
                                city = fetchedData.city,
                                email = fetchedData.email,
                                phoneNumber = fetchedData.phoneNumber,
                                country = mappedCountry,
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

            is ProfileScreenEvent.CountryChanged -> {
                _state.update {
                    it.copy(
                        country = event.country,
                        phoneNumber =
                            _state.value.phoneNumber?.copy(
                                dialCode = event.country.dialCode
                            ) ?: PhoneNumber(
                                dialCode = event.country.dialCode,
                                number = _state.value.phoneNumber?.number ?: ""
                            )
                    )
                }
            }
        }
    }
}
