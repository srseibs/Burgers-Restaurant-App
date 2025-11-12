package com.sailinghawklabs.burgerrestaurant.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.DrawerItem
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextWhite

@Composable
fun DrawerItemCard(
    modifier: Modifier = Modifier,
    drawerItem: DrawerItem,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .width(200.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(100))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = drawerItem.icon),
            contentDescription = "Drawer item icon",
            tint = IconSecondary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = drawerItem.title,
            color = TextWhite,
            fontSize = AppFontSize.EXTRA_REGULAR
        )
    }

}

@Preview
@Composable
private fun DrawerItemCardPrev() {
    BurgerRestaurantTheme {

        DrawerItemCard(
            drawerItem = DrawerItem.Profile,
            onClick = {}
        )
    }
}
