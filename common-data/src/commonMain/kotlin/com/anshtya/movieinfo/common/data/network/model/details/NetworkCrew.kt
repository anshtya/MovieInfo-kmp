package com.anshtya.movieinfo.common.data.network.model.details

import com.anshtya.movieinfo.common.data.model.people.Crew
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCrew(
    @SerialName("credit_id") val creditId: String,
    val department: String?,
    val id: Int,
    val job: String?,
    val name: String,
    @SerialName("profile_path") val profilePath: String?
)

fun NetworkCrew.asModel(): Crew {
    return Crew(
        creditId = creditId,
        department = department ?: "",
        id = id,
        job = job ?: "",
        name = name,
        profilePath = profilePath ?: ""
    )
}
