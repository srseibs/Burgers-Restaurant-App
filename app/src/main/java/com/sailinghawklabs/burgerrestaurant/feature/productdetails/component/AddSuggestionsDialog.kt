package com.sailinghawklabs.burgerrestaurant.feature.productdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
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
import com.sailinghawklabs.burgerrestaurant.feature.util.toCurrencyString
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandYellow
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
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
        confirmButton = {
            Button(
                onClick = onCheckoutClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandYellow)
            ) {
                Text(
                    text = "Checkout (${totalPrice.toCurrencyString()})",
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Resources.Icon.ShoppingCart),
                    contentDescription = "Cart Icon",
                    modifier = Modifier.size(16.dp),
                    tint = IconPrimary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Close),
                    contentDescription = "Close Icon",
                    modifier = Modifier.size(16.dp),
                    tint = IconPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Close",
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    fontFamily = oswaldVariableFont
                )
            }
        },
    )
}

// https://youtu.be/xPzS0Gih_IU?si=n_4QtR7_3gAFAzOT&t=6974
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
            onRemoveItemClick = { },
            onCheckoutClick = { },
            onAddItemClick = {}
        )

    }
}