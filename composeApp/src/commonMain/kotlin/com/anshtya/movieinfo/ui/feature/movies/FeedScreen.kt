package com.anshtya.movieinfo.ui.feature.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.ui.FeedType
import com.anshtya.movieinfo.ui.component.ContentSectionHeader
import com.anshtya.movieinfo.ui.component.LazyRowContentSection
import com.anshtya.movieinfo.ui.component.MediaItemCard
import kotlinx.coroutines.launch
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.now_playing
import movieinfo.composeapp.generated.resources.popular
import movieinfo.composeapp.generated.resources.top_rated
import movieinfo.composeapp.generated.resources.upcoming
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val horizontalPadding = 8.dp

@Composable
internal fun MoviesFeedRoute(
    navigateToDetails: (Int, MediaType) -> Unit,
    navigateToItems: (FeedType, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val nowPlayingMovies by viewModel.nowPlayingMovies.collectAsStateWithLifecycle()
    val popularMovies by viewModel.popularMovies.collectAsStateWithLifecycle()
    val topRatedMovies by viewModel.topRatedMovies.collectAsStateWithLifecycle()
    val upcomingMovies by viewModel.upcomingMovies.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    MoviesFeedScreen(
        nowPlayingMovies = nowPlayingMovies,
        popularMovies = popularMovies,
        topRatedMovies = topRatedMovies,
        upcomingMovies = upcomingMovies,
        errorMessage = errorMessage,
        appendItems = viewModel::appendItems,
        onItemClick = navigateToDetails,
        onSeeAllClick = navigateToItems,
        onErrorShown = viewModel::onErrorShown,
        modifier = modifier
    )
}

@Composable
internal fun MoviesFeedScreen(
    nowPlayingMovies: ContentUiState,
    popularMovies: ContentUiState,
    topRatedMovies: ContentUiState,
    upcomingMovies: ContentUiState,
    errorMessage: String?,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (Int, MediaType) -> Unit,
    onSeeAllClick: (FeedType, String) -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    errorMessage?.let {
        scope.launch { snackbarState.showSnackbar(it) }
        onErrorShown()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 8.dp,
                bottom = paddingValues.calculateBottomPadding() + 8.dp,
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
            ),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ContentSection(
                    content = nowPlayingMovies,
                    sectionName = stringResource(Res.string.now_playing),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                ContentSection(
                    content = popularMovies,
                    sectionName = stringResource(Res.string.popular),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                ContentSection(
                    content = topRatedMovies,
                    sectionName = stringResource(Res.string.top_rated),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
            item {
                ContentSection(
                    content = upcomingMovies,
                    sectionName = stringResource(Res.string.upcoming),
                    appendItems = appendItems,
                    onItemClick = onItemClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
        }
    }
}

@Composable
private fun ContentSection(
    content: ContentUiState,
    sectionName: String,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (Int, MediaType) -> Unit,
    onSeeAllClick: (FeedType, String) -> Unit
) {
    LazyRowContentSection(
        pagingEnabled = true,
        isLoading = content.isLoading,
        endReached = content.endReached,
        itemsEmpty = content.items.isEmpty(),
        rowContentPadding = PaddingValues(horizontal = horizontalPadding),
        appendItems = { appendItems(content.category) },
        sectionHeaderContent = {
            ContentSectionHeader(
                sectionName = sectionName,
                onSeeAllClick = { onSeeAllClick(FeedType.MOVIE, content.category.name) },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
        },
        rowContent = {
            items(
                items = content.items,
                key = { it.id }
            ) {
                MediaItemCard(
                    posterPath = it.imagePath,
                    onItemClick = {
                        onItemClick(it.id, MediaType.MOVIE)
                    }
                )
            }

            if (content.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(110.dp)
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        },
        modifier = Modifier.height(160.dp)
    )
}