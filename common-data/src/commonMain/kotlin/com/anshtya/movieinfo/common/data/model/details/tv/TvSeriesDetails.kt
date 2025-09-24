package com.anshtya.movieinfo.common.data.model.details.tv

import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.people.Credits

data class TvSeriesDetails(
    val adult: Boolean,
    val backdropPath: String,
    val createdBy: List<CreatedBy>,
    val credits: Credits,
    val episodeRunTime: String,
    val firstAirDate: String,
    val genres: List<String>,
    val id: Int,
    val inProduction: String,
    val lastAirDate: String,
    val lastEpisodeToAir: TvEpisodeDetails,
    val name: String,
    val networks: String,
    val nextEpisodeToAir: TvEpisodeDetails?,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val rating: Double,
    val recommendations: List<Content>,
    val releaseYear: Int,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    val voteCount: Int
)

fun TvSeriesDetails.asLibraryItem(): LibraryItem {
    return LibraryItem(
        id = id,
        imagePath = posterPath,
        name = name,
        mediaType = MediaType.TV.name.lowercase(),
    )
}