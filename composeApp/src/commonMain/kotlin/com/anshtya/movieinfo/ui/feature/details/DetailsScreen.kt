package com.anshtya.movieinfo.ui.feature.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.ui.feature.details.content.MovieDetailsContent
import com.anshtya.movieinfo.ui.feature.details.content.PersonDetailsContent
import com.anshtya.movieinfo.ui.feature.details.content.TvShowDetailsContent
import kotlinx.coroutines.launch
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.details_loading
import org.jetbrains.compose.resources.stringResource

internal val horizontalPadding = 8.dp
internal val verticalPadding = 4.dp

@Composable
internal fun DetailsRoute(
    onNavigateToItem: (Int, MediaType) -> Unit,
    onSeeAllCastClick: () -> Unit,
    navigateToAuth: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentDetailsUiState by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        contentDetailsUiState = contentDetailsUiState,
        onHideBottomSheet = viewModel::onHideBottomSheet,
        onErrorShown = viewModel::onErrorShown,
        onFavoriteClick = viewModel::addOrRemoveFavorite,
        onWatchlistClick = viewModel::addOrRemoveFromWatchlist,
        onItemClick = onNavigateToItem,
        onSeeAllCastClick = onSeeAllCastClick,
        onSignInClick = navigateToAuth,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    contentDetailsUiState: ContentDetailUiState,
    onHideBottomSheet: () -> Unit,
    onErrorShown: () -> Unit,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onItemClick: (Int, MediaType) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.statusBars)
    ) { paddingValues ->
        when (contentDetailsUiState) {
            ContentDetailUiState.Loading -> {
                val loadingContentDescription = stringResource(Res.string.details_loading)
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics {
                                contentDescription = loadingContentDescription
                            }
                    )
                }
            }

            ContentDetailUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                ) {
                    uiState.errorMessage?.let {
                        scope.launch { snackbarHostState.showSnackbar(it) }
                        onErrorShown()
                    }
                }
            }

            is ContentDetailUiState.Movie -> {
                MovieDetailsContent(
                    uiState = uiState,
                    movieDetails = contentDetailsUiState.data,
                    onFavoriteClick = onFavoriteClick,
                    onWatchlistClick = onWatchlistClick,
                    onCastClick = onItemClick,
                    onRecommendationClick = onItemClick,
                    onSeeAllCastClick = onSeeAllCastClick,
                    onHideBottomSheet = onHideBottomSheet,
                    onSignInClick = onSignInClick,
                    onBackClick = onBackClick,
                    modifier = Modifier.consumeWindowInsets(paddingValues)
                )
            }

            is ContentDetailUiState.TvSeries -> {
                TvShowDetailsContent(
                    uiState = uiState,
                    tvSeriesDetails = contentDetailsUiState.data,
                    onFavoriteClick = onFavoriteClick,
                    onWatchlistClick = onWatchlistClick,
                    onSeeAllCastClick = onSeeAllCastClick,
                    onCastClick = onItemClick,
                    onRecommendationClick = onItemClick,
                    onHideBottomSheet = onHideBottomSheet,
                    onSignInClick = onSignInClick,
                    onBackClick = onBackClick,
                    modifier = Modifier.consumeWindowInsets(paddingValues)
                )
            }

            is ContentDetailUiState.Person -> {
                PersonDetailsContent(
                    personDetails = contentDetailsUiState.data,
                    onBackClick = onBackClick
                )
            }
        }
    }
}