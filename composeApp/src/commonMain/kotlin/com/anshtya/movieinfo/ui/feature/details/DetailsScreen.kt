package com.anshtya.movieinfo.ui.feature.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.ui.component.AnimatedText
import com.anshtya.movieinfo.ui.component.TopAppBarWithBackButton
import com.anshtya.movieinfo.ui.feature.details.content.MovieDetailsContent
import com.anshtya.movieinfo.ui.feature.details.content.PersonDetailsContent
import com.anshtya.movieinfo.ui.feature.details.content.TvShowDetailsContent
import kotlinx.coroutines.launch
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.details_loading
import movieinfo.composeapp.generated.resources.details_sign_in_sheet
import movieinfo.composeapp.generated.resources.not_available
import movieinfo.composeapp.generated.resources.overview
import movieinfo.composeapp.generated.resources.sign_in
import movieinfo.composeapp.generated.resources.sign_in_sheet_text
import org.jetbrains.compose.resources.stringResource

internal val horizontalPadding = 8.dp
internal val verticalPadding = 4.dp

@Composable
internal fun DetailsRoute(
    onItemClick: (String) -> Unit,
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
        onItemClick = onItemClick,
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
    onItemClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(uiState.showSignInSheet) {
        if (uiState.showSignInSheet) {
            scaffoldState.bottomSheetState.expand()
        }
    }

    var isBackdropImageCollapsed by rememberSaveable { mutableStateOf(false) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        sheetContent = {
            val signInSheetContentDescription = stringResource(
                Res.string.details_sign_in_sheet
            )
            if (uiState.showSignInSheet) {
                ModalBottomSheet(
                    onDismissRequest = onHideBottomSheet,
                    sheetState = bottomSheetState,
                    modifier = Modifier.semantics {
                        contentDescription = signInSheetContentDescription
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                    ) {
                        Text(
                            text = stringResource(Res.string.sign_in_sheet_text),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(Modifier.height(50.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    scaffoldState.bottomSheetState.hide()
                                }.invokeOnCompletion {
                                    onHideBottomSheet()
                                }
                                onSignInClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(text = stringResource(Res.string.sign_in))
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (contentDetailsUiState) {
                ContentDetailUiState.Loading -> {
                    val loadingContentDescription = stringResource(
                        Res.string.details_loading
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics {
                                contentDescription = loadingContentDescription
                            }
                    )
                }

                ContentDetailUiState.Empty -> {
                    uiState.errorMessage?.let {
                        scope.launch { snackbarHostState.showSnackbar(it) }
                        onErrorShown()
                    }
                }

                is ContentDetailUiState.Movie -> {
                    MovieDetailsContent(
                        movieDetails = contentDetailsUiState.data,
                        isFavorite = uiState.markedFavorite,
                        isAddedToWatchList = uiState.savedInWatchlist,
                        onFavoriteClick = onFavoriteClick,
                        onWatchlistClick = onWatchlistClick,
                        onSeeAllCastClick = onSeeAllCastClick,
                        onCastClick = onItemClick,
                        onRecommendationClick = onItemClick,
                        onBackdropCollapse = {
                            isBackdropImageCollapsed = it
                        }
                    )
                }

                is ContentDetailUiState.TvSeries -> {
                    TvShowDetailsContent(
                        tvSeriesDetails = contentDetailsUiState.data,
                        isFavorite = uiState.markedFavorite,
                        isAddedToWatchList = uiState.savedInWatchlist,
                        onFavoriteClick = onFavoriteClick,
                        onWatchlistClick = onWatchlistClick,
                        onSeeAllCastClick = onSeeAllCastClick,
                        onCastClick = onItemClick,
                        onRecommendationClick = onItemClick,
                        onBackdropCollapse = {
                            isBackdropImageCollapsed = it
                        }
                    )
                }

                is ContentDetailUiState.Person -> {
                    PersonDetailsContent(
                        personDetails = contentDetailsUiState.data,
                        onBackClick = onBackClick,
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = 6.dp
                        )
                    )
                }
            }

            if (contentDetailsUiState !is ContentDetailUiState.Person) {
                DetailsTopAppBar(
                    showTitle = isBackdropImageCollapsed,
                    title = when (contentDetailsUiState) {
                        is ContentDetailUiState.Movie -> contentDetailsUiState.data.title
                        is ContentDetailUiState.TvSeries -> contentDetailsUiState.data.name
                        else -> ""
                    },
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopAppBar(
    showTitle: Boolean,
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBarWithBackButton(
        title = {
            AnimatedText(
                text = title,
                visible = showTitle
            )
        },
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        iconButtonColors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Black.copy(alpha = 0.5f),
            contentColor = Color.White
        ),
        onBackClick = onBackClick
    )
}



@Composable
internal fun OverviewSection(
    overview: String
) {
    Column {
        Text(
            text = stringResource(Res.string.overview),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(2.dp))
        if (overview.isEmpty()) {
            Text(text = stringResource(Res.string.not_available))
        } else {
            Text(overview)
        }
    }
}