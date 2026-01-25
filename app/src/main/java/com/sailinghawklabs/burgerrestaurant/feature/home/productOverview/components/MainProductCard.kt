package com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.core.data.model.fakeProducts
import com.sailinghawklabs.burgerrestaurant.feature.admin.component.PreviewableAsyncImage
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.feature.util.toCalorieLabel
import com.sailinghawklabs.burgerrestaurant.feature.util.toCurrencyString
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandYellow
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextWhite
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainProductCard(
    modifier: Modifier = Modifier,
    title: String,
    calories: String,
    price: String,
    imageUrl: String,
    paused: Boolean,
    onClick: () -> Unit,
) {
    val cardHeight = 220.dp
    val brownFraction = 0.45f
    val density = LocalDensity.current.density

    // Animation values
    val imageOffsetX = remember { Animatable(initialValue = -100f) }
    val imageAlpha = remember { Animatable(initialValue = 0.95f) }
    val imageScale = remember { Animatable(initialValue = 0.98f) }


    LaunchedEffect(paused, imageUrl) {
        if (paused) {
            imageOffsetX.snapTo(0f)
            imageAlpha.snapTo(1f)
            imageScale.snapTo(1f)
            return@LaunchedEffect
        }

        while (true) {
            coroutineScope {
                launch {
                    imageOffsetX.animateTo(
                        targetValue = 0f,
                        animationSpec =
                            tween(durationMillis = 900, easing = FastOutSlowInEasing)
                    )
                }
                launch {
                    imageAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 350)
                    )
                }
            }

            imageScale.snapTo(targetValue = 1.02f)
            imageScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            delay(timeMillis = 5000)

            imageOffsetX.snapTo(-100f)
            imageAlpha.snapTo(0.2f)
            imageScale.snapTo(0.8f)

            delay(timeMillis = 500)

        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {

        // root box for layering
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // static background layer
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f - brownFraction)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-1).dp) // covers a small line sometimes
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                BrandBrown,
                                BrandBrown.copy(Alpha.DISABLED),
                                Color.Transparent,
                                Color.Transparent
                            )
                        )
                    )
            )

            // animated layer
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    // Occupy only the space NOT taken by the brown fraction
                    .fillMaxWidth(1f - brownFraction)
                    .align(Alignment.CenterEnd) // Stick to the right
                    .graphicsLayer {
                        // Animation transformations
                        translationX = imageOffsetX.value * density
                        alpha = imageAlpha.value
                        scaleX = imageScale.value
                        scaleY = imageScale.value
                    },
                contentAlignment = Alignment.Center
            ) {
                PreviewableAsyncImage(
                    model = ImageRequest.Builder(context = LocalPlatformContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Product Image",
                    previewImage = R.drawable.iced_tea_nobg,
                    previewTint = Color.Unspecified,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(185.dp)
                )
            }
            // 3. Top Layer (Left Brown Info Panel)
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(brownFraction)
                        .fillMaxHeight()
                        .background(BrandBrown)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            color = TextWhite,
                            fontSize = AppFontSize.LARGE,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = oswaldVariableFont,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 35.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(Resources.Icon.Flame),
                                contentDescription = "Flame Icon",
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = calories,
                                color = TextWhite.copy(0.65f),
                                fontSize = AppFontSize.EXTRA_REGULAR,
                                fontFamily = oswaldVariableFont,
                                fontWeight = FontWeight.Medium,
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = price,
                            color = BrandYellow,
                            fontSize = AppFontSize.REGULAR,
                            fontFamily = oswaldVariableFont,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun MainProductCardPreview() {
    BurgerRestaurantTheme {
        MainProductCard(
            title = fakeProducts[0].title,
            calories = fakeProducts[0].calories.toCalorieLabel(),
            imageUrl = fakeProducts[0].productImage,
            price = fakeProducts[0].price.toCurrencyString(),
            paused = false,
            onClick = {}
        )
    }
}
