package com.anshtya.movieinfo.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.ui.FeedType
import com.anshtya.movieinfo.ui.OnboardingScreen
import com.anshtya.movieinfo.ui.feature.auth.Auth
import com.anshtya.movieinfo.ui.feature.auth.authScreen
import com.anshtya.movieinfo.ui.feature.details.Details
import com.anshtya.movieinfo.ui.feature.details.detailsScreen
import com.anshtya.movieinfo.ui.feature.items.Items
import com.anshtya.movieinfo.ui.feature.items.itemsScreen
import com.anshtya.movieinfo.ui.feature.you.library_items.LibraryItems
import com.anshtya.movieinfo.ui.feature.you.library_items.libraryItemsScreen
import com.anshtya.movieinfo.ui.main.MainRoute
import com.anshtya.movieinfo.ui.main.mainGraph
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
private data object MainGraph

@Serializable
private data object Onboarding

@Composable
fun MovieInfoNavigation(
    hideOnboarding: Boolean,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (hideOnboarding) {
        MainGraph
    } else {
        Onboarding
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Onboarding> {
            OnboardingScreen(
                navigateToAuth = {
                    navController.navigate(Auth)
                }
            )
        }

        composable<MainGraph> {
            Main(
                navigateToDetails = { id, type ->
                    navController.navigate(Details(id, type))
                },
                navigateToItems = { type, category ->
                    navController.navigate(Items(type, category))
                },
                navigateToLibraryItem = {
                    navController.navigate(LibraryItems(it))
                },
                navigateToAuth = {
                    navController.navigate(Auth)
                }
            )
        }

        authScreen(onBackClick = navController::navigateUp)

        itemsScreen(
            onNavigateUp = navController::navigateUp,
            onNavigateToItem = { id, type ->
                navController.navigate(Details(id, type))
            }
        )

        libraryItemsScreen(
            onNavigateUp = navController::navigateUp,
            navigateToDetails = { id, type ->
                navController.navigate(Details(id, type))
            }
        )

        detailsScreen(
            navController = navController,
            navigateToAuth = {
                navController.navigate(Auth)
            }
        )
    }
}

@Composable
private fun Main(
    navigateToDetails: (Int, MediaType) -> Unit,
    navigateToItems: (FeedType, String) -> Unit,
    navigateToLibraryItem: (LibraryType) -> Unit,
    navigateToAuth: () -> Unit,
) {
    val navController = rememberNavController()
    val bottomBarDestinations = remember { MainRoute.entries }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            // handle status bar insets at screen level
            .exclude(WindowInsets.statusBars),
        bottomBar = {
            MovieInfoNavigationBar(
                destinations = bottomBarDestinations,
                currentDestination = currentDestination,
                onNavigateToDestination = { destination ->
                    navController.navigateToBottomBarDestination(destination)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainRoute.MOVIES.route
        ) {
            mainGraph(
                navigateToDetails = navigateToDetails,
                navigateToItems = navigateToItems,
                navigateToLibraryItem = navigateToLibraryItem,
                navigateToAuth = navigateToAuth,
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )
        }
    }
}

@Composable
private fun MovieInfoNavigationBar(
    destinations: List<MainRoute>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (MainRoute) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.title)) }
            )
        }
    }
}

private fun NavDestination?.isDestinationInHierarchy(destination: MainRoute): Boolean {
    return this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false
}

private fun NavController.navigateToBottomBarDestination(destination: MainRoute) {
    val navOptions = navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (destination) {
        MainRoute.MOVIES -> navigate(MainRoute.MOVIES.route, navOptions)
        MainRoute.TV_SHOWS -> navigate(MainRoute.TV_SHOWS.route, navOptions)
        MainRoute.SEARCH -> navigate(MainRoute.SEARCH.route, navOptions)
        MainRoute.YOU -> navigate(MainRoute.YOU.route, navOptions)
    }
}