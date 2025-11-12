package com.sailinghawklabs.burgerrestaurant.feature.home

import com.google.firebase.auth.FirebaseUser

data class HomeState(
    val currentUser: FirebaseUser? = null,

)