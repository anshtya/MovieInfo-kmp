package com.anshtya.movieinfo.common.data.network.model.details

import com.anshtya.movieinfo.common.data.model.details.MovieDetails
import com.anshtya.movieinfo.common.data.network.model.NetworkContentResponse
import com.anshtya.movieinfo.common.data.network.model.asModel
import com.anshtya.movieinfo.common.data.util.formatCurrency
import com.anshtya.movieinfo.common.data.util.formatDateString
import com.anshtya.movieinfo.common.data.util.getDisplayLanguage
import com.anshtya.movieinfo.common.data.util.getFormattedRuntime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMovieDetails(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
//    val belongs_to_collection: Any,
    val budget: Int,
    val credits: NetworkCredits,
    val genres: List<NetworkGenre>?,
    val id: Int,
    @SerialName("original_language") val originalLanguage: String,
    val overview: String,
    val popularity: Float,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @SerialName("production_countries") val productionCountries: List<NetworkProductionCountry>,
    val recommendations: NetworkContentResponse,
    @SerialName("release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val tagline: String,
    val title: String,
    @SerialName("vote_average") val voteAverage: Float,
    @SerialName("vote_count") val voteCount: Int
)

fun NetworkMovieDetails.asModel(): MovieDetails {
    return MovieDetails(
        adult = adult,
        backdropPath = backdropPath ?: "",
        budget = formatCurrency(budget),
        credits = credits.asModel(),
        genres = genres?.map { it.name } ?: emptyList(),
        id = id,
        originalLanguage = getDisplayLanguage(originalLanguage),
        overview = overview,
        posterPath = posterPath ?: "",
        productionCompanies = productionCompanies.joinToString(separator = ", ") { it.name },
        productionCountries = productionCountries.joinToString(separator = ", ") { it.name },
        rating = voteAverage / 2,
        recommendations = recommendations.results.map { it.asModel() },
        releaseDate = formatDateString(releaseDate),
        releaseYear = releaseDate.split("-").first().toInt(),
        revenue = formatCurrency(revenue),
        runtime = getFormattedRuntime(runtime),
        tagline = tagline,
        title = title,
        voteCount = voteCount
    )
}