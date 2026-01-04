package com.sailinghawklabs.burgerrestaurant.feature.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.CustomDrawerState
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.isOpen
import com.sailinghawklabs.burgerrestaurant.feature.home.domain.reverse
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.feature.nav.navigateAndDontComeBack
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BrandBrown
import com.sailinghawklabs.burgerrestaurant.ui.theme.BurgerRestaurantTheme
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onProfileClick: () -> Unit,
    onSignedOut: () -> Unit,
    onAdminClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentUser = state.currentUser
    val context = LocalContext.current



    HomeScreenContent(
        currentUser = currentUser,
        onEvent = viewModel::onEvent,
        state = state
    )

    ObserveAsCommand(flow = viewModel.commandsForScreen) { command ->
        when (command) {
            HomeScreenCommand.ExitDueToUserSignedOff -> onSignedOut()

            is HomeScreenCommand.ShowErrorMessage -> {
                Toast.makeText(context, command.message, Toast.LENGTH_LONG).show()
            }

            is HomeScreenCommand.NavigateToProfile -> {
                onProfileClick()
            }

            is HomeScreenCommand.NavigateToAdmin -> {
                onAdminClick()

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    currentUser: FirebaseUser? = null,
    onEvent: (HomeScreenEvent) -> Unit,
    state: HomeState
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

    // this prevents the back button from closing the app and redirects to the default destination
    BackHandler {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            if (selectedBottomDestination != defaultBottomDestination) {
                navController.navigateAndDontComeBack(defaultBottomDestination.destination)
            }
        }
    }

    val screenWidthDp = getScreenWidthDp()

    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    val drawerOffsetDp by remember { derivedStateOf { screenWidthDp * .5f } }

    val animatedOffsetDp by animateDpAsState(
        targetValue = if (drawerState.isOpen()) drawerOffsetDp else 0.dp
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpen()) 0.9f else 1f
    )
    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpen()) 40.dp else 0.dp
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBrown)
    ) {
        CustomDrawer(
            photoUrl = currentUser?.photoUrl.toString(),
            displayName = currentUser?.displayName,
            onSignOutClick = { onEvent(HomeScreenEvent.LogoutRequest) },
            onProfileClick = { onEvent(HomeScreenEvent.RequestProfile) },
            onAdminClick = { onEvent(HomeScreenEvent.RequestAdmin) },
            isAdmin = if (state.isCurrentUserAdmin.isSuccess()) {
                state.isCurrentUserAdmin.getSuccessData()
            } else {
                false
            }
        )


        // Box for the Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(animatedOffsetDp)
                .scale(animatedScale)
                .dropShadow(
                    shape = RoundedCornerShape(animatedRadius),
                    shadow = Shadow(
                        radius = 15.dp,
                        spread = 3.dp,
                        color = Color.Black.copy(0.3f),
                        offset = DpOffset(x = (-6).dp, 6.dp)
                    )
                )
                .clip(RoundedCornerShape(animatedRadius))

        ) {
            Scaffold(
                containerColor = Surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            AnimatedContent(
                                targetState = selectedBottomDestination
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
                                onClick = { drawerState = drawerState.reverse() }
                            ) {
                                AnimatedContent(
                                    targetState = drawerState
                                ) { drawer ->
                                    if (drawer.isOpen()) {
                                        Icon(
                                            painter = painterResource(R.drawable.close),
                                            contentDescription = "close icon",
                                            tint = IconPrimary
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(R.drawable.menu),
                                            contentDescription = "menu icon",
                                            tint = IconPrimary
                                        )
                                    }
                                }
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

                        }
                        composable<Destination.CartScreen> {

                        }
                        composable<Destination.Notifications> {

                        }
                        composable<Destination.Categories> {

                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    BurgersBottomBar(
                        selected = selectedBottomDestination,
                        onSelect = {
                            navController.navigateAndDontComeBack(it.destination)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun getScreenWidthInPx(): Int {
    val windowInfo = LocalWindowInfo.current
    val screenWidthPx = windowInfo.containerSize.width
    return screenWidthPx
}

@Composable
fun getScreenWidthDp(): Dp {
    val widthPx = LocalWindowInfo.current.containerSize.width
    val widthDp = with(LocalDensity.current) { widthPx.toDp() }
    return widthDp
}

@Preview
@Composable
private fun Preview() {
    BurgerRestaurantTheme {
        HomeScreenContent(
            currentUser = null,
            onEvent = {},
            state = HomeState()
        )
    }
}
