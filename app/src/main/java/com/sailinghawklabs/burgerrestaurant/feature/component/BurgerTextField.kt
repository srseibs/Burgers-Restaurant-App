package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.feature.util.Alpha
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderError
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceDark
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary

@Composable
fun BurgerTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )
    val cornerSize = 8.dp


    TextField(
        value = "hello",
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clip(RoundedCornerShape(cornerSize)),

        enabled = enabled,
        placeholder = {
            placeholder?.let {
                Text(
                    text = placeholder,
                    fontSize = AppFontSize.REGULAR
                )
            }
        },
        keyboardOptions = keyboardOptions,
        singleLine = !expanded,
        shape = RoundedCornerShape(cornerSize),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = SurfaceLight,
            focusedContainerColor = SurfaceLight,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            disabledTextColor = TextPrimary.copy(Alpha.DISABLED),
            focusedPlaceholderColor = TextPrimary.copy(Alpha.HALF),
            unfocusedPlaceholderColor = TextPrimary.copy(Alpha.HALF),
            disabledContainerColor = SurfaceDark,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            selectionColors = TextSelectionColors(
                handleColor = IconSecondary,
                backgroundColor = Color.Unspecified
            )
        )
    )
}


@Preview
@Composable
private fun BurgerTestFieldPrev() {
    BurgerRestaurantTheme {

        var entry by rememberSaveable { mutableStateOf("Mike Seibel") }

        BurgerTextField(
            value = entry,
            onValueChange = { entry = it }
        )
    }
}