package com.sailinghawklabs.burgerrestaurant.feature.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Currency

class CurrencyVisualTransformation(
    private val currency: Currency = Currency.getInstance("USD")
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val number = originalText.toDoubleOrNull() ?: 0.0
        val formattedText = NumberFormat.getCurrencyInstance().apply {
            this.currency = this@CurrencyVisualTransformation.currency
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }.format(number / 100)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return formattedText.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return originalText.length
            }
        }

        return TransformedText(
            AnnotatedString(formattedText),
            offsetMapping
        )
    }
}
