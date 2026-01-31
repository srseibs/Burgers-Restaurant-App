package com.sailinghawklabs.burgerrestaurant.feature.productdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.core.data.model.fakeProducts
import com.sailinghawklabs.burgerrestaurant.feature.component.InfoCard
import com.sailinghawklabs.burgerrestaurant.feature.component.LoadingCard
import com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.components.CartItemCard
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.feature.util.DisplayResult
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont

@Composable
fun AddSuggestionsDialog(
    modifier: Modifier = Modifier,
    suggestedProducts: RequestState<List<Product>>,
    addedIds: Set<String>,
    totalPrice: Double,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onProductClick: (productId: String) -> Unit,
    onAddItemClick: (productId: String) -> Unit,
    onRemoveItemClick: (productId: String) -> Unit,
    onCheckoutClick: () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = {
            Text(
                text = "Suggested for you",
                fontFamily = oswaldVariableFont,
                fontSize = AppFontSize.MEDIUM,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            suggestedProducts.DisplayResult(
                onIdle = {
                    Text(
                        modifier = Modifier.alpha(Alpha.HALF),
                        text = "No suggestions yet...",
                        fontFamily = oswaldVariableFont,
                        fontSize = AppFontSize.MEDIUM,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                onError = { message ->
                    InfoCard(
                        image = Resources.Icon.Bell,
                        title = "Oops!",
                        subtitle = message
                    )
                },
                onSuccess = { products ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        products.take(10).forEach { product ->
                            val isChecked = addedIds.contains(product.id)
                            CartItemCard(
                                product = product,
                                isChecked = isChecked,
                                onAddItemClick = { id -> onAddItemClick(id) },
                                onRemoveItemClick = { id -> onRemoveItemClick(id) }
                            )
                        } // https://youtu.be/xPzS0Gih_IU?si=MdR7SRCpn77Wp4Jd&t=6666
                    }
                }
            )
        },
        confirmButton = {},
        dismissButton = {},
    )
}

@Preview
@Composable
private fun AddSuggestionsDialogPrev() {
    BurgerRestaurantTheme {
        AddSuggestionsDialog(
            suggestedProducts = RequestState.Success(fakeProducts.take(5)),
            addedIds = setOf(
                fakeProducts[0].id,
                fakeProducts[2].id
            ),
            totalPrice = 5.67,
            onDismiss = { },
            onConfirm = { },
            onProductClick = { },
            onRemoveItemClick = { },
            onCheckoutClick = { },
            onAddItemClick = {}
        )

    }
}