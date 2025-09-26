package com.anshtya.movieinfo.ui.feature.movies

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.compose.viewmodel.koinViewModel

const val moviesNavigationRoute = "movies"

fun NavGraphBuilder.moviesScreen(
    navController: NavController,
    navigateToDetails: (String) -> Unit
) {
    navigation(
        route = moviesNavigationRoute,
        startDestination = MoviesScreenRoutes.FEED
    ) {
        composable(route = MoviesScreenRoutes.FEED) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(moviesNavigationRoute)
            }
            val viewModel = koinViewModel<MoviesViewModel>(viewModelStoreOwner = parentEntry)
            FeedRoute(
                navigateToDetails = navigateToDetails,
                navigateToItems = { navController.navigate("${MoviesScreenRoutes.ITEMS}/$it") },
                viewModel = viewModel,
            )
        }

        composable(
            route = "${MoviesScreenRoutes.ITEMS}/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(moviesNavigationRoute)
            }
            val viewModel = koinViewModel<MoviesViewModel>(viewModelStoreOwner = parentEntry)
            ItemsRoute(
                categoryName = backStackEntry.savedStateHandle["category"]!!,
                onItemClick = navigateToDetails,
                onBackClick = navController::navigateUp,
                viewModel = viewModel
            )
        }
    }
}

internal object MoviesScreenRoutes {
    const val FEED = "movies_feed"
    const val ITEMS = "movies_items"
}

fun NavController.navigateToMovies(navOptions: NavOptions) {
    navigate(moviesNavigationRoute, navOptions)
}