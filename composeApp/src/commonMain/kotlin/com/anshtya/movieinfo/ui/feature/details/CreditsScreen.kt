package com.anshtya.movieinfo.ui.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.anshtya.movieinfo.common.data.model.people.Credits
import com.anshtya.movieinfo.ui.component.PersonImage
import com.anshtya.movieinfo.ui.component.TopAppBarWithBackButton
import com.anshtya.movieinfo.ui.component.noRippleClickable
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.cast
import movieinfo.composeapp.generated.resources.credits
import movieinfo.composeapp.generated.resources.crew
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CreditsRoute(
    onNavigateToItem: (Int, MediaType) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel
) {
    val details by viewModel.contentDetailsUiState.collectAsStateWithLifecycle()

    CreditsScreen(
        details = details,
        onItemClick = onNavigateToItem,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreditsScreen(
    details: ContentDetailUiState,
    onItemClick: (Int, MediaType) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBackButton(
                title = {
                    Text(
                        text = stringResource(Res.string.credits),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        when (details) {
            is ContentDetailUiState.Movie -> {
                CreditsLazyColumn(
                    credits = details.data.credits,
                    contentPadding = paddingValues,
                    onItemClick = onItemClick,
                    modifier = Modifier.consumeWindowInsets(paddingValues)
                )
            }

            is ContentDetailUiState.TvSeries -> {
                CreditsLazyColumn(
                    credits = details.data.credits,
                    contentPadding = paddingValues,
                    onItemClick = onItemClick,
                    modifier = Modifier.consumeWindowInsets(paddingValues)
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun CreditsLazyColumn(
    credits: Credits,
    contentPadding: PaddingValues,
    onItemClick: (Int, MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
            top = contentPadding.calculateTopPadding() + 2.dp,
            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
            bottom = contentPadding.calculateBottomPadding() + 2.dp,
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        stickyHeader {
            CategoryHeader(stringResource(Res.string.cast))
        }
        items(
            items = credits.cast,
            key = { it.id }
        ) {
            CreditsItem(
                name = it.name,
                role = it.character,
                imagePath = it.profilePath,
                onItemClick = {
                    onItemClick(it.id, MediaType.PERSON)
                }
            )
        }

        if (credits.crew.isNotEmpty()) {
            item {
                CategoryHeader(text = stringResource(Res.string.crew))
            }

            val crewListByDepartment = credits.crew.groupBy { it.department }
            crewListByDepartment.forEach { mapEntry ->
                stickyHeader {
                    CategoryHeader(text = mapEntry.key)
                }
                items(
                    items = mapEntry.value,
                    key = { it.creditId }
                ) {
                    CreditsItem(
                        name = it.name,
                        role = it.job,
                        imagePath = it.profilePath,
                        onItemClick = {
                            onItemClick(it.id, MediaType.PERSON)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreditsItem(
    name: String,
    role: String,
    imagePath: String,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onItemClick() }
            .padding(horizontal = horizontalPadding, vertical = 6.dp)
    ) {
        PersonImage(
            imageUrl = imagePath,
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(2.dp))
            Text(text = role)
        }
    }
}

@Composable
private fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
    )
}