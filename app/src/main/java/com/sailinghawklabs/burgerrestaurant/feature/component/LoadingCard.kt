package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconSecondary

@Composable
fun LoadingCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = IconSecondary,
            strokeWidth = 2.dp
        )
    }
}


@Preview
@Composable
private fun LoadingCardPrev() {
    BurgerRestaurantTheme {
        LoadingCard()

    }
}