package com.anshtya.movieinfo.ui.feature.details

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.compose.viewmodel.koinViewModel

private const val detailsNavigationRoute = "details"
private const val creditsNavigationRoute = "credits"
internal const val idNavigationArgument = "id"
private const val detailsNavigationRouteWithArg = "$detailsNavigationRoute/{$idNavigationArgument}"

fun NavGraphBuilder.detailsScreen(
    navController: NavController,
    navigateToAuth: () -> Unit
) {
    navigation(
        route = detailsNavigationRouteWithArg,
        startDestination = detailsNavigationRoute
    ) {
        composable(
            route = detailsNavigationRoute
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(detailsNavigationRouteWithArg)
            }
            val viewModel = koinViewModel<DetailsViewModel>(viewModelStoreOwner = parentEntry)

            DetailsRoute(
                onBackClick = navController::navigateUp,
                onItemClick = navController::navigateToDetails,
                onSeeAllCastClick = navController::navigateToCredits,
                navigateToAuth = navigateToAuth,
                viewModel = viewModel
            )
        }

        composable(
            route = creditsNavigationRoute
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(detailsNavigationRouteWithArg)
            }
            val viewModel = koinViewModel<DetailsViewModel>(viewModelStoreOwner = parentEntry)

            CreditsRoute(
                viewModel = viewModel,
                onItemClick = navController::navigateToDetails,
                onBackClick = navController::navigateUp
            )
        }
    }
}

fun NavController.navigateToDetails(id: String) {
    navigate("$detailsNavigationRoute/$id")
}

private fun NavController.navigateToCredits() {
    navigate(creditsNavigationRoute)
}