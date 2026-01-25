package com.sailinghawklabs.burgerrestaurant.feature.productdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.core.data.model.fakeProducts
import com.sailinghawklabs.burgerrestaurant.feature.util.toCalorieLabel
import com.sailinghawklabs.burgerrestaurant.feature.util.toCurrencyString
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceDark

@Composable
fun ProductDetailsCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    calories: Int?,
    price: Double,
    allergyAdvice: String,
    ingredients: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Resources.Icon.Flame),
                        contentDescription = "Flame Icon",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = calories.toCalorieLabel(),
                        fontSize = AppFontSize.REGULAR,
                    )
                }
                Text(
                    text = price.toCurrencyString(),
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.Bold,
                    color = BrandBrown
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = AppFontSize.REGULAR,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = AppFontSize.REGULAR,
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailsInfoSection(
                title = "Allergy Advice",
                body = allergyAdvice
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailsInfoSection(
                title = "Ingredients",
                body = ingredients
            )
        }
    }
}

@Preview
@Composable
private fun ProductDetailsCardPrev() {
    val product = fakeProducts.first()
    ProductDetailsCard(
        title = product.title,
        description = product.description,
        calories = product.calories,
        price = product.price,
        allergyAdvice = product.allergyAdvice,
        ingredients = product.ingredients
    )

}