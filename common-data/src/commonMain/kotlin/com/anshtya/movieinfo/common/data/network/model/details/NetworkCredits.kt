package com.anshtya.movieinfo.common.data.network.model.details

import com.anshtya.movieinfo.common.data.model.people.Credits
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCredits(
    val cast: List<NetworkCast>,
    val crew: List<NetworkCrew>
)

fun NetworkCredits.asModel(): Credits {
    return Credits(
        cast = cast.map { it.asModel() },
        crew = crew.map { it.asModel() }
    )
}
