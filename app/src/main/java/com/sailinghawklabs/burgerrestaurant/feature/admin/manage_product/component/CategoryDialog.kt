package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Gray
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconWhite
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextBrand
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextWhite

@Composable
fun CategoryDialog(
    modifier: Modifier = Modifier,
    categories: List<ProductCategory>,
    selectedCategory: ProductCategory? = null,
    onDismiss: () -> Unit,
    onSelectedCategory: (ProductCategory) -> Unit
) {

    var currentlySelectedCategory by rememberSaveable { mutableStateOf(selectedCategory) }
    LaunchedEffect(selectedCategory) {
        currentlySelectedCategory = selectedCategory
    }


    AlertDialog(
        modifier = modifier,
        containerColor = SurfaceLight,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select a Category",
                fontSize = AppFontSize.EXTRA_MEDIUM,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                categories.forEach { currentCategory ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { currentlySelectedCategory = currentCategory }
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentCategory == currentlySelectedCategory)
                                BrandBrown
                            else Gray
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painter = painterResource(currentCategory.icon),
                                contentDescription = currentCategory.title,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                modifier = Modifier.weight(1f),
                                text = currentCategory.title,
                                color = if (currentCategory == currentlySelectedCategory)
                                    TextWhite
                                else
                                    TextPrimary,
                                fontSize = AppFontSize.REGULAR
                            )
                            AnimatedVisibility(visible = currentCategory == currentlySelectedCategory) {
                                Icon(
                                    painter = painterResource(Resources.Icon.Checkmark),
                                    contentDescription = "Checkmark Icon",
                                    modifier = Modifier.size(16.dp),
                                    tint = IconWhite
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    currentlySelectedCategory?.let {
                        onSelectedCategory(it)
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TextBrand,
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Confirm",
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary.copy(Alpha.HALF)
                )
            ) {
                Text(
                    text = "Cancel",
                    fontSize = AppFontSize.REGULAR,
                    fontWeight = FontWeight.Medium
                )
            }
        },

        )

}


@Preview
@Composable
private fun CategoryDialogPrev() {

    var localCategory by remember { mutableStateOf(ProductCategory.Desserts) }

    BurgerRestaurantTheme {
        CategoryDialog(
            categories = ProductCategory.entries,
            selectedCategory = localCategory,
            onDismiss = {},
            onSelectedCategory = { localCategory = it }
        )
    }
}
