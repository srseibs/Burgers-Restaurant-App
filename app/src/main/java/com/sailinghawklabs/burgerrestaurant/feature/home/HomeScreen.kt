package com.sailinghawklabs.burgerrestaurant.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.home.component.BurgersBottomBar
import com.sailinghawklabs.burgerrestaurant.feature.home.component.CustomDrawer
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.BottomBarDestination
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.feature.nav.navigateAndDontComeBack
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onGotoMainScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentUser = state.currentUser

    HomeScreenContent(
        currentUser = currentUser,
        onEvent = viewModel::onEvent
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            HomeScreenCommand.NavigateToMainScreen -> onGotoMainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    currentUser: FirebaseUser? = null,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    val navController = rememberNavController()
    val defaultBottomDestination: BottomBarDestination = BottomBarDestination.ProductOverviewScreen

    val currentDestination = navController.currentBackStackEntryAsState()
    val selectedBottomDestination by remember(currentDestination.value) {
        derivedStateOf {
            val route = currentDestination.value?.destination?.route
            BottomBarDestination.entries.firstOrNull {
                it.destination::class.qualifiedName == route
            } ?: defaultBottomDestination
        }
    }

    BackHandler {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            if (selectedBottomDestination != defaultBottomDestination) {
                navController.navigateAndDontComeBack(defaultBottomDestination.destination)
            }
        }
    }


// https://youtu.be/2E3mR7mEvtI?si=pYVgAy-LflFPn1G4&t=3703
    Scaffold(
        containerColor = Surface,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedContent(
                        targetState = selectedBottomDestination,
                    ) { destination ->
                        Text(
                            text = destination.title,
                            fontFamily = oswaldVariableFont,
                            fontSize = AppFontSize.LARGE,
                            color = TextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.menu),
                            contentDescription = "menu icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                ),
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "search icon"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = defaultBottomDestination.destination
            ) {
                composable<Destination.ProductOverviewScreen> {
                    Text(text = "Products Screen")
                }
                composable<Destination.CartScreen> {
                    Text(text = "Cart Screen")
                }
                composable<Destination.Notifications> {
                    Text(text = "Notifications Screen")
                }
                composable<Destination.Categories> {
                    Text(text = "Categories")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box {
                BurgersBottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    selected = selectedBottomDestination,
                    onSelect = {
                        navController.navigateAndDontComeBack(it.destination)
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CustomDrawer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.7f)
                            .background(BrandBrown),
                        photoUrl = currentUser?.photoUrl.toString(),
                        displayName = currentUser?.displayName
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        HomeScreenContent(
            currentUser = null,
            onEvent = {}
        )
    }
}
