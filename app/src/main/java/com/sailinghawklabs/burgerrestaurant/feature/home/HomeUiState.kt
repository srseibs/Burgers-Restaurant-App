package com.sailinghawklabs.burgerrestaurant.feature.home

import com.google.firebase.auth.FirebaseUser
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState

data class HomeState(
    val currentUser: FirebaseUser? = null,
    val isCurrentUserAdmin: RequestState<Boolean> = RequestState.Idle
)