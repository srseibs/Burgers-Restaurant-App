package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.profile.component.FormValidators
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderError
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextSecondary

@Composable
fun BurgerTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    enabled: Boolean = true,
    errorMessage: String? = null,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    val cornerSize = 8.dp
    val supportingText: (@Composable () -> Unit) =
        {
            if (errorMessage != null) {
                Text(text = errorMessage.toString())
            } else if (value.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.check),
                    contentDescription = null,
                    tint = Color(0xFF26884E),
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text("")
            }
        }


    val isError = errorMessage == null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        supportingText = supportingText,
        label = {
            Text(
                text = label ?: "",
                modifier = Modifier
                    .background(SurfaceLight)
            )
        },
        keyboardOptions = keyboardOptions,
        singleLine = !expanded,
        shape = RoundedCornerShape(cornerSize),
        colors = OutlinedTextFieldDefaults.colors(
// containers
            focusedContainerColor = SurfaceLight,
            unfocusedContainerColor = SurfaceLight,
            disabledContainerColor = SurfaceLight,
            errorContainerColor = SurfaceLight,


            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = TextPrimary.copy(Alpha.DISABLED),
            focusedPlaceholderColor = TextPrimary.copy(Alpha.HALF),
            unfocusedPlaceholderColor = TextPrimary.copy(Alpha.HALF),

//border colors
            focusedBorderColor = SurfaceSecondary,
            unfocusedBorderColor = SurfaceSecondary,
            disabledBorderColor = SurfaceSecondary,
            errorBorderColor = BorderError,

            focusedLabelColor = TextSecondary,
            unfocusedLabelColor = TextSecondary,
            disabledLabelColor = TextSecondary,
            errorLabelColor = TextSecondary,

            selectionColors = TextSelectionColors(
                handleColor = IconSecondary,
                backgroundColor = Color.Unspecified
            )
        )
    )
}


@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun BurgerTestFieldPrev() {
    BurgerRestaurantTheme {

        var first by rememberSaveable { mutableStateOf("Mike") }
        var last by rememberSaveable { mutableStateOf("Seibel") }
        var city by rememberSaveable { mutableStateOf("San Ramon") }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            BurgerTextField(
                label = "First Name",
                value = first,
                onValueChange = { first = it },
                errorMessage = FormValidators.validateFirstName(first),
            )

            BurgerTextField(
                label = "Last Name",
                value = last,
                onValueChange = { last = it },
                errorMessage = FormValidators.validateLastName(last)

            )

            BurgerTextField(
                label = "City",
                value = city,
                onValueChange = { city = it },
                errorMessage = FormValidators.validateCity(city)
            )

            BurgerTextField(
                enabled = false,
                label = "Email",
                value = "bob@aol.com",
                onValueChange = { },
                errorMessage = null
            )

        }
    }
}