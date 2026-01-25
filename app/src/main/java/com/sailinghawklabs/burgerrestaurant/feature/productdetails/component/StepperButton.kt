package com.sailinghawklabs.burgerrestaurant.feature.productdetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandYellow
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources

@Composable
fun StepperButton(
    text: String,
    onClick: () -> Unit,
    iconPainter: Painter,
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.size(24.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, BrandYellow)
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = "Step Icon",
            tint = BrandBrown
        )
    }
}

@Composable
fun QuantityStepper(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepperButton(
            text = "-",
            onClick = onDecrement,
            iconPainter = painterResource(Resources.Icon.Minus)
        )
        Text(
            text = quantity.toString().padStart(length = 2, padChar = '0'),
            fontSize = AppFontSize.REGULAR,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        StepperButton(
            text = "+",
            onClick = onIncrement,
            iconPainter = painterResource(Resources.Icon.Plus)
        )
    }
}

@Preview
@Composable
private fun QuantityStepperPrev() {

    val qty = remember { mutableIntStateOf(5) }

    BurgerRestaurantTheme {
        QuantityStepper(
            quantity = qty.intValue,
            onIncrement = { qty.intValue += 1 },
            onDecrement = { qty.intValue = (qty.intValue - 1).coerceAtLeast(1) }
        )

    }

}