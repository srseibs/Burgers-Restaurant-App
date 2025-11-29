package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary

@Composable
fun BurgerSelectTextField(
    modifier: Modifier = Modifier,
    text: String,
    iconUrl: String? = null,
    onClick: () -> Unit,
    placeholder: String = "",
    isError: Boolean = false
) {
    val contentColor = if (text.isBlank()) TextPrimary.copy(0.6f) else TextPrimary

    Row(
        modifier = modifier
            .background(SurfaceLight)
            .border(
                width = 1.dp,
                color = SurfaceSecondary,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (iconUrl != null) {
            AsyncImage(
                model = iconUrl,
                contentDescription = "Country flag",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text.ifBlank { placeholder },
            fontSize = AppFontSize.REGULAR,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    if (isError) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Invalid selection",
            color = Color.Red,
            fontSize = AppFontSize.SMALL,
        )
    }


}


@Preview
@Composable
private fun BurgerSelectTextFieldPrev() {
    BurgerRestaurantTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            BurgerSelectTextField(
                text = "United States",
                iconUrl = "https://flagcdn.com/w320/us.png",
                onClick = {}
            )

            BurgerSelectTextField(
                text = "United States",
                iconUrl = "https://flagcdn.com/w320/us.png",
                onClick = {},
                isError = true
            )
        }
    }
}