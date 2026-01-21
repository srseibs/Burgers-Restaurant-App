package com.sailinghawklabs.burgerrestaurant.feature.productdetails

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
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = viewModel(),
    onGotoMainScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductDetailsScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            ProductDetailsScreenCommand.NavigateToMainScreen -> onGotoMainScreen()
        }
    }
}

@Composable
fun ProductDetailsScreenContent(
    state: ProductDetailsState,
    onEvent: (ProductDetailsScreenEvent) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        ProductDetailsScreenContent(
            state = ProductDetailsState(),
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