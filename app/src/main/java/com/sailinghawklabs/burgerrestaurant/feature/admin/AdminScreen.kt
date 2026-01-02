package com.sailinghawklabs.burgerrestaurant.feature.admin

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.sailinghawklabs.burgerrestaurant.feature.admin.component.ProductCard
import com.sailinghawklabs.burgerrestaurant.feature.component.InfoCard
import com.sailinghawklabs.burgerrestaurant.feature.component.LoadingCard
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.util.DisplayResult
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.ButtonPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminScreen(
    viewModel: AdminViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToManageProduct: (String?) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            is AdminScreenCommand.NavigateBack -> onNavigateBack()
            is AdminScreenCommand.NavigateToManageProduct -> onNavigateToManageProduct(command.productId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreenContent(
    state: AdminState,
    onEvent: (AdminScreenEvent) -> Unit,
) {
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Administration",
                        fontFamily = oswaldVariableFont,
                        fontSize = AppFontSize.LARGE,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(AdminScreenEvent.RequestNavigateBack) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back Arrow",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { })
                    {
                        Icon(
                            painter = painterResource(Resources.Icon.Search),
                            contentDescription = "Search",
                            tint = IconPrimary
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AdminScreenEvent.RequestNavigateToManageProduct(null)) },
                containerColor = ButtonPrimary,
                contentColor = IconPrimary
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Plus),
                    contentDescription = "Add"
                )
            }
        }

    ) { scaffoldPadding ->
        state.products.DisplayResult(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = scaffoldPadding.calculateTopPadding(),
                    bottom = scaffoldPadding.calculateBottomPadding()
                ),
            onLoading = {
                LoadingCard(modifier = Modifier.fillMaxSize())
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Icon.Warning,
                    title = "Error",
                    subtitle = message
                )
            },
            onSuccess = { latestProducts ->
                AnimatedContent(
                    targetState = latestProducts
                ) { productList ->
                    if (productList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = latestProducts,
                                key = { it.id }
                            ) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = {
                                        onEvent(
                                            AdminScreenEvent.RequestNavigateToManageProduct(product.id)
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Icon.Warning,
                            title = "Oops!",
                            subtitle = "No products were found."
                        )
                    }
                }
            }
        )
    }
}


@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        AdminScreenContent(
            state = AdminState(),
            onEvent = {}
        )
    }
}
