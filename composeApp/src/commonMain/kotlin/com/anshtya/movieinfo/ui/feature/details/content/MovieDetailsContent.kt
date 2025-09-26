package com.anshtya.movieinfo.ui.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.model.details.asLibraryItem
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.budget
import movieinfo.composeapp.generated.resources.original_language
import movieinfo.composeapp.generated.resources.production_companies
import movieinfo.composeapp.generated.resources.production_countries
import movieinfo.composeapp.generated.resources.release_date
import movieinfo.composeapp.generated.resources.revenue
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MovieDetailsContent(
    movieDetails: MovieDetails,
    isFavorite: Boolean,
    isAddedToWatchList: Boolean,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onCastClick: (String) -> Unit,
    onRecommendationClick: (String) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onBackdropCollapse: (Boolean) -> Unit,
) {
    MediaDetailsContent(
        backdropPath = movieDetails.backdropPath,
        voteCount = movieDetails.voteCount,
        name = movieDetails.title,
        rating = movieDetails.rating,
        releaseYear = movieDetails.releaseYear,
        runtime = movieDetails.runtime,
        tagline = movieDetails.tagline,
        genres = movieDetails.genres,
        overview = movieDetails.overview,
        cast = movieDetails.credits.cast.take(10),
        recommendations = movieDetails.recommendations,
        isFavorite = isFavorite,
        isAddedToWatchList = isAddedToWatchList,
        onFavoriteClick = { onFavoriteClick(movieDetails.asLibraryItem()) },
        onWatchlistClick = { onWatchlistClick(movieDetails.asLibraryItem()) },
        onSeeAllCastClick = onSeeAllCastClick,
        onCastClick = onCastClick,
        onRecommendationClick = { id ->
            onRecommendationClick("${id},${MediaType.MOVIE}")
        },
        onBackdropCollapse = onBackdropCollapse
    ) {
        MovieDetailsSection(
            releaseDate = movieDetails.releaseDate,
            originalLanguage = movieDetails.originalLanguage,
            productionCompanies = movieDetails.productionCompanies,
            productionCountries = movieDetails.productionCountries,
            budget = movieDetails.budget,
            revenue = movieDetails.revenue
        )
    }
}

@Composable
private fun MovieDetailsSection(
    releaseDate: String,
    originalLanguage: String,
    productionCompanies: String,
    productionCountries: String,
    budget: String,
    revenue: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        DetailItem(
            fieldName = stringResource(Res.string.release_date),
            value = releaseDate
        )

        DetailItem(
            fieldName = stringResource(Res.string.original_language),
            value = originalLanguage
        )

        DetailItem(
            fieldName = stringResource(Res.string.budget),
            value = "$${budget}"
        )

        DetailItem(
            fieldName = stringResource(Res.string.revenue),
            value = "$${revenue}"
        )

        DetailItem(
            fieldName = stringResource(Res.string.production_companies),
            value = productionCompanies
        )

        DetailItem(
            fieldName = stringResource(Res.string.production_countries),
            value = productionCountries
        )
    }
}