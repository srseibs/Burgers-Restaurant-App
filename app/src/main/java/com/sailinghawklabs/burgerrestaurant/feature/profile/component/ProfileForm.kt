package com.sailinghawklabs.burgerrestaurant.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.component.BurgerSelectTextField
import com.sailinghawklabs.burgerrestaurant.feature.component.BurgerTextField
import com.sailinghawklabs.burgerrestaurant.feature.home.component.dialog.fakeCountries
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String?,
    onCityChange: (String) -> Unit,
    country: Country?,
    onCountrySelect: () -> Unit,
    postalCode: Int?,
    onPostalCodeChange: (Int?) -> Unit,
    address: String?,
    onAddressChange: (String) -> Unit,
    phoneNumber: PhoneNumber?,
    onPhoneNumberChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        BurgerTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "First Name",
            value = firstName,
            onValueChange = onFirstNameChange,
            errorMessage = FormValidators.validateFirstName(firstName)
        )
        BurgerTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Last Name",
            value = lastName,
            onValueChange = onLastNameChange,
            errorMessage = FormValidators.validateLastName(lastName),
        )
        BurgerTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Email",
            value = email,
            enabled = false,
            onValueChange = { },
            errorMessage = null,
        )

        BurgerTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Address",
            value = address ?: "",
            onValueChange = onAddressChange,
            errorMessage = FormValidators.validateAddress(address),
        )
        BurgerTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "City",
            value = city ?: "",
            onValueChange = onCityChange,
            errorMessage = FormValidators.validateCity(city),
        )
        BurgerTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "Postal code",
            value = postalCode?.toString() ?: "",
            onValueChange = { onPostalCodeChange(it.toIntOrNull()) },
            errorMessage = FormValidators.validatePostalCode(postalCode),
        )
        BurgerSelectTextField(
            modifier = Modifier.fillMaxWidth(),
            text = country?.name ?: "",
            iconUrl = country?.flagUrl,
            onClick = onCountrySelect,
            placeholder = "Select a country",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BurgerSelectTextField(
                modifier = Modifier
                    .width(120.dp)
                    .padding(bottom = 12.dp),
                text = if (country?.dialCode != null) "+${country.dialCode}" else "",
                iconUrl = country?.flagUrl,
                onClick = onCountrySelect,
                placeholder = "Country",
            )

            Spacer(modifier = Modifier.width(8.dp))

            BurgerTextField(
                modifier = Modifier.weight(1f),
                label = "Phone number",
                value = phoneNumber?.number ?: "",
                onValueChange = onPhoneNumberChange,
                errorMessage = FormValidators.validatePhoneNumber(phoneNumber),
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileFormPrev() {
    BurgerRestaurantTheme {
        ProfileForm(
            firstName = "John",
            onFirstNameChange = {},
            lastName = "Doe",
            onLastNameChange = {},
            email = "john.doe@email.com",
            city = "Anytown",
            onCityChange = {},
            country = fakeCountries[0],
            onCountrySelect = {},
            postalCode = 12345,
            onPostalCodeChange = {},
            phoneNumber = PhoneNumber(
                dialCode = 1,
                number = "555-555-5555"
            ),
            onPhoneNumberChange = {},
            address = "123 Main St",
            onAddressChange = {}
        )
    }
}

