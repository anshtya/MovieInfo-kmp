package com.anshtya.movieinfo.ui.feature.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.category.MovieListCategory
import com.anshtya.movieinfo.ui.component.LazyVerticalContentGrid
import com.anshtya.movieinfo.ui.component.MediaItemCard
import com.anshtya.movieinfo.ui.component.TopAppBarWithBackButton
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.now_playing
import movieinfo.composeapp.generated.resources.popular
import movieinfo.composeapp.generated.resources.top_rated
import movieinfo.composeapp.generated.resources.upcoming
import org.jetbrains.compose.resources.stringResource

private val horizontalPadding = 8.dp

@Composable
fun ItemsRoute(
    categoryName: String,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MoviesViewModel
) {
    val category = enumValueOf<MovieListCategory>(categoryName)
    val content by when (category) {
        MovieListCategory.NOW_PLAYING -> viewModel.nowPlayingMovies.collectAsStateWithLifecycle()
        MovieListCategory.UPCOMING -> viewModel.upcomingMovies.collectAsStateWithLifecycle()
        MovieListCategory.TOP_RATED -> viewModel.topRatedMovies.collectAsStateWithLifecycle()
        MovieListCategory.POPULAR -> viewModel.popularMovies.collectAsStateWithLifecycle()
    }

    val categoryDisplayName = when (category) {
        MovieListCategory.NOW_PLAYING -> stringResource(Res.string.now_playing)
        MovieListCategory.UPCOMING -> stringResource(Res.string.upcoming)
        MovieListCategory.TOP_RATED -> stringResource(Res.string.top_rated)
        MovieListCategory.POPULAR -> stringResource(Res.string.popular)
    }

    ItemsScreen(
        content = content,
        categoryDisplayName = categoryDisplayName,
        appendItems = viewModel::appendItems,
        onItemClick = onItemClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemsScreen(
    content: ContentUiState,
    categoryDisplayName: String,
    appendItems: (MovieListCategory) -> Unit,
    onItemClick: (String) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            LazyVerticalContentGrid(
                pagingEnabled = true,
                itemsEmpty = content.items.isEmpty(),
                isLoading = content.isLoading,
                endReached = content.endReached,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                appendItems = { appendItems(content.category) }
            ) {
                items(
                    items = content.items,
                    key = { it.id }
                ) {
                    MediaItemCard(
                        posterPath = it.imagePath,
                        onItemClick = { onItemClick("${it.id},${MediaType.MOVIE}") },
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
}