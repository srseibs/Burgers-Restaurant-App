package com.sailinghawklabs.burgerrestaurant.feature.splash

import androidx.lifecycle.ViewModel
import com.sailinghawklabs.burgerrestaurant.core.data.auth.GoogleUiClient

class SplashViewModel(
    private val googleAuthUiClient: GoogleUiClient,
) : ViewModel() {
    val userExists = googleAuthUiClient.currentUser != null

}