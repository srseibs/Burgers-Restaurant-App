package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.ButtonDisabled
import com.sailinghawklabs.burgerrestaurant.ui.theme.ButtonPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.ButtonSecondary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextSecondary

enum class ButtonType {
    PRIMARY,
    SECONDARY
}


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    enabled: Boolean = true,
    buttonType: ButtonType = ButtonType.PRIMARY,
    onClick: () -> Unit

) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (buttonType == ButtonType.PRIMARY) ButtonPrimary else ButtonSecondary,
            contentColor = TextPrimary,
            disabledContainerColor = ButtonDisabled,
            disabledContentColor = TextPrimary.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(20.dp)
    ) {
        Text(
            text = text,
            fontSize = AppFontSize.REGULAR,
            fontWeight = FontWeight.Medium
        )

        if (icon != null) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = icon,
                contentDescription = "Button Icon",
                tint = TextSecondary
            )

        }
    }
}


@Preview
@Composable
private fun PrimaryButtonPrev() {
    BurgerRestaurantTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                buttonType = ButtonType.PRIMARY,
                text = "Primary",
                icon = painterResource(id = R.drawable.check),
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                buttonType = ButtonType.SECONDARY,
                text = "Secondary",
                onClick = {}
            )
        }
    }
}