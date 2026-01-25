package com.sailinghawklabs.burgerrestaurant.feature.util

fun Int?.toCalorieLabel() = this?.let { "$it kcal" } ?: ""