package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    primaryText: String = "Sign in with Google",
    loadingText: String = "Please wait...",
    icon: Painter? = painterResource(Resources.Image.GoogleLogo),
    buttonShape: Shape = RoundedCornerShape(100),
    backgroundColor: Color = SurfaceLight,
    borderColor: Color = BorderIdle,
    progressIndicatorColor: Color = IconSecondary,
    onClick: () -> Unit = {}
) {

    var buttonText by remember { mutableStateOf(primaryText) }
    LaunchedEffect(key1 = isLoading) {
        buttonText = if (isLoading) loadingText else primaryText
    }

    Surface(
        modifier = modifier
            .clip(buttonShape)
            .border(width = 1.dp, color = borderColor, shape = buttonShape)
            .clickable(enabled = !isLoading) { onClick() },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = isLoading
            ) { loadingState ->
                if (loadingState) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = progressIndicatorColor
                    )
                } else {
                    if (icon != null) {
                        Icon(
                            painter = icon,
                            contentDescription = "Google Logo",
                            tint = Color.Unspecified
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = buttonText,
                color = TextPrimary,
                fontSize = AppFontSize.REGULAR
            )
        }
    }


}

@Preview
@Composable
private fun GoogleButtonPrev() {
    BurgerRestaurantTheme {

        var isLoading by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GoogleButton(
                isLoading = isLoading,
                onClick = { isLoading = !isLoading },
                icon = painterResource(R.drawable.google_logo)
            )
        }
    }
}
