package com.anshtya.movieinfo.common.data.network.model.details.tv

import com.anshtya.movieinfo.common.data.model.details.tv.TvSeriesDetails
import com.anshtya.movieinfo.common.data.network.model.NetworkContentResponse
import com.anshtya.movieinfo.common.data.network.model.asModel
import com.anshtya.movieinfo.common.data.network.model.details.NetworkCredits
import com.anshtya.movieinfo.common.data.network.model.details.NetworkGenre
import com.anshtya.movieinfo.common.data.network.model.details.NetworkProductionCompany
import com.anshtya.movieinfo.common.data.network.model.details.NetworkProductionCountry
import com.anshtya.movieinfo.common.data.network.model.details.asModel
import com.anshtya.movieinfo.common.data.util.formatDateString
import com.anshtya.movieinfo.common.data.util.getDisplayLanguage
import com.anshtya.movieinfo.common.data.util.getFormattedRuntime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTvSeriesDetails(
    val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("created_by") val createdBy: List<NetworkCreatedBy>,
    val credits: NetworkCredits,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>,
    @SerialName("first_air_date") val firstAirDate: String,
    val genres: List<NetworkGenre>?,
    val id: Int,
    @SerialName("in_production") val inProduction: Boolean,
    @SerialName("last_air_date") val lastAirDate: String,
    @SerialName("last_episode_to_air") val lastEpisodeToAir: NetworkEpisodeDetails,
    val name: String,
    val networks: List<NetworkBroadcastNetwork>,
    @SerialName("next_episode_to_air") val nextEpisodeToAir: NetworkEpisodeDetails?,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int,
    @SerialName("number_of_seasons") val numberOfSeasons: Int,
    @SerialName("origin_country") val originCountry: List<String>,
    @SerialName("original_language") val originalLanguage: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("production_companies") val productionCompanies: List<NetworkProductionCompany>,
    @SerialName("production_countries") val productionCountries: List<NetworkProductionCountry>,
    val recommendations: NetworkContentResponse,
//    val seasons: List<Season>,
    val status: String,
    val tagline: String,
    val type: String,
    @SerialName("vote_average") val voteAverage: Float,
    @SerialName("vote_count") val voteCount: Int
) {
    fun getRuntime(): Int {
        return episodeRunTime.first()
    }
}

fun NetworkTvSeriesDetails.asModel(): TvSeriesDetails {
    return TvSeriesDetails(
        adult = adult,
        backdropPath = backdropPath ?: "",
        createdBy = createdBy.map(NetworkCreatedBy::asModel),
        credits = credits.asModel(),
        episodeRunTime = if (episodeRunTime.isEmpty()) "" else getFormattedRuntime(getRuntime()),
        firstAirDate = formatDateString(firstAirDate),
        genres = genres?.map { it.name } ?: emptyList(),
        id = id,
        inProduction = if (inProduction) "Yes" else "No",
        lastAirDate = formatDateString(lastAirDate),
        lastEpisodeToAir = lastEpisodeToAir.asModel(),
        name = name,
        networks = networks.joinToString(separator = ", ") { it.name },
        nextEpisodeToAir = nextEpisodeToAir?.asModel(),
        numberOfEpisodes = numberOfEpisodes,
        numberOfSeasons = numberOfSeasons,
        originCountry = originCountry,
        originalLanguage = getDisplayLanguage(originalLanguage),
        overview = overview,
        posterPath = posterPath ?: "",
        productionCompanies = productionCompanies.joinToString(separator = ", ") { it.name },
        productionCountries = productionCountries.joinToString(separator = ", ") { it.name },
        recommendations = recommendations.results.map { it.asModel() },
        releaseYear = firstAirDate.split("-").first().toInt(),
        status = status,
        tagline = tagline,
        type = type,
        rating = voteAverage / 2,
        voteCount = voteCount
    )
}