package com.sailinghawklabs.burgerrestaurant.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.DrawerItem
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextBrand
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont

@Composable
fun CustomDrawer(
    modifier: Modifier = Modifier,
    photoUrl: String? = null,
    displayName: String? = null,
    onProfileClick: () -> Unit = {},
    onLocationsClick: () -> Unit = {},
    onRewardsClick: () -> Unit = {},
    onOffersClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onAdminClick: () -> Unit = {},
    isAdmin: Boolean = false
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.Top)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        val imageModel = if (LocalInspectionMode.current) null else photoUrl

        AsyncImage(
            model = imageModel,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            fallback = painterResource(R.drawable.user)
        )

//        SubcomposeAsyncImage(
//            model = photoUrl,
//            contentDescription = "Profile photo",
//            modifier = Modifier
//                .size(120.dp)
//                .clip(CircleShape),
//
//            contentScale = ContentScale.Fit,
//            error = {
//                if (LocalInspectionMode.current) {
//                    Image(
//                        painter = painterResource(R.drawable.burger),
//                        contentDescription = "Dummy image",
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .size(120.dp)
//                            .clip(CircleShape)
//                    )
//                } else {
//                    Text("Error loading image")
//                }
//            }
//        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Welcome ${displayName?.split(" ")?.firstOrNull() ?: "User"}",
            fontFamily = oswaldVariableFont,
            fontSize = AppFontSize.EXTRA_REGULAR,
            fontWeight = FontWeight.Medium,
            color = TextBrand
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(vertical = 10.dp),
            thickness = 2.dp,
            color = Color.Black
        )

        DrawerItemCard(drawerItem = DrawerItem.Profile, onClick = onProfileClick)
        DrawerItemCard(drawerItem = DrawerItem.Locations, onClick = onLocationsClick)
        DrawerItemCard(drawerItem = DrawerItem.Rewards, onClick = onRewardsClick)
        DrawerItemCard(drawerItem = DrawerItem.Offers, onClick = onOffersClick)
        DrawerItemCard(drawerItem = DrawerItem.ContactUs, onClick = onContactUsClick)
        DrawerItemCard(drawerItem = DrawerItem.SignOut, onClick = onSignOutClick)

        Spacer(modifier = Modifier.weight(1f))

        if (isAdmin) {
            DrawerItemCard(drawerItem = DrawerItem.AdminPanel, onClick = onAdminClick)
        }
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Preview
@Composable
private fun CustomDrawerPrev() {
    BurgerRestaurantTheme {
        CustomDrawer(
            photoUrl = "https://scontent-sjc6-1.xx.fbcdn.net/v/t1.6435-1/82877049_617128115715815_5868603567627042816_n.jpg?stp=dst-jpg_s200x200_tt6&_nc_cat=101&ccb=1-7&_nc_sid=e99d92&_nc_ohc=C2zRcktPQJkQ7kNvwFUTi2K&_nc_oc=AdmjKdU2wA-1Fw22oA3pI3dYB8swEB7aTnufsbiZT2nQIxkztdCyKRErfpJtJsoqZcys6HtMhAw0e50VrhDhsw_R&_nc_zt=24&_nc_ht=scontent-sjc6-1.xx&_nc_gid=zoze4h2nTffPRnEQVFeMxQ&oh=00_Afh40nOOlFProj7KoL9gi_T210Iosal8QPpW9UAC544fgA&oe=693B7BB9",
            displayName = "Mike Seibel"
        )
    }
}