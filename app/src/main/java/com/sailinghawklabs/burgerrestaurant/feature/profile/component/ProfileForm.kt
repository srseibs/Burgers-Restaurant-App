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
import com.sailinghawklabs.burgerrestaurant.feature.profile.validateCity
import com.sailinghawklabs.burgerrestaurant.feature.profile.validateFirstName
import com.sailinghawklabs.burgerrestaurant.feature.profile.validateLastName
import com.sailinghawklabs.burgerrestaurant.feature.profile.validatePhoneNumber
import com.sailinghawklabs.burgerrestaurant.feature.profile.validatePostalCode
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
            errorMessage = firstName.validateFirstName()
        )
        BurgerTextField(
            label = "Last Name",
            value = lastName,
            onValueChange = onLastNameChange,
            errorMessage = lastName.validateLastName(),
        )
        BurgerTextField(
            label = "Email",
            value = email,
            enabled = false,
            onValueChange = { },
            errorMessage = null,
        )
        BurgerTextField(
            label = "City",
            value = city ?: "",
            onValueChange = onCityChange,
            errorMessage = city?.validateCity(),
        )
        BurgerTextField(
            label = "Postal code",
            value = postalCode ?: "",
            onValueChange = onPostalCodeChange,
            errorMessage = postalCode.validatePostalCode(),
        )
        BurgerTextField(
            label = "Phone number",
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            errorMessage = phoneNumber.validatePhoneNumber(),
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

