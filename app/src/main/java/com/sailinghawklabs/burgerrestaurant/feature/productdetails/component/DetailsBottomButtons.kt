package com.sailinghawklabs.burgerrestaurant.feature.productdetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandYellow
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary

@Composable
fun DetailsBottomButtons(
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onBuyNowClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedIconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderIdle)
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Heart),
                contentDescription = "Heart Icon",
                modifier = Modifier.size(24.dp)
            )
        }
        Button(
            onClick = onAddToCartClick,
            modifier = Modifier
                .height(46.dp)
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandYellow,
            )
        ) {
            Text(
                text = "Add to Cart",
                fontWeight = FontWeight.Bold,
                fontSize = AppFontSize.REGULAR,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(Resources.Icon.ShoppingCart),
                contentDescription = "Cart Icon",
                tint = IconPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
        Button(
            onClick = onAddToCartClick,
            modifier = Modifier
                .height(46.dp)
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandYellow,
            )
        ) {
            Text(
                text = "Buy Now",
                fontWeight = FontWeight.Bold,
                fontSize = AppFontSize.REGULAR,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(Resources.Icon.Dollar),
                contentDescription = "Buy Now",
                tint = IconPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsBottomButtonsPrev() {
    BurgerRestaurantTheme {
        DetailsBottomButtons(
            onFavoriteClick = {},
            onAddToCartClick = {},
            onBuyNowClick = {}
        )
    }
}