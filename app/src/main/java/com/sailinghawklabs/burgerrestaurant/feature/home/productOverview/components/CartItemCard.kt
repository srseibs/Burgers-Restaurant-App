package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.core.data.model.fakeProducts
import com.sailinghawklabs.burgerrestaurant.feature.admin.component.PreviewableAsyncImage
import com.sailinghawklabs.burgerrestaurant.feature.util.toCalorieLabel
import com.sailinghawklabs.burgerrestaurant.feature.util.toCurrencyString
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    isChecked: Boolean,
    onAddItemClick: (productId: String) -> Unit,
    onRemoveItemClick: (productId: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape((12.dp)))
            .border(
                width = 1.dp,
                color = BorderIdle,
                shape = RoundedCornerShape(12.dp)
            )
            .background(SurfaceLight)
    ) {
        PreviewableAsyncImage(
            modifier = Modifier
                .height(50.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                ),
            model = ImageRequest.Builder(context = LocalPlatformContext.current)
                .data(product.productImage)
                .crossfade(true)
                .build(),
            previewImage = R.drawable.burger,
            previewTint = Color.Unspecified,
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Resources.Icon.Burger)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = product.title.uppercase(),
                fontFamily = oswaldVariableFont,
                fontSize = AppFontSize.MEDIUM,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(Resources.Icon.Flame),
                        contentDescription = "Flame Icon",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.calories.toCalorieLabel(),
                        fontSize = AppFontSize.REGULAR,
                        color = TextPrimary,
                        fontFamily = oswaldVariableFont
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.price.toCurrencyString(),
                    fontSize = AppFontSize.REGULAR,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(20.dp))

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { nowChecked ->
                        when {
                            nowChecked && !isChecked -> onAddItemClick(product.id)
                            !nowChecked && isChecked -> onRemoveItemClick(product.id)
                        }
                    }
                )
            }

        }
    }
}


@Preview
@Composable
private fun CartItemCardPrev() {
    BurgerRestaurantTheme {
        CartItemCard(
            product = fakeProducts.first(),
            isChecked = true,
            onAddItemClick = {},
            onRemoveItemClick = {}
        )
    }
}




