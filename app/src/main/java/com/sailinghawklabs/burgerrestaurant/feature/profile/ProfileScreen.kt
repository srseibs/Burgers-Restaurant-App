package com.sailinghawklabs.burgerrestaurant.feature.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.component.ButtonType
import com.sailinghawklabs.burgerrestaurant.feature.component.InfoCard
import com.sailinghawklabs.burgerrestaurant.feature.component.LoadingCard
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.component.PrimaryButton
import com.sailinghawklabs.burgerrestaurant.feature.home.component.dialog.CountryPickerDialog
import com.sailinghawklabs.burgerrestaurant.feature.profile.component.ProfileForm
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current


    ProfileScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        isFormValid = { viewModel.isFormDataValid }
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            ProfileScreenCommand.NavigateToMainScreen -> onNavigateBack()

            is ProfileScreenCommand.ShowToast -> {
                Toast.makeText(context, command.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    state: ProfileState,
    isFormValid: () -> Boolean,
    onEvent: (ProfileScreenEvent) -> Unit,
) {
    val countriesState = state.countries

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontFamily = oswaldVariableFont,
                        fontSize = AppFontSize.LARGE,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ProfileScreenEvent.RequestNavigateBack) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back Arrow",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }

    ) { scaffoldPadding ->
        var selectCountryDialogOpen by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 24.dp)
//                .imePadding()
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectCountryDialogOpen && countriesState.isSuccess()) {
                CountryPickerDialog(
                    countries = countriesState.getSuccessData(),
                    selectedCountry = state.country,
                    onCountrySelected = {
                        onEvent(ProfileScreenEvent.CountryChanged(it))
                        selectCountryDialogOpen = false
                    },
                    onDismiss = { selectCountryDialogOpen = false }
                )
            }

            when (state.screenReady) {
                is RequestState.Loading -> {
                    LoadingCard(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is RequestState.Error -> {
                    InfoCard(
                        modifier = Modifier.fillMaxSize(),
                        image = R.drawable.error_blue,
                        title = "Error",
                        subtitle = state.screenReady.getErrorMessage()
                    )
                }

                is RequestState.Success -> {
                    if (state.profilePictureUrl != null) {
                        AsyncImage(
                            model = "https://yt3.ggpht.com/T-7NRB3hM324iMkEudXqxlcFu2AI_pyLVfR_RMiinQf81ACspB0bdFvwZMlG-9Q5UIBxqINe=s88-c-k-c0x00ffffff-no-rj",
                            contentDescription = "Profile picture",
                            modifier = Modifier
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Profile picture",
                            modifier = Modifier
                        )
                    }
                    ProfileForm(
                        modifier = Modifier.fillMaxWidth(),
                        firstName = state.firstName,
                        onFirstNameChange = { onEvent(ProfileScreenEvent.FirstNameChanged(it)) },
                        lastName = state.lastName,
                        onLastNameChange = { onEvent(ProfileScreenEvent.LastNameChanged(it)) },
                        email = state.email,
                        city = state.city,
                        onCityChange = { onEvent(ProfileScreenEvent.CityChanged(it)) },
                        country = state.country,
                        onCountrySelect = {
                            selectCountryDialogOpen = !selectCountryDialogOpen
                        },
                        postalCode = state.postalCode,
                        address = state.address,
                        onAddressChange = { onEvent(ProfileScreenEvent.AddressChanged(it)) },
                        onPostalCodeChange = { onEvent(ProfileScreenEvent.PostalCodeChanged(it)) },
                        phoneNumber = state.phoneNumber,
                        onPhoneNumberChange = { onEvent(ProfileScreenEvent.PhoneNumberChanged(it)) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        buttonType = ButtonType.PRIMARY,
                        enabled = isFormValid(),
                        text = "Update",
                        icon = painterResource(id = R.drawable.user),
                        onClick = { onEvent(ProfileScreenEvent.Submit) }
                    )
                }

                RequestState.Idle -> {}
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        ProfileScreenContent(
            state = ProfileState(
                screenReady = RequestState.Success(Unit),
                countries = RequestState.Success(emptyList())
            ),
            isFormValid = { true },
            onEvent = {}
        )
    }
}
