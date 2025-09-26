package com.anshtya.movieinfo.common.data.model.details

import com.anshtya.movieinfo.common.data.model.Content
import com.anshtya.movieinfo.common.data.model.LibraryItem
import com.anshtya.movieinfo.common.data.model.MediaType
import com.anshtya.movieinfo.common.data.model.people.Credits

data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String,
//    val belongs_to_collection: Any,
    val budget: String,
    val credits: Credits,
    val genres: List<String>,
    val id: Int,
    val originalLanguage: String,
    val overview: String,
    val posterPath: String,
    val productionCompanies: String,
    val productionCountries: String,
    val rating: Float,
    val recommendations: List<Content>,
    val releaseDate: String,
    val releaseYear: Int,
    val revenue: String,
    val runtime: String,
    val tagline: String,
    val title: String,
    val voteCount: Int
)

fun MovieDetails.asLibraryItem(): LibraryItem {
    return LibraryItem(
        id = id,
        imagePath = posterPath,
        name = title,
        mediaType = MediaType.MOVIE.name.lowercase()
    )
}