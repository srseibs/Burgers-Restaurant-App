package com.sailinghawklabs.burgerrestaurant.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight

@Composable
fun ProfilePhotoEditor(
    modifier: Modifier = Modifier,
    photoUrl: String?,
    isUploading: Boolean,
    progress: Float,
    onPickClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(132.dp)
            .clip(CircleShape)
            .background(SurfaceLight)
            .clickable(enabled = !isUploading) { onPickClick() },
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNullOrBlank()) {
            Icon(
                painter = painterResource(id = Resources.Icon.Person),
                contentDescription = "User profile icon",
                tint = IconPrimary,
                modifier = Modifier.size(64.dp)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "User profile photo",
                modifier = Modifier.matchParentSize(),
                placeholder = painterResource(id = Resources.Icon.Person),
                error = painterResource(id = Resources.Icon.Close)
            )
        }

        // Upload overlay
        if (isUploading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = .5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = Color.White,
                        fontSize = AppFontSize.SMALL
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .offset(y = 4.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        TextButton(
            onClick = onPickClick,
            enabled = !isUploading
        ) {
            Icon(
                painter = painterResource(id = Resources.Icon.Camera),
                contentDescription = "Change profile photo",
                tint = IconPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Change photo",
                color = IconPrimary,
                fontSize = AppFontSize.REGULAR
            )
        }
    }
}


@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun ProfilePhotoEditorPrev() {
    BurgerRestaurantTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ProfilePhotoEditor(
                photoUrl = null,
                isUploading = false,
                progress = .2f,
                onPickClick = {}
            )
            HorizontalDivider(modifier = Modifier.padding(16.dp))
            ProfilePhotoEditor(
                photoUrl = null,
                isUploading = true,
                progress = .7f,
                onPickClick = {}
            )

        }


    }
}