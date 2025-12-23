package com.sailinghawklabs.burgerrestaurant.feature.util

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

object MessageUtils {

    @Composable
    fun showToast(
        message: String,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = message) {
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, duration).show()
            }
        }
    }
}