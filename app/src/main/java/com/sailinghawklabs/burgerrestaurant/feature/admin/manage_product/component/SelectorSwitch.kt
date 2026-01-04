package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceBrand
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary

@Composable
fun SelectorSwitch(
    modifier: Modifier = Modifier,
    label: String,
    state: Boolean,
    onStateChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = label,
            fontSize = AppFontSize.REGULAR,
            color = TextPrimary
        )
        Switch(
            checked = state,
            onCheckedChange = onStateChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = SurfaceBrand,
                uncheckedTrackColor = SurfaceBrand.copy(alpha = 0.5f),

                checkedThumbColor = Surface,
                uncheckedThumbColor = Surface,

                checkedBorderColor = SurfaceBrand,
                uncheckedBorderColor = SurfaceBrand
            )
        )
    }

}

@Preview
@Composable
private fun SelectorSwitchPrev() {

    var switchState by remember { mutableStateOf(false) }

    BurgerRestaurantTheme {
        SelectorSwitch(
            modifier = Modifier,
            label = "New",
            state = switchState,
            onStateChange = { switchState = it }
        )
    }


}