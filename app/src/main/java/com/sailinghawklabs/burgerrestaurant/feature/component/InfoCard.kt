package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    title: String,
    subtitle: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = image),
            contentDescription = "Info card image",
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontSize = AppFontSize.MEDIUM,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = subtitle,
            fontSize = AppFontSize.REGULAR,
            textAlign = TextAlign.Center
        )

    }
}

@Preview
@Composable
private fun InfoCardPrev() {
    BurgerRestaurantTheme() {
        InfoCard(
            image = R.drawable.ic_launcher_foreground,
            title = "Title",
            subtitle = "Subtitle",
            modifier = Modifier.background(BrandBrown)
        )
    }
}