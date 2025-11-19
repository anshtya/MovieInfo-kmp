package com.anshtya.movieinfo.ui.feature.details

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anshtya.movieinfo.common.data.model.MediaType
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class Details(
    val id: Int,
    val type: MediaType
)

private sealed interface DetailsGraph {
    @Serializable
    data object Details : DetailsGraph

    @Serializable
    data object Credits : DetailsGraph
}

fun NavGraphBuilder.detailsScreen(
    navController: NavController,
    navigateToAuth: () -> Unit
) {
    navigation<Details>(
        startDestination = DetailsGraph.Details
    ) {
        composable<DetailsGraph.Details> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<Details>()
            }
            val viewModel = koinViewModel<DetailsViewModel>(viewModelStoreOwner = parentEntry)

            DetailsRoute(
                onBackClick = navController::navigateUp,
                onNavigateToItem = { id, type ->
                    navController.navigate(Details(id, type))
                },
                onSeeAllCastClick = {
                    navController.navigate(DetailsGraph.Credits)
                },
                navigateToAuth = navigateToAuth,
                viewModel = viewModel
            )
        }

        composable<DetailsGraph.Credits> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<Details>()
            }
            val viewModel = koinViewModel<DetailsViewModel>(viewModelStoreOwner = parentEntry)

            CreditsRoute(
                viewModel = viewModel,
                onNavigateToItem = { id, type ->
                    navController.navigate(Details(id, type))
                },
                onBackClick = navController::navigateUp
            )
        }
    }
}