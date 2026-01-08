package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
import com.sailinghawklabs.burgerrestaurant.feature.admin.component.ProductCard
import com.sailinghawklabs.burgerrestaurant.feature.admin.component.toCalorieLabel
import com.sailinghawklabs.burgerrestaurant.feature.component.InfoCard
import com.sailinghawklabs.burgerrestaurant.feature.component.LoadingCard
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.components.CategoryChip
import com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.components.MainProductCard
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.feature.util.DisplayResult
import com.sailinghawklabs.burgerrestaurant.feature.util.toCurrencyString
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductOverviewScreen(
    viewModel: ProductOverviewViewModel = koinViewModel(),
    onProductClick: (String) -> Unit,
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


// https://youtu.be/YOye1vyUd04?si=e2V3z37qdGjhEWRZ&t=4333
@Composable
fun ProductOverviewScreenContent(
    state: ProductOverviewState,
    onEvent: (ProductOverviewScreenEvent) -> Unit,
) {
    val heroProduct = state.heroProduct
    val popularProducts = state.popularProducts
    val discountedProducts = state.discountedProducts
    val newProducts = state.newProducts.getSuccessDataOrNull() ?: emptyList()
    val categoryProducts = state.categoryProducts
    val selectedCategory = state.selectedCategory

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Main Product Card
        item {
            AnimatedContent(
                targetState = heroProduct?.id,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 500))
                }
            ) { _ ->
                heroProduct?.let { product ->
                    MainProductCard(
                        title = product.title,
                        calories = product.calories.toCalorieLabel(),
                        price = product.price.toCurrencyString(),
                        imageUrl = product.productImage,
                        paused = state.heroPaused
                    )
                } ?: LoadingCard(modifier = Modifier.fillMaxSize())
            }
        }

        // Category Row
        item { ProductOverviewSectionHeader("Our Menu") }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                items(ProductCategory.entries) { category ->
                    CategoryChip(
                        title = category.title,
                        iconRes = category.icon,
                        onClick = { onEvent(ProductOverviewScreenEvent.CategorySelected(category)) },
                        // isSelected = category == selectedCategory,
                    )
                }
            }
        }

        // Popular Products
        if (selectedCategory != null) {
            item { ProductOverviewSectionHeader(selectedCategory.title) }
            item {
                categoryProducts.DisplayResult(
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
                    onSuccess = { productList ->
                        val products = productList
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                        if (products.isEmpty()) {
                            InfoCard(
                                image = Resources.Icon.Bell,
                                title = "Sorry! Nothing here",
                                subtitle = "No products found for this category"
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            onEvent(
                                                ProductOverviewScreenEvent.ProductClicked(
                                                    product.id
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        } else {
            item { ProductOverviewSectionHeader("Popular Products") }
            item {
                popularProducts.DisplayResult(
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
                    onSuccess = { productList ->
                        val products = productList
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                        if (products.isEmpty()) {
                            InfoCard(
                                image = Resources.Icon.Bell,
                                title = "Sorry! Nothing here",
                                subtitle = "No popular products are found."
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            onEvent(
                                                ProductOverviewScreenEvent.ProductClicked(
                                                    product.id
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
            item { ProductOverviewSectionHeader("Discounted Products") }
            item {
                discountedProducts.DisplayResult(
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
                    onSuccess = { productList ->
                        val products = productList
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                        if (products.isEmpty()) {
                            InfoCard(
                                image = Resources.Icon.Bell,
                                title = "Sorry! Nothing here",
                                subtitle = "No products found for this category"
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            onEvent(
                                                ProductOverviewScreenEvent.ProductClicked(
                                                    product.id
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductOverviewSectionHeader(
    title: String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(Alpha.HALF),
        text = title,
        fontSize = AppFontSize.EXTRA_REGULAR,
        color = TextPrimary,
        textAlign = TextAlign.Center
    )
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