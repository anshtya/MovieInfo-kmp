package com.anshtya.movieinfo.common.data.repository.test.data

import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.model.details.tv.TvEpisodeDetails
import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.model.people.Credits
import com.anshtya.movieinfo.common.data.model.people.PersonDetails

val testMovieDetail = MovieDetails(
    adult = false,
    backdropPath = "path",
    budget = 1000000.toString(),
    credits = Credits(cast = emptyList(), crew = emptyList()),
    genres = emptyList(),
    id = 100,
    originalLanguage = "English",
    overview = "overview",
    posterPath = "path",
    productionCompanies = "",
    productionCountries = "",
    rating = 1.0f,
    recommendations = listOf(),
    releaseDate = "",
    releaseYear = 2004,
    revenue = "",
    runtime = "100",
    tagline = "tagline",
    title = "Movie Name",
    voteCount = 100
)

val testTvSeriesDetails = TvSeriesDetails(
    adult = false,
    backdropPath = "path",
    createdBy = listOf(),
    credits = Credits(cast = emptyList(), crew = emptyList()),
    episodeRunTime = "",
    firstAirDate = "",
    genres = emptyList(),
    id = 101,
    inProduction = "",
    lastAirDate = "",
    lastEpisodeToAir = TvEpisodeDetails(
        airDate = "",
        episodeNumber = 0,
        id = 110,
        name = "",
        overview = "",
        productionCode = "",
        runtime = 10,
        seasonNumber = 0,
        showId = 0,
        stillPath = "",
        voteAverage = 0f,
        voteCount = 0
    ),
    name = "",
    networks = "",
    nextEpisodeToAir = null,
    numberOfEpisodes = 0,
    numberOfSeasons = 0,
    originCountry = emptyList(),
    originalLanguage = "",
    overview = "",
    posterPath = "",
    productionCompanies = "",
    productionCountries = "",
    rating = 0f,
    recommendations = emptyList(),
    releaseYear = 0,
    status = "",
    tagline = "",
    type = "",
    voteCount = 0
)

val testPersonDetails = PersonDetails(
    adult = true,
    alsoKnownAs = "",
    biography = "",
    birthday = "",
    deathday = "",
    gender = "",
    id = 102,
    knownForDepartment = "",
    name = "name",
    placeOfBirth = "",
    profilePath = ""
)