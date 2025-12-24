package com.sailinghawklabs.burgerrestaurant.feature.util

fun Double.toCurrencyString(): String {
    return "$${"%.2f".format(this)}"
}