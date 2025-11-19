package com.anshtya.movieinfo.ui.main

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anshtya.movieinfo.common.data.model.FeedType
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.ui.feature.movies.MoviesFeedRoute
import com.anshtya.movieinfo.ui.feature.search.SearchRoute
import com.anshtya.movieinfo.ui.feature.tv.TvShowsFeedRoute
import com.anshtya.movieinfo.ui.feature.you.YouRoute

fun NavGraphBuilder.mainGraph(
    navigateToDetails: (Int, MediaType) -> Unit,
    navigateToItems: (FeedType, String) -> Unit,
    navigateToLibraryItem: (LibraryType) -> Unit,
    navigateToAuth: () -> Unit,
    modifier: Modifier = Modifier
) {
    composable(MainRoute.MOVIES.route) {
        MoviesFeedRoute(
            navigateToDetails = navigateToDetails,
            navigateToItems = navigateToItems,
            modifier = modifier
        )
    }

    composable(MainRoute.TV_SHOWS.route) {
        TvShowsFeedRoute(
            navigateToDetails = navigateToDetails,
            navigateToItems = navigateToItems,
            modifier = modifier
        )
    }

    composable(MainRoute.SEARCH.route) {
        SearchRoute(
            navigateToDetail = navigateToDetails,
            modifier = modifier
        )
    }

    composable(MainRoute.YOU.route) {
        YouRoute(
            navigateToAuth = navigateToAuth,
            navigateToLibraryItem = navigateToLibraryItem,
            modifier = modifier
        )
    }
}