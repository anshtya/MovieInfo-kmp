package com.anshtya.movieinfo.common.data.network.model.details.tv

import com.anshtya.movieinfo.common.data.model.details.tv.TvEpisodeDetails
import com.anshtya.movieinfo.common.data.util.formatDateString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkEpisodeDetails(
    @SerialName("air_date") val airDate: String,
    @SerialName("episode_number") val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    @SerialName("production_code") val productionCode: String,
    val runtime: Int?,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("show_id") val showId: Int,
    @SerialName("still_path") val stillPath: String?,
    @SerialName("vote_average") val voteAverage: Float,
    @SerialName("vote_count") val voteCount: Int
)

fun NetworkEpisodeDetails.asModel(): TvEpisodeDetails {
    return TvEpisodeDetails(
        airDate = formatDateString(airDate),
        episodeNumber = episodeNumber,
        id = id,
        name = name,
        overview = overview,
        productionCode = productionCode,
        runtime = runtime,
        seasonNumber = seasonNumber,
        showId = showId,
        stillPath = stillPath ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}