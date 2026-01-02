package com.sailinghawklabs.burgerrestaurant.feature.admin.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.core.data.model.fakeProducts
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.feature.util.toCurrencyString
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape((12.dp)))
            .border(
                width = 1.dp,
                color = BorderIdle,
                shape = RoundedCornerShape(12.dp)
            )
            .background(SurfaceLight)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = product.title,
                fontSize = AppFontSize.MEDIUM,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(Alpha.HALF),
                text = product.description,
                fontSize = AppFontSize.REGULAR,
                color = TextPrimary,
                fontFamily = oswaldVariableFont,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.price.toCurrencyString(),
                    fontSize = AppFontSize.EXTRA_REGULAR,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(Resources.Icon.Flame),
                        contentDescription = "Flame Icon",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.calories?.let { "$it kcal" } ?: "",
                        fontSize = AppFontSize.EXTRA_REGULAR,
                        color = TextPrimary,
                        fontFamily = oswaldVariableFont
                    )
                }
            }
        }

        PreviewableAsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                ),
            model = ImageRequest.Builder(context = LocalPlatformContext.current)
                .data(product.productImage)
                .crossfade(true)
                .build(),
            previewImage = R.drawable.burger,
            previewTint = Color.Unspecified,
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Resources.Icon.Burger)
        )
    }
}


@Composable
fun PreviewableAsyncImage(
    modifier: Modifier = Modifier.size(120.dp),
    @DrawableRes previewImage: Int = R.drawable.ic_launcher_foreground,
    previewTint: Color = Color.Blue,
    model: Any?,
    contentDescription: String?,
    contentScale: ContentScale,
    placeholder: Painter? = null
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        contentScale = contentScale,
        model = model,
        contentDescription = contentDescription,
        loading = {
            if (placeholder != null) {
                Image(
                    painter = placeholder,
                    contentDescription = null
                )
            }
        },
        error = { errorState ->
            if (LocalInspectionMode.current) {
                Icon(
                    painter = painterResource(previewImage),
                    contentDescription = "Preview Image",
                    tint = previewTint
                )
            } else {
                val errorText = errorState.result.throwable.message ?: "Unknown Error"
                Text(
                    textAlign = TextAlign.Center,
                    text = "Failed to load image\n : $errorText",
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )

}


@Preview
@Composable
private fun ProductCardPrev() {
    ProductCard(
        product = fakeProducts.first(),
        onClick = {}
    )
}
