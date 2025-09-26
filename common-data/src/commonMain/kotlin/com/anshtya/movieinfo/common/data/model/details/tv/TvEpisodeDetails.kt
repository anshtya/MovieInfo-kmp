package com.anshtya.movieinfo.common.data.model.details.tv

data class TvEpisodeDetails(
    val airDate: String,
    val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val productionCode: String,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String,
    val voteAverage: Float,
    val voteCount: Int
)