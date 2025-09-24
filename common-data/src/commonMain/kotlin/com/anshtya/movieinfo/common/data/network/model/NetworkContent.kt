package com.anshtya.movieinfo.common.data.network.model

import com.anshtya.movieinfo.common.data.model.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkContent(
    val id: Int,
    val name: String?,
    @SerialName("poster_path") val posterPath: String?,
    val title: String?,
)

fun NetworkContent.asModel(): Content {
    return Content(
        id = id,
        imagePath = posterPath ?: "",
        name = name ?: title ?: ""
    )
}