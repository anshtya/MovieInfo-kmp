package com.anshtya.movieinfo.ui.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.model.details.tv.asLibraryItem
import com.anshtya.movieinfo.ui.feature.details.DetailsUiState
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.first_air_date
import movieinfo.composeapp.generated.resources.in_production
import movieinfo.composeapp.generated.resources.last_air_date
import movieinfo.composeapp.generated.resources.networks
import movieinfo.composeapp.generated.resources.next_air_date
import movieinfo.composeapp.generated.resources.number_episodes
import movieinfo.composeapp.generated.resources.number_seasons
import movieinfo.composeapp.generated.resources.original_language
import movieinfo.composeapp.generated.resources.production_companies
import movieinfo.composeapp.generated.resources.production_countries
import movieinfo.composeapp.generated.resources.status
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TvShowDetailsContent(
    uiState: DetailsUiState,
    tvSeriesDetails: TvSeriesDetails,
    onFavoriteClick: (LibraryItem) -> Unit,
    onWatchlistClick: (LibraryItem) -> Unit,
    onSeeAllCastClick: () -> Unit,
    onCastClick: (Int, MediaType) -> Unit,
    onRecommendationClick: (Int, MediaType) -> Unit,
    onHideBottomSheet: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MediaDetailsContent(
        uiState = uiState,
        backdropPath = tvSeriesDetails.backdropPath,
        voteCount = tvSeriesDetails.voteCount,
        name = tvSeriesDetails.name,
        rating = tvSeriesDetails.rating,
        releaseYear = tvSeriesDetails.releaseYear,
        runtime = tvSeriesDetails.episodeRunTime,
        tagline = tvSeriesDetails.tagline,
        genres = tvSeriesDetails.genres,
        overview = tvSeriesDetails.overview,
        cast = tvSeriesDetails.credits.cast.take(10),
        recommendations = tvSeriesDetails.recommendations,
        onFavoriteClick = { onFavoriteClick(tvSeriesDetails.asLibraryItem()) },
        onWatchlistClick = { onWatchlistClick(tvSeriesDetails.asLibraryItem()) },
        onSeeAllCastClick = onSeeAllCastClick,
        onCastClick = onCastClick,
        onRecommendationClick = {
            onRecommendationClick(it, MediaType.TV)
        },
        onHideBottomSheet = onHideBottomSheet,
        onSignInClick = onSignInClick,
        onBackClick = onBackClick,
        modifier = modifier
    ) {
        TvDetailsSection(
            originalLanguage = tvSeriesDetails.originalLanguage,
            firstAirDate = tvSeriesDetails.firstAirDate,
            lastAirDate = tvSeriesDetails.lastAirDate,
            inProduction = tvSeriesDetails.inProduction,
            status = tvSeriesDetails.status,
            nextAirDate = tvSeriesDetails.nextEpisodeToAir?.airDate,
            numberOfEpisodes = tvSeriesDetails.numberOfEpisodes,
            numberOfSeasons = tvSeriesDetails.numberOfSeasons,
            networks = tvSeriesDetails.networks,
            productionCompanies = tvSeriesDetails.productionCompanies,
            productionCountries = tvSeriesDetails.productionCountries
        )
    }
}

@Composable
private fun TvDetailsSection(
    originalLanguage: String,
    firstAirDate: String,
    lastAirDate: String,
    inProduction: String,
    status: String,
    nextAirDate: String?,
    numberOfEpisodes: Int,
    numberOfSeasons: Int,
    networks: String,
    productionCompanies: String,
    productionCountries: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(bottom = 6.dp)
    ) {
        DetailItem(
            fieldName = stringResource(Res.string.original_language),
            value = originalLanguage
        )

        DetailItem(
            fieldName = stringResource(Res.string.first_air_date),
            value = firstAirDate
        )

        DetailItem(
            fieldName = stringResource(Res.string.last_air_date),
            value = lastAirDate
        )

        DetailItem(
            fieldName = stringResource(Res.string.in_production),
            value = inProduction
        )

        DetailItem(
            fieldName = stringResource(Res.string.status),
            value = status
        )

        nextAirDate?.let {
            DetailItem(
                fieldName = stringResource(Res.string.next_air_date),
                value = it
            )
        }

        DetailItem(
            fieldName = stringResource(Res.string.number_episodes),
            value = "$numberOfEpisodes"
        )

        DetailItem(
            fieldName = stringResource(Res.string.number_seasons),
            value = "$numberOfSeasons"
        )

        DetailItem(
            fieldName = stringResource(Res.string.networks),
            value = networks
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