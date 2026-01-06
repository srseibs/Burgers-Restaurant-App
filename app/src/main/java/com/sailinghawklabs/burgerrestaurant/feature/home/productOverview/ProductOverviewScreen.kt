package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
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
fun ProductOverviewScreen(
    viewModel: ProductOverviewViewModel = viewModel(),
    onGotoMainScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductOverviewScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            ProductOverviewScreenCommand.NavigateToMainScreen -> onGotoMainScreen()
        }
    }
}

@Composable
fun ProductOverviewScreenContent(
    state: ProductOverviewState,
    onEvent: (ProductOverviewScreenEvent) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        ProductOverviewScreenContent(
            state = ProductOverviewState(),
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