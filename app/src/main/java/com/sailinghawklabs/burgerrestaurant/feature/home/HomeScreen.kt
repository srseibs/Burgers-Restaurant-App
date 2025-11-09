package com.sailinghawklabs.burgerrestaurant.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onGotoMainScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            HomeScreenCommand.NavigateToMainScreen -> onGotoMainScreen()
        }
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome home ...")
    }

}

@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        HomeScreenContent(
            state = HomeState(),
            onEvent = {}
        )
    }
}

@Composable
fun <T> ObserveAsCommand(flow: Flow<T>, onCommand: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onCommand)
            }
        }
    }
}