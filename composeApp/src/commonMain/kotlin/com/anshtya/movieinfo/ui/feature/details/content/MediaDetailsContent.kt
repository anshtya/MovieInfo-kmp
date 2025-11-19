package com.anshtya.movieinfo.ui.feature.details.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.people.Cast
import com.anshtya.movieinfo.ui.component.AnimatedText
import com.anshtya.movieinfo.ui.component.ContentSectionHeader
import com.anshtya.movieinfo.ui.component.LazyRowContentSection
import com.anshtya.movieinfo.ui.component.LibraryActionButton
import com.anshtya.movieinfo.ui.component.MediaItemCard
import com.anshtya.movieinfo.ui.component.Rating
import com.anshtya.movieinfo.ui.component.TmdbImage
import com.anshtya.movieinfo.ui.component.TopAppBarWithBackButton
import com.anshtya.movieinfo.ui.component.noRippleClickable
import com.anshtya.movieinfo.ui.feature.details.DetailsUiState
import com.anshtya.movieinfo.ui.feature.details.horizontalPadding
import com.anshtya.movieinfo.ui.feature.details.verticalPadding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.add_to_favorites
import movieinfo.composeapp.generated.resources.add_to_watchlist
import movieinfo.composeapp.generated.resources.details_sign_in_sheet
import movieinfo.composeapp.generated.resources.not_available
import movieinfo.composeapp.generated.resources.recommendations
import movieinfo.composeapp.generated.resources.remove_from_favorites
import movieinfo.composeapp.generated.resources.remove_from_watchlist
import movieinfo.composeapp.generated.resources.sign_in
import movieinfo.composeapp.generated.resources.sign_in_sheet_text
import movieinfo.composeapp.generated.resources.top_billed_cast
import movieinfo.composeapp.generated.resources.view_all
import org.jetbrains.compose.resources.stringResource

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
internal fun MediaDetailsContent(
    uiState: DetailsUiState,
    backdropPath: String,
    voteCount: Int,
    name: String,
    rating: Float,
    releaseYear: Int,
    runtime: String,
    tagline: String,
    genres: List<String>,
    overview: String,
    cast: List<Cast>,
    recommendations: List<Content>,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onSeeAllCastClick: () -> Unit,
    onCastClick: (Int, MediaType) -> Unit,
    onRecommendationClick: (Int) -> Unit,
    onHideBottomSheet: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )
    LaunchedEffect(uiState.showSignInSheet) {
        if (uiState.showSignInSheet) {
            scaffoldState.bottomSheetState.expand()
        }
    }

    val backdropExpandedHeight = 220.dp
    val collapsedHeight = TopAppBarDefaults.TopAppBarExpandedHeight +
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val heightToCollapse = backdropExpandedHeight - collapsedHeight

    val heightToCollapsePx = with(LocalDensity.current) { heightToCollapse.toPx() }
    // persist collapse offset between different Details screen
    var savedCollapseOffsetPx by rememberSaveable { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember(heightToCollapsePx) {
        ExitOnlyCollapseNestedConnection(heightToCollapsePx)
    }

    LaunchedEffect(Unit) {
        // set value of savedCollapseOffsetPx when returning from different Details screen
        nestedScrollConnection.collapseOffsetPx = savedCollapseOffsetPx

        // whenever backdrop collapses or expands, save collapse offset
        snapshotFlow { nestedScrollConnection.collapseOffsetPx }
            .debounce(500L)
            .collect { offset ->
                savedCollapseOffsetPx = offset
            }
    }

    val backdropHeight = with(LocalDensity.current) {
        (backdropExpandedHeight.toPx() + nestedScrollConnection.collapseOffsetPx).toDp()
    }
    val isBackdropCollapsed by remember(backdropHeight) {
        derivedStateOf { backdropHeight == collapsedHeight }
    }
    val scrollValue = 1 - ((backdropExpandedHeight - backdropHeight) / heightToCollapse)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
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
                        verticalArrangement = Arrangement.spacedBy(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                    ) {
                        Text(
                            text = stringResource(Res.string.sign_in_sheet_text),
                            style = MaterialTheme.typography.bodyLarge
                        )
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
        sheetPeekHeight = 0.dp,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection)
        ) {
            Box(Modifier.fillMaxWidth()) {
                BackdropImageSection(
                    path = backdropPath,
                    scrollValue = scrollValue,
                    modifier = Modifier.height(backdropHeight)
                )
                DetailsTopAppBar(
                    showTitle = isBackdropCollapsed,
                    title = name,
                    onBackClick = onBackClick,
                    modifier = Modifier.statusBarsPadding()
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    InfoSection(
                        voteCount = voteCount,
                        name = name,
                        rating = rating,
                        releaseYear = releaseYear,
                        runtime = runtime,
                        tagline = tagline
                    )
                }

                item { GenreSection(genres) }

                item {
                    LibraryActions(
                        isFavorite = uiState.markedFavorite,
                        isAddedToWatchList = uiState.savedInWatchlist,
                        onFavoriteClick = onFavoriteClick,
                        onWatchlistClick = onWatchlistClick
                    )
                }

                item {
                    TopBilledCast(
                        cast = cast,
                        onCastClick = onCastClick,
                        onSeeAllCastClick = onSeeAllCastClick
                    )
                }

                item { OverviewSection(overview) }

                item { content() }

                item {
                    Recommendations(
                        recommendations = recommendations,
                        onRecommendationClick = onRecommendationClick
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                        .navigationBarsPadding()
                        .height(10.dp)
                    )
                }
            }
        }
    }
}

private class ExitOnlyCollapseNestedConnection(
    private val heightToCollapse: Float
) : NestedScrollConnection {
    var collapseOffsetPx by mutableFloatStateOf(0f)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y

        // if scrolling down, don't consume anything
        if (delta > 0f) return Offset.Zero

        val previousOffset = collapseOffsetPx
        val newOffset = collapseOffsetPx + delta
        collapseOffsetPx = newOffset.coerceIn(-heightToCollapse, 0f)
        return if (previousOffset != collapseOffsetPx) {
            // We are in the middle of top app bar collapse
            available
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // change height of top app bar when scrolling all the way down and
        // child has finished scrolling
        if (consumed.y >= 0f && available.y > 0f) {
            val prevOffset = collapseOffsetPx
            val newOffset = collapseOffsetPx + available.y
            collapseOffsetPx = newOffset.coerceIn(-heightToCollapse, 0f)
            return Offset(x = 0f, y = (collapseOffsetPx - prevOffset))
        }

        return Offset.Zero
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopAppBar(
    showTitle: Boolean,
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
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
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
internal fun DetailItem(
    fieldName: String,
    value: String
) {
    val text = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append(fieldName)
        }
        append(value)
    }
    Text(text)
}

@Composable
private fun BackdropImageSection(
    path: String,
    scrollValue: Float,
    modifier: Modifier = Modifier
) {
    Surface(modifier.fillMaxWidth()) {
        TmdbImage(
            width = 1280,
            imageUrl = path,
            contentScale = ContentScale.Crop,
            modifier = Modifier.alpha(scrollValue)
        )
    }
}

@Composable
private fun InfoSection(
    voteCount: Int,
    name: String,
    rating: Float,
    releaseYear: Int,
    tagline: String,
    runtime: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (runtime.isNotEmpty()) {
                Text(runtime)
                if (releaseYear.toString().isNotEmpty()) {
                    Text("|")
                    Text("$releaseYear")
                }
            } else {
                Text("$releaseYear")
            }
        }

        Rating(rating = rating, count = voteCount)

        if (tagline.isNotEmpty()) {
            Text(
                text = tagline,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun GenreSection(
    genres: List<String>
) {
    if (genres.isNotEmpty()) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach {
                GenreButton(name = it)
            }
        }
    }
}

@Composable
private fun TopBilledCast(
    cast: List<Cast>,
    onCastClick: (Int, MediaType) -> Unit,
    onSeeAllCastClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ContentSectionHeader(
            sectionName = stringResource(Res.string.top_billed_cast),
            onSeeAllClick = onSeeAllCastClick
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
                .padding(bottom = 2.dp)
        ) {
            cast.forEach {
                CastItem(
                    id = it.id,
                    imagePath = it.profilePath,
                    name = it.name,
                    characterName = it.character,
                    onItemClick = onCastClick
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(130.dp)
                    .noRippleClickable { onSeeAllCastClick() }
            ) {
                Text(
                    text = stringResource(Res.string.view_all),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .noRippleClickable { onSeeAllCastClick() }
                )
            }
        }
    }
}

@Composable
private fun Recommendations(
    recommendations: List<Content>,
    onRecommendationClick: (Int) -> Unit
) {
    LazyRowContentSection(
        pagingEnabled = false,
        sectionHeaderContent = {
            Text(
                text = stringResource(Res.string.recommendations),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        rowContent = {
            if (recommendations.isEmpty()) {
                item {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(Res.string.not_available),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            } else {
                items(
                    items = recommendations,
                    key = { it.id }
                ) {
                    MediaItemCard(
                        posterPath = it.imagePath,
                        onItemClick = { onRecommendationClick(it.id) }
                    )
                }
            }
        },
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun CastItem(
    id: Int,
    imagePath: String,
    name: String,
    characterName: String,
    onItemClick: (Int, MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(130.dp)
                .noRippleClickable {
                    onItemClick(id, MediaType.PERSON)
                }
        ) {
            TmdbImage(
                width = 500,
                imageUrl = imagePath,
                modifier = modifier
                    .height(140.dp)
//                    .noRippleClickable {
//                        onItemClick(id, MediaType.PERSON)
//                    }
            )
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(2.dp))
                Text(text = characterName)
            }
        }
    }
}

@Composable
private fun LibraryActions(
    isFavorite: Boolean,
    isAddedToWatchList: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(top = 6.dp, bottom = 4.dp)
    ) {
        LibraryActionButton(
            name = if (isFavorite) {
                stringResource(Res.string.remove_from_favorites)
            } else {
                stringResource(Res.string.add_to_favorites)
            },
            icon = Icons.Rounded.Favorite,
            iconTint = if (isFavorite) {
                Color.Red
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            onClick = onFavoriteClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        LibraryActionButton(
            name = if (isAddedToWatchList) {
                stringResource(Res.string.remove_from_watchlist)
            } else {
                stringResource(Res.string.add_to_watchlist)
            },
            icon = if (isAddedToWatchList) {
                Icons.Rounded.Bookmark
            } else {
                Icons.Outlined.BookmarkBorder
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, Color.Black),
            onClick = onWatchlistClick,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
    }
}

@Composable
private fun GenreButton(
    name: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(8.dp)
        )
    }
}