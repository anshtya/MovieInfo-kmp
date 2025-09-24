package com.anshtya.movieinfo.common.data.network.model.details

import com.anshtya.movieinfo.common.data.model.people.Cast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCast(
    val character: String?,
    val id: Int,
    val name: String,
    @SerialName("profile_path") val profilePath: String?
)

fun NetworkCast.asModel(): Cast {
    return Cast(
        character = character ?: "",
        id = id,
        name = name,
        profilePath = profilePath ?: ""
    )
}