package com.sailinghawklabs.burgerrestaurant.feature.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandYellow
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import com.sailinghawklabs.burgerrestaurant.ui.theme.sentientVariableFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit = {},
    onAlreadyAuthed: () -> Unit = {},
    viewModel: SplashViewModel = koinViewModel()
) {


    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = {
                    OvershootInterpolator(7f).getInterpolation(it)
                }
            )
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandYellow)
            .navigationBarsPadding()
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            painter = painterResource(R.drawable.burgers),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .aspectRatio(1f)
                .scale(scale.value)
        )
        Text(
            text = stringResource(R.string.splash_header),
            fontFamily = oswaldVariableFont,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.splash_subtext),
            fontFamily = sentientVariableFont,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        SplashButton(
            onClick = {
                if (viewModel.userExists) {
                    onAlreadyAuthed()
                } else {
                    onDoneClick()
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SplashButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BrandBrown,
    contentColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .widthIn(max = 550.dp)
            .padding(horizontal = 24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.splash_btn_text),
                fontFamily = sentientVariableFont,
            )
            Icon(
                painter = painterResource(R.drawable.log_in),
                contentDescription = "Order now"
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun SplashScreenPrev() {
    BurgerRestaurantTheme {
        SplashScreen()
    }
}
