package com.anshtya.movieinfo.ui.feature.you.library_items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.ui.component.LazyVerticalContentGrid
import com.anshtya.movieinfo.ui.component.MediaItemCard
import com.anshtya.movieinfo.ui.component.TopAppBarWithBackButton
import kotlinx.coroutines.launch
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.delete
import movieinfo.composeapp.generated.resources.favorites
import movieinfo.composeapp.generated.resources.no_items
import movieinfo.composeapp.generated.resources.watchlist
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LibraryItemsRoute(
    onBackClick: () -> Unit,
    navigateToDetails: (Int, MediaType) -> Unit,
    viewModel: LibraryItemsViewModel = koinViewModel()
) {
    val movieItems by viewModel.movieItems.collectAsStateWithLifecycle()
    val tvItems by viewModel.tvItems.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LibraryItemsScreen(
        movieItems = movieItems,
        tvItems = tvItems,
        libraryType = viewModel.libraryType,
        errorMessage = errorMessage,
        onDeleteItem = viewModel::deleteItem,
        onBackClick = onBackClick,
        onItemClick = navigateToDetails,
        onErrorShown = viewModel::onErrorShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryItemsScreen(
    movieItems: List<LibraryItem>,
    tvItems: List<LibraryItem>,
    libraryType: LibraryType?,
    errorMessage: String?,
    onDeleteItem: (LibraryItem) -> Unit,
    onItemClick: (Int, MediaType) -> Unit,
    onBackClick: () -> Unit,
    onErrorShown: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    val libraryItemTitle = when (libraryType) {
        LibraryType.FAVORITE -> stringResource(Res.string.favorites)
        LibraryType.WATCHLIST -> stringResource(Res.string.watchlist)
        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = {
                    libraryItemTitle?.let { Text(text = it) }
                },
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxSize()
        ) {
            val libraryMediaTabs = LibraryMediaType.entries
            val pagerState = rememberPagerState(pageCount = { libraryMediaTabs.size })

            val selectedTabIndex by remember(pagerState.currentPage) {
                mutableIntStateOf(pagerState.currentPage)
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                libraryMediaTabs.forEachIndexed { index, mediaTypeTab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = stringResource(mediaTypeTab.displayName)) }
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                when (libraryMediaTabs[page]) {
                    LibraryMediaType.MOVIE -> {
                        LibraryContent(
                            content = movieItems,
                            onItemClick = onItemClick,
                            onDeleteClick = onDeleteItem
                        )
                    }

                    LibraryMediaType.TV -> {
                        LibraryContent(
                            content = tvItems,
                            onItemClick = onItemClick,
                            onDeleteClick = onDeleteItem
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryContent(
    content: List<LibraryItem>,
    onItemClick: (Int, MediaType) -> Unit,
    onDeleteClick: (LibraryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (content.isEmpty()) {
        Box(modifier.fillMaxSize()) {
            Text(
                text = stringResource(Res.string.no_items),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyVerticalContentGrid(
            pagingEnabled = false,
            contentPadding = PaddingValues(
                start = 10.dp,
                end = 10.dp,
                top = 8.dp,
                bottom = WindowInsets.navigationBars
                    .asPaddingValues().calculateBottomPadding() + 4.dp
            ),
            modifier = modifier.fillMaxSize()
        ) {
            items(
                items = content,
                key = { it.id }
            ) {
                LibraryItem(
                    posterPath = it.imagePath,
                    onItemClick = {
                        onItemClick(
                            it.id,
                            enumValueOf<MediaType>(it.mediaType.uppercase())
                        )
                    },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
    }
}

@Composable
private fun LibraryItem(
    posterPath: String,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box {
        MediaItemCard(
            posterPath = posterPath,
            onItemClick = onItemClick
        )

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(42.dp)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(Res.string.delete),
                tint = Color.Black,
                modifier = Modifier
                    .clickable(onClick = onDeleteClick)
                    .padding(4.dp)
            )
        }
    }
}