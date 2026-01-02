package com.sailinghawklabs.burgerrestaurant.feature.util

import android.content.Context
import android.widget.Toast

object MessageUtils {

    fun showToast(
        context: Context,
        message: String,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, duration).show()
        }
    }
}
