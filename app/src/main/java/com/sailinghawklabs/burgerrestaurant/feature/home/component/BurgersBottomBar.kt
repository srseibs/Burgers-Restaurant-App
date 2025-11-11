package com.sailinghawklabs.burgerrestaurant.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.BottomBarDestination
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight

@Composable
fun BurgersBottomBar(
    modifier: Modifier = Modifier,
    selected: BottomBarDestination,
    onSelect: (BottomBarDestination) -> Unit,
    destinations: List<BottomBarDestination> = BottomBarDestination.entries
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceLight)
            .padding(vertical = 18.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        destinations.forEach { destination ->
            val animatedTint by animateColorAsState(
                targetValue = if (selected == destination) IconSecondary else IconPrimary
            )

            IconButton(
                onClick = { onSelect(destination) },
            ) {
                Icon(
                    painter = painterResource(destination.icon),
                    contentDescription = "Bottom bar icon",
                    tint = animatedTint
                )
            }
        }
    }


}

@Preview
@Composable
private fun BurgersBottomBarPrev() {

    var selected by rememberSaveable { mutableStateOf(BottomBarDestination.CartScreen) }

    BurgerRestaurantTheme {
        BurgersBottomBar(
            selected = selected,
            onSelect = { selected = it }
        )
    }

}
