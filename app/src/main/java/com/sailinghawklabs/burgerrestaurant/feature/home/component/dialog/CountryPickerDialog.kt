package com.sailinghawklabs.burgerrestaurant.feature.home.component.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.feature.component.BurgerTextField
import com.sailinghawklabs.burgerrestaurant.feature.component.ErrorCard
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconWhite
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceBrand
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceDark
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextBrand
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary

@Composable
fun CountryPickerDialog(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    selectedCountry: Country?,
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var localSelection by remember(key1 = selectedCountry) { mutableStateOf(selectedCountry) }

    val filteredCountries = remember(key1 = searchQuery, key2 = countries) {
        val query = searchQuery.trim().lowercase()
        if (query.isEmpty())
            countries
        else
            countries.filter { country ->
                country.name.lowercase().contains(query) ||
                        "+${country.dialCode}".contains(query) ||
                        country.code.lowercase().contains(query)
            }
    }

    AlertDialog(
        modifier = modifier,
        containerColor = SurfaceLight,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { localSelection?.let(onCountrySelected) },
                enabled = localSelection != null,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TextBrand,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Confirm",
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary.copy(Alpha.HALF)
                )
            ) {
                Text(
                    text = "Cancel",
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        title = {
            Text(
                text = "Select a Country",
                fontSize = AppFontSize.EXTRA_MEDIUM,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth()
            ) {
                BurgerTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = "Dial code or country",
                    showError = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (filteredCountries.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(
                            items = filteredCountries,
                            key = { it.code }
                        ) { country ->
                            CountryPickerDetail(
                                country = country,
                                isSelected = localSelection?.code == country.code,
                                onCountrySelected = { localSelection = country }
                            )
                        }
                    }
                } else {
                    ErrorCard(
                        modifier = Modifier.weight(1f),
                        message = "Dial code not found"
                    )
                }
            }

        }
    )
}

@Composable
fun CountryPickerDetail(
    modifier: Modifier = Modifier,
    country: Country,
    isSelected: Boolean,
    onCountrySelected: () -> Unit
) {

    val saturation = remember { Animatable(initialValue = if (isSelected) 1f else 0f) }
    LaunchedEffect(key1 = isSelected) {
        saturation.animateTo(if (isSelected) 1f else 0f)
    }

    val colorMatrix = remember(key1 = saturation.value) {
        ColorMatrix().apply { setToSaturation(saturation.value) }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCountrySelected() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            model = country.flagUrl,
            colorFilter = ColorFilter.colorMatrix(colorMatrix),
            contentDescription = "${country.name} flag",
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "+${country.dialCode} (${country.name})",
            fontSize = AppFontSize.REGULAR,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ItemSelector(isSelected = isSelected)
    }

}

@Composable
fun ItemSelector(
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    val animatedBackground by animateColorAsState(
        targetValue = if (isSelected) SurfaceBrand else SurfaceDark,
    )

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(color = animatedBackground),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isSelected
        ) {
            Icon(
                modifier = modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Checkmark),
                contentDescription = "Checkmark",
                tint = IconWhite
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun CountryPickerDialogPrev() {
    BurgerRestaurantTheme {
        CountryPickerDialog(
            countries = fakeCountries,
            selectedCountry = fakeCountries[2],
            onDismiss = {},
            onCountrySelected = {}
        )
    }
}


val fakeCountries = listOf(
    Country("US", "United States", 1, "https://flagcdn.com/w320/us.png"),
    Country("CA", "Canada", 1, "https://flagcdn.com/w320/ca.png"),
    Country("MX", "Mexico", 52, "https://flagcdn.com/w320/mx.png"),
)
