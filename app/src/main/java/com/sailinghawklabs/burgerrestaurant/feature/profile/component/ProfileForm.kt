package com.sailinghawklabs.burgerrestaurant.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.feature.component.BurgerTextField
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
    postalCode: String?,
    onPostalCodeChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BurgerTextField(
            label = "First Name",
            value = firstName,
            onValueChange = onFirstNameChange,
            isError = firstName.isNotEmpty() && firstName.length !in 3..50,
            errorMessage = "Must be 3 to 50 characters",
        )
        BurgerTextField(
            label = "Last Name",
            value = lastName,
            onValueChange = onLastNameChange,
            isError = lastName.isNotEmpty() && lastName.length !in 3..50,
            errorMessage = "Must be 3 to 50 characters",
        )
        BurgerTextField(
            label = "Email",
            value = email,
            enabled = false,
            onValueChange = { },
            isError = false,
            errorMessage = null,
        )
        BurgerTextField(
            label = "City",
            value = city ?: "",
            onValueChange = onCityChange,
            isError = !city.isNullOrBlank() && city.length > 50,
            errorMessage = "Must be less than 50 characters",
        )
        BurgerTextField(
            label = "Postal code",
            value = postalCode ?: "",
            onValueChange = onPostalCodeChange,
            isError = !postalCode.isNullOrBlank() && postalCode.length !in 3..10,
            errorMessage = "Must be 3 to 10 characters",
        )
        BurgerTextField(
            label = "Phone number",
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            isError = phoneNumber.isNotEmpty() && phoneNumber.length !in 8..15,
            errorMessage = "Must be 8 to 15 characters",
        )
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
            postalCode = "12345",
            onPostalCodeChange = {},
            phoneNumber = "123-456-7890",
            onPhoneNumberChange = {},
        )
    }
}

