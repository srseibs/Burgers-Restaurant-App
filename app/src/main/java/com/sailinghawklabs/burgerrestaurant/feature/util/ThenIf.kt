package com.sailinghawklabs.burgerrestaurant.feature.util

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(predicate: Boolean, modify: () -> Modifier): Modifier {
    return this.then(if (predicate) modify() else Modifier)
}