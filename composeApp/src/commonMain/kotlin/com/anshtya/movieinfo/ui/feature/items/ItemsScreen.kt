package com.anshtya.movieinfo.ui.feature.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.common.data.model.category.TvSeriesListCategory
import com.anshtya.movieinfo.ui.FeedType
import com.anshtya.movieinfo.ui.component.LazyVerticalContentGrid
import com.anshtya.movieinfo.ui.component.MediaItemCard
import com.anshtya.movieinfo.ui.component.TopAppBarWithBackButton
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.airing_today
import movieinfo.composeapp.generated.resources.now_playing
import movieinfo.composeapp.generated.resources.on_air
import movieinfo.composeapp.generated.resources.popular
import movieinfo.composeapp.generated.resources.top_rated
import movieinfo.composeapp.generated.resources.upcoming
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val padding = 8.dp

@Composable
fun ItemsRoute(
    onNavigateUp: () -> Unit,
    onNavigateToItem: (Int, MediaType) -> Unit,
    viewModel: ItemsViewModel = koinViewModel()
) {
    val categoryDisplayName = when (viewModel.item.type) {
        FeedType.MOVIE -> {
            val category = enumValueOf<MovieListCategory>(viewModel.item.category)
            when (category) {
                MovieListCategory.NOW_PLAYING -> stringResource(Res.string.now_playing)
                MovieListCategory.UPCOMING -> stringResource(Res.string.upcoming)
                MovieListCategory.TOP_RATED -> stringResource(Res.string.top_rated)
                MovieListCategory.POPULAR -> stringResource(Res.string.popular)
            }
        }

        FeedType.TV -> {
            val category = enumValueOf<TvSeriesListCategory>(viewModel.item.category)
            when (category) {
                TvSeriesListCategory.AIRING_TODAY -> stringResource(Res.string.airing_today)
                TvSeriesListCategory.ON_THE_AIR -> stringResource(Res.string.on_air)
                TvSeriesListCategory.TOP_RATED -> stringResource(Res.string.top_rated)
                TvSeriesListCategory.POPULAR -> stringResource(Res.string.popular)
            }
        }
    }

    val content by viewModel.uiState.collectAsStateWithLifecycle()

    ItemsScreen(
        content = content,
        categoryDisplayName = categoryDisplayName,
        appendItems = viewModel::appendItems,
        onItemClick = onNavigateToItem,
        onBackClick = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemsScreen(
    content: ItemsUiState,
    categoryDisplayName: String,
    appendItems: () -> Unit,
    onItemClick: (Int, MediaType) -> Unit,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = {
                    Text(
                        text = categoryDisplayName,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyVerticalContentGrid(
            pagingEnabled = true,
            itemsEmpty = content.items.isEmpty(),
            isLoading = content.isLoading,
            endReached = content.endReached,
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + padding,
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + padding,
                top = paddingValues.calculateTopPadding() + padding,
                bottom = paddingValues.calculateBottomPadding() + padding
            ),
            appendItems = appendItems,
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .fillMaxWidth()
        ) {
            items(
                items = content.items,
                key = { it.id }
            ) {
                MediaItemCard(
                    posterPath = it.imagePath,
                    onItemClick = { onItemClick(it.id, MediaType.TV) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
            if (content.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}