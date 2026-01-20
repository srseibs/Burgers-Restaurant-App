package com.sailinghawklabs.burgerrestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sailinghawklabs.burgerrestaurant.feature.nav.Navigator
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BurgerRestaurantTheme {
                Navigator()
            }
        }
    }
}
//  https://youtu.be/xPzS0Gih_IU?si=tYESqYGLkl9ejujI&t=404