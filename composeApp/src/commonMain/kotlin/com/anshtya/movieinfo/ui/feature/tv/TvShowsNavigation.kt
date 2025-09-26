package com.anshtya.movieinfo.ui.feature.tv

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.compose.viewmodel.koinViewModel

private const val tvShowsNavigationRoute = "tv_shows"

fun NavGraphBuilder.tvShowsScreen(
    navController: NavController,
    navigateToDetails: (String) -> Unit
) {
    navigation(
        route = tvShowsNavigationRoute,
        startDestination = TvShowsScreenRoutes.FEED
    ) {
        composable(route = TvShowsScreenRoutes.FEED) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(tvShowsNavigationRoute)
            }
            val viewModel = koinViewModel<TvShowsViewModel>(viewModelStoreOwner = parentEntry)
            FeedRoute(
                navigateToDetails = navigateToDetails,
                navigateToItems = {
                    navController.navigate("${TvShowsScreenRoutes.ITEMS}/$it")
                },
                viewModel = viewModel,
            )
        }

        composable(
            route = "${TvShowsScreenRoutes.ITEMS}/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(tvShowsNavigationRoute)
            }
            val viewModel = koinViewModel<TvShowsViewModel>(viewModelStoreOwner = parentEntry)
            ItemsRoute(
                categoryName = backStackEntry.savedStateHandle["category"]!!,
                onItemClick = navigateToDetails,
                onBackClick = navController::navigateUp,
                viewModel = viewModel
            )
        }
    }
}

internal object TvShowsScreenRoutes {
    const val FEED = "tv_shows_feed"
    const val ITEMS = "tv_shows_items"
}

fun NavController.navigateToTvShows(navOptions: NavOptions) {
    navigate(tvShowsNavigationRoute, navOptions)
}