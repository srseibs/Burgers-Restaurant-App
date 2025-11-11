package com.sailinghawklabs.burgerrestaurant.feature.auth

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.component.GoogleButton
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.component.PrimaryButton
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onGotoHomeScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsCommand(viewModel.commandsForScreen) { command ->
        when (command) {
            is AuthScreenCommand.NavigateToMainScreen -> {
                onGotoHomeScreen()
            }
        }
    }

    LaunchedEffect(key1 = state) {
        when (state) {
            AuthState.Success -> {}
            is AuthState.Error -> {}
            AuthState.Idle -> {}
            AuthState.Loading -> {}
        }
    }

    AuthScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        isLoading = state is AuthState.Loading
    )
}

@Composable
fun AuthScreenContent(
    state: AuthState,
    onEvent: (AuthScreenEvent) -> Unit,
    isLoading: Boolean
) {

    val context = LocalContext.current
    val activity = context as Activity

    Scaffold { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = scaffoldPadding)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.burgers),
                    contentDescription = "logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                )
                Text(
                    text = stringResource(R.string.sign_in_text),
                    fontFamily = oswaldVariableFont,
                    fontSize = AppFontSize.MEDIUM
                )
            }
            GoogleButton(
                isLoading = false,
                onClick = {
                    onEvent(AuthScreenEvent.RequestGoogleLogin(activity = activity))
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.guest_text),
                onClick = {
                    onEvent(AuthScreenEvent.RequestGuestLogin)
                }
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        AuthScreenContent(
            state = AuthState.Idle,
            onEvent = {},
            isLoading = false
        )
    }
}
