package com.sailinghawklabs.burgerrestaurant.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.profile.component.ProfileForm
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

    ProfileScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            ProfileScreenCommand.NavigateToMainScreen -> onNavigateBack()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onEvent: (ProfileScreenEvent) -> Unit,
) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(horizontal = 24.dp)
                .imePadding()
        ) {
            ProfileForm(
                firstName = state.firstName,
                onFirstNameChange = { onEvent(ProfileScreenEvent.FirstNameChanged(it)) },
                lastName = state.lastName,
                onLastNameChange = { onEvent(ProfileScreenEvent.LastNameChanged(it)) },
                email = state.email,
                city = state.city,
                onCityChange = { onEvent(ProfileScreenEvent.CityChanged(it)) },
                postalCode = state.postalCode,
                onPostalCodeChange = { onEvent(ProfileScreenEvent.PostalCodeChanged(it)) },
                phoneNumber = state.phoneNumber,
                onPhoneNumberChange = { onEvent(ProfileScreenEvent.PhoneNumberChanged(it)) },
            )

        }
    }
}

@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        ProfileScreenContent(
            state = ProfileState(),
            onEvent = {}
        )
    }
}
