package com.sailinghawklabs.burgerrestaurant.feature.auth

import android.app.Activity

// Events to the ViewModel <---- from the Screen
sealed interface AuthScreenEvent {
    data class RequestGoogleLogin(val activity: Activity) : AuthScreenEvent
    data object RequestGuestLogin : AuthScreenEvent
}
