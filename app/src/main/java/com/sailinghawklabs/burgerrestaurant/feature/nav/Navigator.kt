package com.sailinghawklabs.burgerrestaurant.feature.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sailinghawklabs.burgerrestaurant.feature.admin.AdminScreen
import com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product.ManageProductScreen
import com.sailinghawklabs.burgerrestaurant.feature.auth.AuthScreen
import com.sailinghawklabs.burgerrestaurant.feature.home.HomeScreen
import com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.ProductOverviewScreen
import com.sailinghawklabs.burgerrestaurant.feature.profile.ProfileScreen
import com.sailinghawklabs.burgerrestaurant.feature.splash.SplashScreen


@Composable
fun Navigator(
    startDestination: Destination = Destination.SplashScreen,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Destination.SplashScreen> {
            SplashScreen(
                onDoneClick = {
                    navController.navigateAndDontComeBack(Destination.AuthScreen)
                },
                onAlreadyAuthed = {
                    navController.navigateAndDontComeBack(Destination.HomeGraph)
                }
            )
        }

        composable<Destination.AuthScreen> {
            AuthScreen(
                onGotoHomeScreen = {
                    navController.navigateAndDontComeBack(Destination.HomeGraph)
                }
            )
        }

        composable<Destination.HomeGraph> {
            HomeScreen(
                onSignedOut = {
                    navController.navigateAndDontComeBack(Destination.AuthScreen)
                },
                onProfileClick = {
                    navController.navigate(Destination.ProfileScreen)
                },
                onAdminClick = {
                    navController.navigate(Destination.AdminScreen)
                },
                onProductOverviewClick = {
                    navController.navigate(Destination.ProductOverviewScreen)
                }
            )
        }

        composable<Destination.ProfileScreen> {
            ProfileScreen(
                onNavigateBack = {
                    navController.navigateAndDontComeBack(Destination.HomeGraph)
                }
            )
        }

        composable<Destination.AdminScreen> {
            AdminScreen(
                onNavigateBack = {
                    navController.navigateAndDontComeBack(Destination.HomeGraph)
                },
                onNavigateToManageProduct = { productId ->
                    navController.navigate(Destination.ManageProductScreen(productId))
                }
            )
        }

        composable<Destination.ManageProductScreen> {
            val productId = it.toRoute<Destination.ManageProductScreen>().productId
            ManageProductScreen(
                productId = productId,
                onNavigateBack = {
                    navController.navigateAndDontComeBack(Destination.AdminScreen)
                }
            )
        }

        composable<Destination.ProductOverviewScreen> {
            ProductOverviewScreen(
                onProductClick = {},
                onGotoMainScreen = {
                    navController.navigateAndDontComeBack(Destination.HomeGraph)
                }
            )
        }
    }

}