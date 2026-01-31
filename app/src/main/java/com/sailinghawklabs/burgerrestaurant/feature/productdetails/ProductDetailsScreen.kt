package com.sailinghawklabs.burgerrestaurant.feature.productdetails

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sailinghawklabs.burgerrestaurant.core.data.model.fakeProducts
import com.sailinghawklabs.burgerrestaurant.feature.component.InfoCard
import com.sailinghawklabs.burgerrestaurant.feature.component.LoadingCard
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.component.PrimaryButton
import com.sailinghawklabs.burgerrestaurant.feature.productdetails.component.AddSuggestionsDialog
import com.sailinghawklabs.burgerrestaurant.feature.productdetails.component.DetailsBottomButtons
import com.sailinghawklabs.burgerrestaurant.feature.productdetails.component.ProductDetailsCard
import com.sailinghawklabs.burgerrestaurant.feature.productdetails.component.QuantityStepper
import com.sailinghawklabs.burgerrestaurant.feature.util.DisplayResult
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = koinViewModel(),
    onGotoMainScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProductDetailsScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            ProductDetailsScreenCommand.NavigateBack -> onGotoMainScreen()
            is ProductDetailsScreenCommand.ShowMessage -> {
                // TODO: Show a Snackbar with the message
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreenContent(
    state: ProductDetailsState,
    onEvent: (ProductDetailsScreenEvent) -> Unit,
) {
    val productState = state.product
    val context = LocalContext.current

    if (state.showSuggestedProductsDialog) {
        println("addedIds: ${state.addedSuggestedIds}")
        AddSuggestionsDialog(
            suggestedProducts = state.suggestedProducts,
            totalPrice = state.addedCartTotal,
            addedIds = state.addedSuggestedIds,
            onDismiss = { onEvent(ProductDetailsScreenEvent.DismissSuggestedProductsDialog) },
            onAddItemClick = { productId ->
                onEvent(ProductDetailsScreenEvent.AddSuggestedItemToCart(productId))
            },
            onRemoveItemClick = { productId ->
                onEvent(ProductDetailsScreenEvent.RemoveSuggestedItemFromCart(productId))
            },
            onCheckoutClick = { }
        )

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
                        fontFamily = oswaldVariableFont,
                        fontSize = AppFontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(ProductDetailsScreenEvent.RequestNavigateBack) }
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                actions = {
                    QuantityStepper(
                        quantity = state.quantity,
                        onIncrement = { onEvent(ProductDetailsScreenEvent.IncrementQuantity) },
                        onDecrement = { onEvent(ProductDetailsScreenEvent.DecrementQuantity) }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentAlignment = Alignment.Center
        ) {
            productState.DisplayResult(
                onLoading = {
                    LoadingCard(modifier = Modifier.fillMaxSize())
                },
                onError = { message ->
                    InfoCard(
                        image = Resources.Icon.Bell,
                        title = "Oops!",
                        subtitle = message
                    )
                },
                onSuccess = { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(product.productImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Product Image",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .height(250.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp,
                                    color = BorderIdle,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                ProductDetailsCard(
                                    title = product.title,
                                    description = product.description,
                                    calories = product.calories,
                                    price = product.price,
                                    allergyAdvice = product.allergyAdvice,
                                    ingredients = product.ingredients
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                DetailsBottomButtons(
                                    isFavorite = state.isFavorite,
                                    onFavoriteClick = {
                                        onEvent(ProductDetailsScreenEvent.ToggleFavorite)
                                    },
                                    onAddToCartClick = {
                                        onEvent(ProductDetailsScreenEvent.RequestAddToCart(product.id))
                                    },
                                    onBuyNowClick = {
                                        onEvent(ProductDetailsScreenEvent.RequestBuyNow)
                                    }
                                )
                            }
                        }
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            icon = painterResource(Resources.Icon.Book),
                            text = "Browse for More",
                            enabled = true,
                            onClick = {}
                        )
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        ProductDetailsScreenContent(
            state = ProductDetailsState(
                product = RequestState.Success(fakeProducts.first())
            ),
            onEvent = {}
        )
    }
}
